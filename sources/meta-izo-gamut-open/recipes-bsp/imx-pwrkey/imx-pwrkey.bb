DESCRIPTION = "A daemon to handle soft power on/off behavior of IMX."
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://COPYING.BSD-3;md5=58dcd8452651fc8b07d1f65ce07ca8af"

SRC_URI="\
	file://COPYING.BSD-3 \
	file://imx-pwrkey.c \
	file://imx-pwrkey.sh \
	"

DAEMON="imx-pwrkey"

PR="r2"

S="${WORKDIR}"

do_compile() {
	cd ${S}
	$CC imx-pwrkey.c -o ${DAEMON}
}

do_install() {
	# make sure all the init.d dirs exist
	install -d ${D}${sysconfdir}/init.d
	install -d ${D}${sysconfdir}/rcS.d

	install -d ${D}${sbindir}
	install -m 0755 ${S}/${DAEMON} ${D}${sbindir}/${DAEMON}

	# daemon for handling soft power on/off button presses
	install -m 0755 ${WORKDIR}/imx-pwrkey.sh ${D}${sysconfdir}/init.d/
	ln -sf ../init.d/imx-pwrkey.sh ${D}${sysconfdir}/rcS.d/S95imx-pwrkey.sh
}