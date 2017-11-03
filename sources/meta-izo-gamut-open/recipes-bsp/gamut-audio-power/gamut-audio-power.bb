DESCRIPTION = "Gamut audio power scripts."
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://COPYING.BSD-3;md5=58dcd8452651fc8b07d1f65ce07ca8af"

SRC_URI = " \
	file://COPYING.BSD-3 \
	file://gamut-audio-power \
	"

S = "${WORKDIR}"

PR="r0"

#TODO: we need to fix the script to not use i2cset
# to set the PMIC 5.15v on
RDEPENDS_${PN} += "i2c-tools gamut-gpios lua-izo lua-posix"

S = "${WORKDIR}"

FILE_${PN} = " \
	/etc/init.d \
	/etc/rcS.d \
	"

do_install(){
	# make sure all the init.d dirs exist
	install -d ${D}${sysconfdir}/init.d
	install -d ${D}${sysconfdir}/rcS.d

	#install the gamut audio start script
	install -m 0755 ${WORKDIR}/gamut-audio-power ${D}${sysconfdir}/init.d/
	# create the link for the script
	ln -sf ../init.d/gamut-audio-power ${D}${sysconfdir}/rcS.d/S90gamut-audio-power
}