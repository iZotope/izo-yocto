include u-boot-fslc-gamut.inc

# need to override the license check as the file trivially changed
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=0507cd7da8e7ad6d6701926ec9b84c95"

PV = "v2015.10+git${SRCPV}"
PR = "r34"
SRCREV = "faf653a06d1589abc8a3bb2b501ac88cabdc0ce1"
SRCBRANCH = "public_github"
