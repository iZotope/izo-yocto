FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

# we want to hold the run time data for connman in the gamut non volitile
EXTRA_OECONF += " --localstatedir=${GAMUT_NV_LOCALSTATEDIR} "

# get rid of the init script for sysvinit
INHIBIT_UPDATERCD_BBCLASS = "1"
