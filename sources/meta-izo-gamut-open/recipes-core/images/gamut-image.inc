# This should include things that are global to ALL gamut images or that can
# easily be overridden by another image
SUMMARY = "A bootable system targeting iZotope gamut hardware."

IMAGE_INSTALL = "packagegroup-core-boot ${ROOTFS_PKGMANAGE_BOOTSTRAP} ${CORE_IMAGE_EXTRA_INSTALL}"

IMAGE_LINGUAS = " "

# inherit our image from core-image
inherit core-image
# common gamut image stuff
require gamut-image-common.inc

IMAGE_ROOTFS_SIZE ?= "8192"

# we want a read only rootfs
EXTRA_IMAGE_FEATURES += "read-only-rootfs"
# add extra hooks for setting up our read-only rootfs
read_only_rootfs_hook_append () {
    if ! [ -e "${IMAGE_ROOTFS}${sysconfdir}/ssh/ssh_host_rsa_key" ]; then

        # get the ssh keys into the Gamut nonvolatile location
        sed -i '/SYSCONFDIR/d' ${IMAGE_ROOTFS}/etc/default/ssh
        echo "SYSCONFDIR=${GAMUT_NV_LOCALSTATEDIR}/run/ssh" >> ${IMAGE_ROOTFS}/etc/default/ssh
    
        # Move the HostKeys to the Gamut nonvolatile storage
        sed -i '/HostKey/d' ${IMAGE_ROOTFS}${sysconfdir}/ssh/sshd_config_readonly
        echo "# HostKey modification via gamut-image.inc" >> ${IMAGE_ROOTFS}${sysconfdir}/ssh/sshd_config_readonly
        echo "HostKey ${GAMUT_NV_LOCALSTATEDIR}/run/ssh/ssh_host_rsa_key" >> ${IMAGE_ROOTFS}${sysconfdir}/ssh/sshd_config_readonly
        echo "HostKey ${GAMUT_NV_LOCALSTATEDIR}/run/ssh/ssh_host_dsa_key" >> ${IMAGE_ROOTFS}${sysconfdir}/ssh/sshd_config_readonly
        echo "HostKey ${GAMUT_NV_LOCALSTATEDIR}/run/ssh/ssh_host_ecdsa_key" >> ${IMAGE_ROOTFS}${sysconfdir}/ssh/sshd_config_readonly
    fi
}

pkg_all = ""
pkg_dev = ""
pkg_prd = ""
############################
# Target Packages
# Add to pkg_all for all images
# Add to pkg_dev for only dev image
# Add to pkg_prd for only prod image
############################
### Atmel and related
# the atmel firmware
pkg_all += "gamut-ui-firmware"
# for programming the Atmel
pkg_all += "openocd"

### Networking and Wifi
# connection manger for wifi connections
pkg_all += "connman connman-client"
# add the murata FW
pkg_all += "murata-fw"
# tools for managing the wifi
pkg_all += "iw"
# hostapd for WiFi access point.
pkg_all += "hostapd-gamut"
# CRDA for wifi regulatory domain stuff
pkg_all += "crda"

### Kernel and Boot
# make sure all in-tree modules get installed to root-fs
# TODO: make this only the modules we need
pkg_all += "kernel-modules"
# force the device tree to install to rootfs
pkg_all += "kernel-devicetree"
# Kernel image for booting
pkg_all += "kernel-image"
# install the u-boot binary
pkg_all += "u-boot-fslc-gamut"
# the gamut u-boot boot scripts
pkg_all += "gamut-u-boot-scripts"
# update script/utility
pkg_all += "gamut-update"
# script to create non-volatile storage for read only rootfs
pkg_all += "gamut-readonly"
# we need the full 'start-stop-daemon'
pkg_all += "dpkg-start-stop-daemon"

### Linux system related
# we want a full getty implementation
pkg_all += "mingetty"

### Hardware and BSP related
# For setting up/mounting gamut's partitions
pkg_all += "gamut-partitions-normal"
# enabled the audio power system
pkg_all += "gamut-audio-power"
# Reboot and shutdown
pkg_all += "gamut-power"
# Diagnostic routines
pkg_all += "gamut-diagnostic"
### General System packages
# set cpu speed to max on boot
pkg_all += "boot-freq"
# filesystem make commands (e.g. mkfs.ext2) possibly used for updates
pkg_dev += "e2fsprogs-mke2fs dosfstools"
# usb based networking
pkg_all += "usb-networking"
# udev rules for gamut's GPIOs
pkg_all += "gamut-gpios"
# For scheduling regularly intervaled jobs
pkg_all += "cronie"
# For logging user applications into circular buffer
pkg_all += "logrotate"
# Alsa-state saves alsa levels from boot to boot
pkg_all += "alsa-state"

### Lua related (for system scripting)
pkg_all += "lua"
pkg_all += "lua-izo"
pkg_all += "lua-resty-prettycjson"

### SSH related
# for sftp
pkg_all += "openssh-sftp-server"

########################################
## Everything below is dev image only! #
########################################

### Misc helpful for development
# tools for working with alsa
pkg_dev += "alsa-utils"

### General Debugging tools
# gdb for remote debugging
pkg_dev += "gdb gdbserver strace ltrace"
# include full ps command for debug builds
pkg_dev += "procps"
# devmem2 to peek and poke registers during driver debugging
pkg_all += "devmem2"
# I2C tools for early prototyping to connect to I2C Hardware
pkg_dev += "i2c-tools"
# For gdb debugging threads
pkg_dev += "glibc-dbg"
# For doing kernel events/scheduler forensics
pkg_dev += "trace-cmd"
# Package required for speed benchmark tests
pkg_dev += "iperf3"


############################
# Native tool chain and SDK only related
# TOOLCHAIN_HOST_TASK are host packages to go in SDK
# TOOLCHAIN_TARGET_TASK are target packages to go in SDK
############################
#nativesdk-python-misc is needed for filecmp package that is used by Gyp
TOOLCHAIN_HOST_TASK += "nativesdk-python-misc"
TOOLCHAIN_HOST_TASK += "nativesdk-google-breakpad"
# add Lua and friends to the Native SDK for use with lua unit testing
# this is a partial fix till we get full busted support in
TOOLCHAIN_HOST_TASK += "nativesdk-lua nativesdk-lua-dev"
TOOLCHAIN_HOST_TASK += "nativesdk-lua-izo"

# Include static Libs for avahi only in sdk
TOOLCHAIN_TARGET_TASK_append += "libavahi-common libavahi-core"
