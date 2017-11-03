/* Copyright (C) 2017, iZotope, Inc. All Rights Reserved
 * Released under the BSD-3 license (see COPYING.BSD-3)
 */
#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <string.h>
#include <fcntl.h>
#include <sys/mman.h>
#include <syslog.h>
#include <sys/reboot.h>
#include <unistd.h>

/* This is a general purpose register in the System Reset Controller. It is normally used by the i.MX6 boot ROM
 * to store the argument for a wakeup from sleep routine for Core0 when waking up from sleep. We repurpose it here 
 * to communicate withu-boot between warm boots since this wont mess with sleep.
 *  See "i.MX6SL reference manual Rev. 2, 06/2015" section "46.7.8 SRC General Purpose Register 2 (SRC_GPR2)" (page 2798).
 *
 * We use a few different bytes of the registers for different pourposes.
 *
 *   31  16 | 15     8 | 7     0
 * +-----------------------------+
 * | unused | BOOT_CNT | RCV_REQ |
 * +-----------------------------+
 *
 *   [7:0] : RCV_REQ  : if '0xff' signals U-Boot to go into recovery mode, otherwise will do a normal boot
 *  [15:8] : BOOT_CNT : u-boot will increment this each time it starts a boot process. The application
 *                      software should clear this upon a full successful boot (network comms can be
 *                      established). If the system gets caught in a reboot loop, u-boot will go into
 *                      recovery after some number of boot attempts (configurable in u-boot with an
 *                      env var).
 */
#define SRC_GPR2_ADDRESS 0x20d8024
#define GPR2_RCV_REQ_SHIFT (0)
#define GPR2_RCV_REQ_MASK (0xFF << GPR2_RCV_REQ_SHIFT)
#define REQUEST_RECOVERY_VALUE 0xFF
#define GPR2_BOOT_CNT_SHIFT (8)
#define GPR2_BOOT_CNT_MASK (0xFF << GPR2_BOOT_CNT_SHIFT)

/* vars for dealing with mapping physical memory into our virual address space */
static uint32_t* gpr2_virt_addr = NULL;

#define MAP_SIZE 4096UL
#define MAP_MASK (MAP_SIZE - 1)

/* syslog wrapper to also print to console */
#define log_msg(priority, ...) \
do { \
	printf(__VA_ARGS__); \
	printf("\n"); \
	syslog(priority, __VA_ARGS__); \
} while(0)

/* mmap the physical page for the GPR2 register into our virual address space
 * returns 0 on succes, other on failure
 * From devmem2: http://free-electrons.com/pub/mirror/devmem2.c
 */
static int gpr2_mmap() {
	int fd;
	static void* mmap_base;

	if((fd = open("/dev/mem", O_RDWR | O_SYNC)) == -1) {
		log_msg(LOG_ERR, "Error: Could not opening /dev/mem\n");
		return 1;
	}

	mmap_base = mmap(0, MAP_SIZE, PROT_READ | PROT_WRITE, MAP_SHARED, fd, SRC_GPR2_ADDRESS & ~MAP_MASK);
	if(mmap_base == (void *) -1) {
		log_msg(LOG_ERR, "Error: Could not map physical page for GPR2\n");
		return 1;
	}

	gpr2_virt_addr = mmap_base + (SRC_GPR2_ADDRESS & MAP_MASK);

	/* mmap will presist after closing the fd. The OS will automatically clean up the mapped memory on termination. */
	close(fd);

	return 0;
}

/* Write GPR2 register. will mmap the pyhsical page if it isn't already. Exits the program on error.*/
static void gpr2_write(uint32_t new_value, uint32_t mask) {
	if (!gpr2_virt_addr) {
		if (gpr2_mmap()) {
			log_msg(LOG_ERR, "Could not mmap GPR2 register.");
			exit(EXIT_FAILURE);
		}
	}

	/* nobody else in the system should be touching GPR2, don't worry about this not being atmoic */
	uint32_t read_value = *gpr2_virt_addr;
	read_value &= ~mask;
	new_value &= mask;
	read_value |= new_value;
	*gpr2_virt_addr =  read_value;
}

static void cleanup(void) {
	/* First, sync in case we end up doing a hard poweroff and need to protect against corruption */
	sync();

	/* Execute necessary init scripts */
	system("/etc/init.d/gamut-cleanup");

	/* Sync again to sync writes that might have happened during cleanup */
	sync();
}

static void request_recovery(void) {
	// Set a flag in the unused SRC_GPR2 register that will persist after a warm reboot
	gpr2_write(REQUEST_RECOVERY_VALUE << GPR2_RCV_REQ_SHIFT, GPR2_RCV_REQ_MASK);
}

static void clear_boot_cnt(void) {
	gpr2_write(0, GPR2_BOOT_CNT_MASK);
}

const char * usage = "Specify \"successful-boot\", \"reboot\", \"shutdown\", or \"recovery-reboot\"";

int main(int argc, char* argv[])
{
	/* Fail out if not run as root */
	if (getuid() != 0) {
		log_msg(LOG_WARNING, "Must be root to run gamut-boot.");
		return EXIT_FAILURE;
	}

	/* Setup check */
	if (argc != 2) {
		log_msg(LOG_WARNING, usage);
		return EXIT_FAILURE;
	}

	if (strcmp(argv[1], "shutdown") == 0) {
		log_msg(LOG_NOTICE, "Shutting down...");
		cleanup();
		reboot(RB_POWER_OFF);
	} else if (strcmp(argv[1], "reboot") == 0) {
		log_msg(LOG_NOTICE, "Rebooting...");
		cleanup();
		reboot(RB_AUTOBOOT);
	} else if (strcmp(argv[1], "recovery-reboot") == 0) {
		log_msg(LOG_NOTICE, "Rebooting into recovery mode...");
		cleanup();
		request_recovery();
		reboot(RB_AUTOBOOT);
	} else if (strcmp(argv[1], "successful-boot") == 0) {
		log_msg(LOG_NOTICE, "Successful boot indicated, clearing boot count.");
		clear_boot_cnt();
	} else {
		log_msg(LOG_WARNING, usage);
		return EXIT_FAILURE;
	}

	return 0;
}

