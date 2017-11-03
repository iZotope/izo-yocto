SUMMARY = "lua-resty-prettycjson is a JSON Pretty Formatter for Lua cJSON."
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=89c619e8d778c498640104d70143609c"
HOMEPAGE = "https://github.com/bungle/lua-resty-prettycjson"

PR = "r0"

RDEPENDS_${PN} = "lua-cjson"

S = "${WORKDIR}/git"

SRC_URI = " \
	git://github.com/bungle/lua-resty-prettycjson.git;protocol=https;tag=v${PV} \
	"

TARGET_DIR="${datadir}/lua/5.3/resty"

FILES_${PN} = " \
	${TARGET_DIR} \
"

do_install () {
	install -d ${D}${TARGET_DIR}
	install -m 0644 ${S}/lib/resty/prettycjson.lua ${D}${TARGET_DIR}
}

BBCLASSEXTEND = "native nativesdk"
