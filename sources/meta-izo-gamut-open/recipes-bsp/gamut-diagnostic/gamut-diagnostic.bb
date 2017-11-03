DESCRIPTION = "Diagnostic scripts for gamut."
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://COPYING.BSD-3;md5=58dcd8452651fc8b07d1f65ce07ca8af"

RDEPENDS_${PN} = "gamut-update jq e2fsprogs-e2fsck"

SRC_URI = " \
	file://COPYING.BSD-3 \
	file://fsck-data-partitions.sh  \
	file://fsck-partition \
	file://serial-write.sh  \
	file://factory-build-info.sh \
	"

S = "${WORKDIR}"

# don't forget to update if you change the script!
PR = "r6"

do_install () {
	install -d ${D}${sysconfdir}/init.d
	install -d ${D}${sysconfdir}/rcS.d
	install -d ${D}${sbindir}

	install -m 774 ${WORKDIR}/fsck-data-partitions.sh ${D}${sysconfdir}/init.d/
	install -m 774 ${WORKDIR}/factory-build-info.sh ${D}${sysconfdir}/init.d/
	install -m 774 ${WORKDIR}/serial-write.sh ${D}${sbindir}
	install -m 774 ${WORKDIR}/fsck-partition ${D}${sbindir}
	ln -sf ../init.d/fsck-data-partitions.sh ${D}${sysconfdir}/rcS.d/S02fsck-data-partitions.sh
	ln -sf ../init.d/factory-build-info.sh ${D}${sysconfdir}/rcS.d/S08factory-build-info.sh
}

PACKAGE_ARCH = "${MACHINE_ARCH}"
COMPATIBLE_MACHINE = "(gamut-r1)"
