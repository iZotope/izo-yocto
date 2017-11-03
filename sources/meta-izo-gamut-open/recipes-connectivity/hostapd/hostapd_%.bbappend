FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
	file://init-gamut.patch;apply=no \
"

# get rid of the init script for sysvinit
INHIBIT_UPDATERCD_BBCLASS = "1"

PACKAGES += "${PN}-gamut"
RDEPENDS_${PN}-gamut += "${PN}"

# exclude the sysconf dir, no config file or init file
FILES_${PN}_remove = "${sysconfdir}"
# add alts for hotapd init script
FILES_${PN} += "${sysconfdir}/init.d/hostapd.hostapd"
FILES_${PN}-gamut = "${sysconfdir}/init.d/hostapd.hostapd-gamut"
# add alts for hostapd conf file
FILES_${PN} += "${sysconfdir}/hostapd.conf.hostapd"

# make mutiple versions of the init and conf file for differnt versions
do_install_append() {
	# create versions of the init file
	mv ${D}${sysconfdir}/init.d/hostapd ${D}${sysconfdir}/init.d/hostapd.hostapd
	patch -o ${D}${sysconfdir}/init.d/hostapd.hostapd-gamut ${WORKDIR}/init ${WORKDIR}/init-gamut.patch
	chmod 777 ${D}${sysconfdir}/init.d/hostapd.hostapd-gamut

	# create versions of the config file
	mv ${D}${sysconfdir}/hostapd.conf ${D}${sysconfdir}/hostapd.conf.hostapd
}

# setup alternatives for the init file and config file
inherit update-alternatives

ALTERNATIVE_${PN} = "hostapd-init hostapd-config"
ALTERNATIVE_${PN}-recovery = "hostapd-config"
ALTERNATIVE_${PN}-gamut = "hostapd-init"

ALTERNATIVE_PRIORITY_${PN} = "10"
ALTERNATIVE_PRIORITY_${PN}-gamut = "20"

ALTERNATIVE_LINK_NAME[hostapd-init] = "${sysconfdir}/init.d/hostapd"
ALTERNATIVE_TARGET_hostapd-gamut[hostapd-init] = "${sysconfdir}/init.d/hostapd.hostapd-gamut"

ALTERNATIVE_LINK_NAME[hostapd-config] = "${sysconfdir}/hostapd.conf"
