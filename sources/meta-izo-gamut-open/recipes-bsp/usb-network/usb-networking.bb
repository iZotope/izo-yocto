DESCRIPTION = "turns on usb0 as an ethernet device for gamut"
LICENSE = "CLOSED"
LIC_FILES_CHKSUM = "file://COPYING.BSD-3;md5=58dcd8452651fc8b07d1f65ce07ca8af"

RDEPENDS_${PN} += "lua lua-izo devmem2"

SRC_URI=" \
	file://COPYING.BSD-3 \
	file://usb-networking \
"

PR="r1"

S="${WORKDIR}"

do_install() {
	install -d ${D}${sysconfdir}/init.d
	install -d ${D}${sysconfdir}/rc5.d

	install -m 0775 ${WORKDIR}/usb-networking ${D}${sysconfdir}/init.d
	ln -sf ../init.d/usb-networking ${D}${sysconfdir}/rc5.d/S08usb-networkinig
}
