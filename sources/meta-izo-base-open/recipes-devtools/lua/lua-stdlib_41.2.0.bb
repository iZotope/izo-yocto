SUMMARY = "stdlib is a library of modules for common programming tasks, \
including list, table and functional operations, objects, pickling, pretty-printing \
and command-line option parsing."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=97c3f0908c9d2f747338e51da4f3df0a"
HOMEPAGE = "https://lua-stdlib.github.io/lua-stdlib/"

PR = "r1"

RDEPENDS_${PN} += "lua"

S = "${WORKDIR}/git"

FILES_${PN} += "${datadir}/lua/5.3"

SRC_URI = " \
	git://github.com/lua-stdlib/lua-stdlib.git;protocol=https;tag=release-v${PV};branch=release \
	"

INSTALL_BASE="${datadir}/lua/5.3"

do_configure() {
	true
}

do_compile() {
	true
}

do_install () {
	install -d ${D}${INSTALL_BASE}
	install -m 644 ${S}/lib/std.lua ${D}${INSTALL_BASE}

	install -d ${D}${INSTALL_BASE}/std
	install -m 644 ${S}/lib/std/base.lua ${D}${INSTALL_BASE}/std
	install -m 644 ${S}/lib/std/container.lua ${D}${INSTALL_BASE}/std
	install -m 644 ${S}/lib/std/debug.lua ${D}${INSTALL_BASE}/std
	install -m 644 ${S}/lib/std/functional.lua ${D}${INSTALL_BASE}/std
	install -m 644 ${S}/lib/std/io.lua ${D}${INSTALL_BASE}/std
	install -m 644 ${S}/lib/std/list.lua ${D}${INSTALL_BASE}/std
	install -m 644 ${S}/lib/std/math.lua ${D}${INSTALL_BASE}/std
	install -m 644 ${S}/lib/std/object.lua ${D}${INSTALL_BASE}/std
	install -m 644 ${S}/lib/std/operator.lua ${D}${INSTALL_BASE}/std
	install -m 644 ${S}/lib/std/optparse.lua ${D}${INSTALL_BASE}/std
	install -m 644 ${S}/lib/std/package.lua ${D}${INSTALL_BASE}/std
	install -m 644 ${S}/lib/std/set.lua ${D}${INSTALL_BASE}/std
	install -m 644 ${S}/lib/std/strbuf.lua ${D}${INSTALL_BASE}/std
	install -m 644 ${S}/lib/std/strict.lua ${D}${INSTALL_BASE}/std
	install -m 644 ${S}/lib/std/string.lua ${D}${INSTALL_BASE}/std
	install -m 644 ${S}/lib/std/table.lua ${D}${INSTALL_BASE}/std
	install -m 644 ${S}/lib/std/tree.lua ${D}${INSTALL_BASE}/std

	install -d ${D}${INSTALL_BASE}/std/debug_init
	install -m 644 ${S}/lib/std/debug_init/init.lua ${D}${INSTALL_BASE}/std/debug_init
}

BBCLASSEXTEND = "native nativesdk"
