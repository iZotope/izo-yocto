DESCRIPTION = "tool for formatting partitions on Gamut"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://COPYING.BSD-3;md5=58dcd8452651fc8b07d1f65ce07ca8af"

SRC_URI = " \
	file://COPYING.BSD-3 \
	file://gamut-mkparts \
"

S = "${WORKDIR}"

PR = "r1"

S = "${WORKDIR}"

RDEPENDS_${PN} = " \
	e2fsprogs-mke2fs \
	util-linux-fdisk \
	bash \
	gamut-update \
	gamut-partitions-common \
	jq \
"

do_install(){
	install -d ${D}${sbindir}
	install -m 0774 ${WORKDIR}/gamut-mkparts ${D}${sbindir}
}
