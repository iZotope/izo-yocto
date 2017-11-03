FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
	file://sshd_init.patch;patchdir=${WORKDIR} \
"
