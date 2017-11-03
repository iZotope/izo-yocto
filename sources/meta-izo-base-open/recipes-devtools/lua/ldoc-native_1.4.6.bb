DESCRIPTION = "This project grew out of the documentation needs of Penlight \
(and not always getting satisfaction with LuaDoc) and depends on Penlight \
itself.(This allowed me to not write a lot of code.)"
LICENSE = "MIT"

LIC_FILES_CHKSUM = "file://COPYRIGHT;md5=4183d221633f1fc26c238fbb9e020e1f"

PR="r0"

SRC_URI = "https://github.com/stevedonovan/LDoc/archive/${PV}.tar.gz"

SRC_URI[md5sum] = "e292a11e9bbf1daf429d6f547d2f1afa"
SRC_URI[sha256sum] = "4b73e78a0325fb3c295d015ddb60b5cee5b647cecb5c50ce8f01319b53bd536f"

inherit native

S="${WORKDIR}/LDoc-${PV}"

do_configure() {
	true
}

do_install() {
    install -d ${D}/usr/bin
    make LUA=/usr/bin/lua DESTDIR=${D} install
}

