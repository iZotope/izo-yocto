SUMMARY = "The Lua CJSON module provides JSON support for Lua."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b1fee3afe4f4a4b26c13016123b2d08a"
HOMEPAGE = "https://www.kyne.com.au/~mark/software/lua-cjson.php"

PR = "r0"

SRCREV = "e8972ac754788d3ef10a57a36016d6c3e85ba20d"

DEPENDS="lua"

S = "${WORKDIR}/git"

FILES_${PN} += "${libdir}/lua/5.3"
FILES_${PN}-dbg += "${libdir}/lua/5.3/.debug"

SRC_URI = " \
	git://github.com/mpx/lua-cjson.git;protocol=https;branch=master \
	"

MAKE_OPTS="'PREFIX=${prefix}' 'LUA_INCLUDE_DIR=./' 'DESTDIR=${D}'"

do_compile () {
	# Don't use oe_runmake here, the '-e' option is uses breaks this build
	make ${MAKE_OPTS}
}

do_install () {
	make ${MAKE_OPTS} install
}

BBCLASSEXTEND = "native nativesdk"
