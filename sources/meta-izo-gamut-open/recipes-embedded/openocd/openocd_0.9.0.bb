DESCRIPTION = "Free and Open On-Chip Debugging, In-System Programming and Boundary-Scan Testing"
HOMEPAGE = "http://openocd.org/"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"
SRCREV = "7b8b2f944322161334e21f30709504e4d42da18e"
PV = "0.9.0"
PR = "r0"

inherit autotools

SRC_URI = "git://repo.or.cz/openocd.git"
S = "${WORKDIR}/git"
BUILD_DIR = "${WORKDIR}/build"

# Note, disable all interfaces but sysfsgpio that we are using
EXTRA_OECONF = " --enable-sysfsgpio --disable-ftdi \
--disable-stlink --disable-ti-icdi --disable-ulink \
--disable-usb-blaster-2 --disable-vsllink --disable-jlink \
--disable-osbdm --disable-opendous --disable-aice \
--disable-usbprog --disable-rlink --disable-armjtagew --disable-cmsis-dap "

SRC_URI += " \
	file://0001-add-gamut-board-config.patch \
"

do_configure() {
    cd ${S}
    NOCONFIGURE=true ./bootstrap
    cd ${BUILD_DIR}
    oe_runconf
}

