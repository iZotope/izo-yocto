DESCRIPTION = "Gamut GPIO udev rules."
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://COPYING.BSD-3;md5=58dcd8452651fc8b07d1f65ce07ca8af"

RDEPENDS_${PN} = "lua-izo lua-posix"

SRC_URI = " \
	file://COPYING.BSD-3 \
	file://gpios.csv \
	file://make_gpio_script.py \
	file://export_gpios.lua \
"

PR="r2"

S = "${WORKDIR}"


GPIOS_FILE = "gpios.json"

FILES_${PN} = " \
	${sysconfdir}/rcS.d \
	${sysconfdir}/init.d \
	${sysconfdir}/${GPIOS_FILE} \
"

do_compile() {
	python make_gpio_script.py > ${GPIOS_FILE}
}

do_install() {
	install -d ${D}${sysconfdir}/init.d
	install -d ${D}${sysconfdir}/rcS.d
	install -m 775 ${WORKDIR}/export_gpios.lua ${D}${sysconfdir}/init.d
	ln -s ../init.d/export_gpios.lua ${D}${sysconfdir}/rcS.d/S05export_gpios

	install -m 664 ${WORKDIR}/${GPIOS_FILE} ${D}${sysconfdir}
}

PACKAGE_ARCH = "${MACHINE_ARCH}"
