IMAGE_CLASSES = "image_types_uboot"
# this must be done after setting IMAGE_CLASSES above!
require gamut-image-rcv.inc

inherit izo_buildinfo

IMAGE_FSTYPES = "cpio.gz.u-boot"

IMAGE_INSTALL += "${pkg_all} ${pkg_rcv}"

