# Recipe created by recipetool

WS = "${WORKDIR}/git"
S = "${WS}"

LICENSE = "Unknown"
LIC_FILES_CHKSUM = "file://${WS}/LICENSE;md5=56c24a43c81c3af6fcf590851931489e \
                    file://${WS}/src/third_party/curl/COPYING;md5=49975302782d24b80d205ec689f3f25f"

PR = "r1"
SRCREV = "a1adc2ca7602fdbe8720d18b2f21f541ac73d890"
SRC_URI = "gitsm://github.com/iZotope/google-breakpad.git;protocol=https;branch=public_github"

inherit autotools
inherit nativesdk