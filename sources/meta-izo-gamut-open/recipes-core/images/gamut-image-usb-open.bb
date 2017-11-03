IMAGE_CLASSES = "image_types_uboot"
# this must be done after setting IMAGE_CLASSES above!
require gamut-image-rcv.inc

IMAGE_FSTYPES = "cpio.gz.u-boot"

IMAGE_FEATURES += "ssh-server-openssh"

IMAGE_FEATURES += "empty-root-password allow-empty-password"

IMAGE_INSTALL += "${pkg_all} ${pkg_usb}"

# allow password based login to gamut
ROOTFS_POSTPROCESS_COMMAND += "password_based_ssh; "
password_based_ssh() {
	sed -i '/^PasswordAuthentication/d' ${IMAGE_ROOTFS}/etc/ssh/sshd_config
}

select_first_usb () {
	# this is a nasty hack to make the USB image use the first USB port
	# (on the debug interface) for the USB network interface
	sed -i '/g_zero/d' ${IMAGE_ROOTFS}/etc/init.d/usb-networking
}

make_serial_dir () {
	# create a mount point for the serial partition
	    install -d -m 755 ${IMAGE_ROOTFS}/serial
}

ROOTFS_POSTPROCESS_COMMAND += "select_first_usb; make_serial_dir"