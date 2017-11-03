# the open embedded version of lua doesn't do a shared lib by default
# this adds that

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
	file://build-shared-lib.patch \
"

PR .= ".0"

do_install_append() {
	rmdir ${D}${libdir}/lua/5.3
	rmdir ${D}${libdir}/lua
	# symlinks for lib
	PV=${PV}
	minver=${PV%.*}
	ln -s liblua.so.${PV} ${D}${libdir}/liblua.so.${minver}
	ln -s liblua.so.${PV} ${D}${libdir}/liblua.so
}

BBCLASSEXTEND += "nativesdk"
