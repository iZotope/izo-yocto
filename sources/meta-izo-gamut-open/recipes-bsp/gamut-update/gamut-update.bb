DESCRIPTION = "update scripts for Gamut."
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://COPYING.BSD-3;md5=58dcd8452651fc8b07d1f65ce07ca8af"

# don't forget to update if you change the script!
PR = "r8"

S = "${WORKDIR}"

RDEPENDS_${PN} = "e2fsprogs-mke2fs bash diffutils jq"

SRC_URI = " \
	file://COPYING.BSD-3 \
	file://gamut-update \
	"

do_install () {
	install -d ${D}/${sbindir}
	install -d ${D}/etc
	install -m 774 ${WORKDIR}/gamut-update ${D}/${sbindir}
}

PACKAGE_ARCH = "${MACHINE_ARCH}"
COMPATIBLE_MACHINE = "(gamut-r1)"
