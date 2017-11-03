DESCRIPTION = "Murata WiFi firmware and support scripts."
LICENSE = "CLOSED"

PR = "r14"

RDEPENDS_${PN} += "procps"

SRC_URI= " \
	file://BCM43430A1.1DX.hcd \
	file://bcmdhd.cal \
	file://fw_bcmdhd.bin \
	file://murata.conf \
	file://wifi-ap.sh \
	file://wifi-normal.sh \
	file://wifi-scan.sh \
	file://tubes.sh \
	file://wifi-getmode.sh \
	file://wifi-set-ssid.sh \
	file://udhcpd \
	file://udhcpd.conf \
	"

WIFI_POWER_AP_MODE="700"
WIFI_POWER_WIFI_MODE="700"

WIFI_POWER_PATH="/etc/"
WIFI_POWER_AP_FILE="${WIFI_POWER_PATH}/wifi_power.ap"
WIFI_POWER_WIFI_FILE="${WIFI_POWER_PATH}/wifi_power.wifi"

FILES_${PN} += " \
	${base_libdir}/firmware/brcm/ \
	${WIFI_POWER_PATH} \
	"

do_install() {
	install -d ${D}${bindir}
	install -m 0755 ${WORKDIR}/wifi-ap.sh ${D}${bindir}
	install -m 0755 ${WORKDIR}/wifi-normal.sh ${D}${bindir}
	install -m 0755 ${WORKDIR}/wifi-scan.sh ${D}${bindir}
	install -m 0755 ${WORKDIR}/tubes.sh ${D}${bindir}
	install -m 0755 ${WORKDIR}/wifi-getmode.sh ${D}${bindir}
	install -m 0755 ${WORKDIR}/wifi-set-ssid.sh ${D}${bindir}

	install -d ${D}${sysconfdir}/firmware
	install -m 0660 ${WORKDIR}/BCM43430A1.1DX.hcd ${D}${sysconfdir}/firmware

	install -d ${D}${base_libdir}/firmware/brcm
	install -m 0660 ${WORKDIR}/bcmdhd.cal ${D}${base_libdir}/firmware/brcm/brcmfmac43430-sdio.txt
	install -m 0660 ${WORKDIR}/fw_bcmdhd.bin ${D}${base_libdir}/firmware/brcm/brcmfmac43430-sdio.bin

	install -d ${D}${sysconfdir}/modprobe.d
	install -m 0660 ${WORKDIR}/murata.conf ${D}${sysconfdir}/modprobe.d

	# udhcpd is part of BusyBox but doesn't include a configuration file or service daemon
	install -d ${D}${sysconfdir}/init.d
	install -m 0755 ${WORKDIR}/udhcpd ${D}${sysconfdir}/init.d
	install -m 0755 ${WORKDIR}/udhcpd.conf ${D}${sysconfdir}

	# create the wifi power files
	install -d ${D}/${WIFI_POWER_PATH}
	echo ${WIFI_POWER_AP_MODE} > ${D}/${WIFI_POWER_AP_FILE}
	echo ${WIFI_POWER_WIFI_MODE} > ${D}/${WIFI_POWER_WIFI_FILE}

	# setup the wifi AP ssid at boot time
	install -d ${D}${sysconfdir}/rcS.d
	ln -sf  /usr/bin/wifi-set-ssid.sh ${D}${sysconfdir}/rcS.d/S23wifi-set-ssid.sh

	# Start hostapd at boot
	install -d ${D}${sysconfdir}/rc5.d
	ln -s /usr/bin/wifi-ap.sh ${D}/etc/rc5.d/S05wifi-ap.sh
}
