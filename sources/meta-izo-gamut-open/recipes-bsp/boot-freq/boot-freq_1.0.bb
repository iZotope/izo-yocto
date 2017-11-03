DESCRIPTION = "Set CPU freq to max at boot time."
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://COPYING.BSD-3;md5=58dcd8452651fc8b07d1f65ce07ca8af"

PR = "r0"

SRC_URI = " \
	file://COPYING.BSD-3 \
	file://max-cpu-freq \
	"

S = "${WORKDIR}"

do_install(){
	# make sure the needed init.d dirs exist
	install -d ${D}${sysconfdir}/init.d
	install -d ${D}${sysconfdir}/rcS.d

	install -m 755 ${WORKDIR}/max-cpu-freq ${D}${sysconfdir}/init.d/
	ln -sf ../init.d/max-cpu-freq ${D}${sysconfdir}/rcS.d/S03max-cpu-freq
}
