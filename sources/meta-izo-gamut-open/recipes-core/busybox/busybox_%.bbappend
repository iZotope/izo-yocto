FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

# Add extra config fragmets to enable features for gamut in busybox
SRC_URI += " \
	file://izo-svlogd.cfg \
	file://izo-chrt.cfg \
	file://izo-rdev.cfg \
	"
