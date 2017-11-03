SUMMARY = "Recover images for iZotope gamut hardware."

IMAGE_LINGUAS = " "

# inherit our image from core-image
inherit core-image
# common gamut image stuff
require gamut-image-common.inc

# Avoid static /dev
USE_DEVFS = "1"

IMAGE_FEATURES += "ssh-server-openssh"

CORE_IMAGE_BASE_INSTALL = " \
    ${CORE_IMAGE_EXTRA_INSTALL} \
    packagegroup-core-boot \
"

pkg_all = ""
pkg_rcv = ""
pkg_usb = ""
############################
# Target Packages
# Add to pkg_all for all images
# Add to pkg_rcv for the recover ramfs only
############################
### BSP related stuff
pkg_rcv += "gamut-partitions-recovery"
# only common as the usb image shouldn't mount anything automatically during bootstrap
pkg_usb += "gamut-partitions-common"
# bootstrapping related
pkg_all += "gamut-update"
pkg_all += "gamut-mkparts"
# kernel and boot stuff
pkg_all += "gamut-u-boot-scripts"
pkg_all += "kernel-devicetree"
pkg_all += "kernel-modules"
# ui board updater
pkg_rcv += "gamut-ui-firmware-updater"
# set cpu speed to 1GHz after booting
pkg_all += "boot-freq"
# For wifi
pkg_all += "murata-fw"
pkg_all += "hostapd-gamut"
# Recovery is read-only.
pkg_all += "gamut-readonly"
# tools for managing the wifi
pkg_all += "iw"
# connection manger for wifi connections
pkg_rcv += "connman connman-client"
# needed for full 'start-stop-daemon'
pkg_all += "dpkg-start-stop-daemon"
# udev rules for gamut's GPIOs
pkg_all += "gamut-gpios"

# add images should have lua available
pkg_all += "lua-izo"

# give all images a key based ssh login
pkg_all += "ssh-keys-server"
pkg_all += "openssh-sftp-server"
# usb networking
pkg_all += "usb-networking"
# Reboot and shutdown"
pkg_all += "gamut-power"
# Diagnostic routines
pkg_all += "gamut-diagnostic"
# we want a read only rootfs
EXTRA_IMAGE_FEATURES += "read-only-rootfs"
