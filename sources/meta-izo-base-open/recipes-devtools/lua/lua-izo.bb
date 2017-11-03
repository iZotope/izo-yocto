SUMMARY = "iZotope genearl purpose library for lua on hardware"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=d734d47346878704905834c00124e94d"

SRCREV = "17727d8e93e2cf5c3118bad7d0163f2a190f0aa9"
PR = "r4"

RDEPENDS_${PN} = "lua-stdlib lua-cjson lua-resty-prettycjson lua-posix"

S = "${WORKDIR}/git"

SRC_URI = " \
	git://github.com/iZotope/lua-izo.git;protocol=https;branch=public_github \
	"

TARGET_DIR="${datadir}/lua/5.3"

FILES_${PN} += " \
	${TARGET_DIR} \
"

do_install () {
	make DESTDIR=${D}${base_prefix} install
}

BBCLASSEXTEND = "native nativesdk"
