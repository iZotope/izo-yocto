SUMMARY = "This is a POSIX binding for LuaJIT, Lua 5.1, 5.2 and 5.3"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=7dd2aad04bb7ca212e69127ba8d58f9f"
HOMEPAGE = "https://luaposix.github.io/luaposix/"

PR = "r1"

DEPENDS += "lua lua-native"

inherit autotools

SRC_URI = " \
	https://github.com/luaposix/luaposix/archive/release-v${PV}.tar.gz \
	"
SRC_URI[md5sum] = "b36ff049095f28752caeb0b46144516c"
SRC_URI[sha256sum] = "e66262f5b7fe1c32c65f17a5ef5ffb31c4d1877019b4870a5d373e2ab6526a21"

S="${WORKDIR}/luaposix-release-v${PV}"

FILES_${PN} = "\
	${libdir}/lua/5.3/ \
	${datadir}/lua/5.3/posix/ \
"

FILES_${PN}-dbg += "\
	${libdir}/lua/5.3/.debug \
"

# For some reason the build scrips get really confused with the corss-compile and 
# setting --prefix=/usr. Any other prefix seems to work though. We set it to a dummy
# name and then rename it to /usr after installing
DUMMY_DIR = "${prefix}/asdf"
EXTRA_OECONF = "--prefix=${DUMMY_DIR} --exec_prefix=${DUMMY_DIR}"

do_install_append () {
	mkdir -p ${D}${prefix}
	echo would move mv ${D}${DUMMY_DIR} ${D}${prefix}
	mv ${D}${DUMMY_DIR}/* ${D}${prefix}
	echo ${FILES}
	rmdir ${D}${DUMMY_DIR}
}

BBCLASSEXTEND = "native nativesdk"
