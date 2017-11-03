DESCRIPTION = "U-boot boot script for gamut device."
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://COPYING.BSD-3;md5=58dcd8452651fc8b07d1f65ce07ca8af"
DEPENDS = "u-boot-mkimage-native"

SRC_URI = " \
	file://COPYING.BSD-3 \
	file://boot.scr.txt  \
	"

S = "${WORKDIR}"

# don't forget to update if you change the boot script!
PR = "r0"

FILES_${PN} += "/boot/"

do_mkimage(){
	mkimage -T script -C none -n 'gamut boot script' -d ${WORKDIR}/boot.scr.txt ${WORKDIR}/boot.scr
}

addtask mkimage after do_compile before do_install

do_install () {
	install -d ${D}/boot
	install -m 644 ${WORKDIR}/boot.scr ${D}/boot
}

PACKAGE_ARCH = "${MACHINE_ARCH}"
COMPATIBLE_MACHINE = "(gamut-r1)"
