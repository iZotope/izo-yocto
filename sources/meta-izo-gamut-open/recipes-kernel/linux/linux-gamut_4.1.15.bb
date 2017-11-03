# Copyright (C) 2013-2015 Freescale Semiconductor
# Copyright (C) 2015-2017 iZotope, inc.
# Released under the MIT license (see COPYING.MIT for the terms)

SUMMARY = "Linux real-time Kernel"
DESCRIPTION = "Linux kernel that is based on Freescale's linux-imx, \
with added real-time capabilities."

require recipes-kernel/linux/linux-gamut.inc
require recipes-kernel/linux/linux-dtb.inc

DEPENDS += "lzop-native bc-native"

SRCBRANCH = "public_github"
PR = "r15"
SRCREV = "c50c7bf2ea9a9976a1388f233ee881a99aa7e63f"

SRC_URI += " \
"

COMPATIBLE_MACHINE = "(mx6)"
