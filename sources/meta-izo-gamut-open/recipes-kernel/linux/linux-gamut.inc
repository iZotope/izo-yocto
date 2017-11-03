# Copyright (C) 2012, 2015 O.S. Systems Software LTDA.
# Released under the MIT license (see COPYING.MIT for the terms)

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

inherit kernel fsl-kernel-localversion

# Put a local version until we have a true SRCREV to point to
LOCALVERSION ?= ""
SCMVERSION ?= "y"
SRCBRANCH ?= ""

SRC_URI = "git://github.com/iZotope/linux-fslc.git;branch=${SRCBRANCH};protocol=https"

S = "${WORKDIR}/git"

# copy defconfig into config if it exisits in the git repo for our defconfig workflow
do_configure_prepend () {
	# always copy in the defconfig from the repo if it's there
	# this covers the case where it is updated in the upstream repo
	if [ -f "${S}/defconfig" ]; then
		cp "${S}/defconfig" "${WORKDIR}/defconfig"
		bbnote "Using defconfig from git repo."
	fi
}

# We need to pass it as param since kernel might support more then one
# machine, with different entry points
KERNEL_EXTRA_ARGS += "LOADADDR=${UBOOT_ENTRYPOINT}"
