/* Copyright (C) 2017, iZotope, Inc. All Rights Reserved
 * Released under the BSD-3 license (see COPYING.BSD-3)
 */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <fcntl.h>
#include <poll.h>
#include <linux/input.h>
#include <sys/reboot.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <syslog.h>

#define DAEMON_NAME "imx-pwrkey"
#define NAMEMAX 256

struct power_info {
	struct input_event ev[64];
	int fd;
	int rd;
	int value;
	int size;
	char name[NAMEMAX];
	char *device;
};

void power_info_init(struct power_info *power_info, char* device)
{
	power_info->size = sizeof (struct input_event);
	strncpy(power_info->name, "Unknown", NAMEMAX);
	power_info->device = device;

	if ((power_info->fd = open(power_info->device, O_RDONLY)) == -1) {
		syslog(LOG_NOTICE, "%s is not a valid device.\n",
		       power_info->device);
		exit(EXIT_FAILURE);
	}

	ioctl(power_info->fd, EVIOCGNAME(sizeof(power_info->name)),
	      power_info->name);
	syslog(LOG_NOTICE, "Reading from: %s (%s)\n", power_info->device,
	       power_info->name);
}

void poll_power_fd(struct power_info *power_info)
{
	struct pollfd pollfds[] = {
		{ .fd = power_info->fd, .events = POLLIN },
	};

	int poll_result = poll(pollfds, sizeof(pollfds) / sizeof(*pollfds), -1);

	if (pollfds[0].revents & POLLIN) {
		struct input_event ev;

		power_info->rd = read(power_info->fd, power_info->ev,
				      power_info->size * 64);
		if (power_info->rd < power_info->size) {
			syslog(LOG_NOTICE, "read(...) failed");
			exit(EXIT_FAILURE);
		}

		ev = power_info->ev[0];

		if (ev.value == 1 && ev.type == 1) {
			syslog(LOG_NOTICE, "Code[%d]", ev.code);
		}

		sync();
		
		char *const args[] = { "/sbin/poweroff", NULL };
		execv(args[0], args);
	}
}

int main(int argc, char* argv[])
{
	struct power_info power_info;
	setlogmask(LOG_UPTO(LOG_NOTICE));
	openlog(DAEMON_NAME, LOG_CONS | LOG_NDELAY | LOG_PERROR | LOG_PID,
		LOG_USER);

	syslog(LOG_INFO, "Entering Daemon");

	pid_t pid, sid;

	/* Fail out if not run as root */
	if (getuid() != 0) {
		syslog(LOG_NOTICE, "Must be root to run this.");
		exit(EXIT_FAILURE);
	}

	/* Fork off the parent process */
	pid = fork();
	if (pid < 0) {
		syslog(LOG_NOTICE, "Failed to fork off daemon");
		exit(EXIT_FAILURE);
	}
	/* Exit the parent process if we get a good PID */
	if (pid > 0) {
		exit(EXIT_SUCCESS);
	}

	umask(0);

	/* Create a new SID for the child process */
	sid = setsid();
	if (sid < 0) {
		syslog(LOG_NOTICE, "Failed to set SID for child process");
		exit(EXIT_FAILURE);
	}

	/* Change the current working directory */
	if ((chdir("/")) < 0) {
		syslog(LOG_NOTICE, "Failed to change current working directory");
		exit(EXIT_FAILURE);
	}

	/* Close out the standard file descriptors */
	close(STDIN_FILENO);
	close(STDOUT_FILENO);
	close(STDERR_FILENO);

	/* Setup check */
	if (argv[1] == NULL) {
		syslog(LOG_NOTICE, "Please specify (as command line argument) the path to the dev event interface device");
		exit(EXIT_FAILURE);
	}

	power_info_init(&power_info, argv[1]);

	while (1) {
		poll_power_fd(&power_info);
	}

	closelog();
}
