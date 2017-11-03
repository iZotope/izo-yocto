DESCRIPTION = "Shutdown and reboot scripts for gamut"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://COPYING.BSD-3;md5=58dcd8452651fc8b07d1f65ce07ca8af"

SRC_URI="\
	file://COPYING.BSD-3 \
	file://gamut-boot.c \
	file://gamut-shutdown \
	file://gamut-reboot \
	file://gamut-recovery-reboot \
	file://gamut-cleanup \
	"

S = "${WORKDIR}"

BOOT_COMMAND="gamut-boot"

PR="r3"

S="${WORKDIR}"

do_compile() {
	cd ${S}
	$CC gamut-boot.c -o ${BOOT_COMMAND}
}

do_install() {
	install -d ${D}/${sysconfdir}/init.d
	install -d ${D}${sbindir}
	install -m 0755 ${S}/${BOOT_COMMAND} ${D}${sbindir}/${BOOT_COMMAND}
	install -m 0755 ${WORKDIR}/gamut-shutdown ${D}${sbindir}
	install -m 0755 ${WORKDIR}/gamut-reboot ${D}${sbindir}
	install -m 0755 ${WORKDIR}/gamut-recovery-reboot ${D}${sbindir}
	install -m 0755 ${WORKDIR}/gamut-cleanup ${D}${sysconfdir}/init.d/
}
