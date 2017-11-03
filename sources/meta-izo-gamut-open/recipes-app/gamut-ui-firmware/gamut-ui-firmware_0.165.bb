DESCRIPTION = "The gamut ui firmware."
LICENSE = "CLOSED"

PR = "r5"
RDEPENDS_${PN} = "openocd"
FW_BUILD = "${PV}"

SRC_URI = "\
	file://gamut_ui_firmware_build_${FW_BUILD}.bin \
	file://update-atmel.cfg \
	file://ui-board-update.sh \
	"

S = "${WORKDIR}"

FILES_${PN} += " \
	${base_libdir}/firmware/gamut-ui \
	${sysconfdir}/init.d \
	${sysconfdir}/rcS.d \
"

do_install() {
   install -d ${D}${base_libdir}/firmware/gamut-ui
   install -m 0644 ${WORKDIR}/gamut_ui_firmware_build_${FW_BUILD}.bin ${D}${base_libdir}/firmware/gamut-ui/gamut_ui_firmware_build_${FW_BUILD}.bin
   ln -s ${base_libdir}/firmware/gamut-ui/gamut_ui_firmware_build_${FW_BUILD}.bin  ${D}${base_libdir}/firmware/gamut-ui/gamut_ui_firmware.bin

   # cfg for openocd
   install -m 0644 ${WORKDIR}/update-atmel.cfg ${D}${base_libdir}/firmware/gamut-ui/update-atmel.cfg

   # init scripts for auto update at boot
    install -d ${D}${sysconfdir}/init.d/
    install -d ${D}${sysconfdir}/rcS.d/
    install -m 0755 ${WORKDIR}/ui-board-update.sh ${D}${sysconfdir}/init.d/
}
