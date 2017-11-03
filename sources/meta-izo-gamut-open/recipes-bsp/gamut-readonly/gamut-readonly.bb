DESCRIPTION = "scripts to support Gamut read only rootfs"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://COPYING.BSD-3;md5=58dcd8452651fc8b07d1f65ce07ca8af"

S = "${WORKDIR}"

SRC_URI = " \
	file://COPYING.BSD-3 \
	file://gamut-make-nv.sh  \
	"

# don't forget to update if you change the script!
PR = "r0"

do_install () {
	# make sure all the init.d dirs exist
	install -d ${D}${sysconfdir}/init.d
	install -d ${D}${sysconfdir}/rcS.d
	install -m 774 ${WORKDIR}/gamut-make-nv.sh ${D}${sysconfdir}/init.d
	ln -sf ../init.d/gamut-make-nv.sh ${D}${sysconfdir}/rcS.d/S04gamut-make-nv.sh
}

PACKAGE_ARCH = "${MACHINE_ARCH}"
COMPATIBLE_MACHINE = "(gamut-r1)"
