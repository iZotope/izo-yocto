###################################
# Copyright (C) 2017, iZotope, Inc. All Rights Reserved
# Released under the BSD-3 license (see COPYING.BSD-3)
#
# Generate Gamut update image type
###################################

inherit image_types

## Fix allowing of empty ssh password image feature from image.bbclass
# The ssh_allow_empty_password hook from image.bbclass doesn't account for
# read-only rootfs which uses sshd_config_readonly for sshd. This will properly
# apply the same changes to the read only config as well.
# looks like this has been fixed upstream in Poky, we can remove when we update
ssh_allow_empty_password_append () {
	if [ -e ${IMAGE_ROOTFS}${sysconfdir}/ssh/sshd_config_readonly ]; then
		sed -i 's/^[#[:space:]]*PermitRootLogin.*/PermitRootLogin yes/' ${IMAGE_ROOTFS}${sysconfdir}/ssh/sshd_config_readonly
		sed -i 's/^[#[:space:]]*PermitEmptyPasswords.*/PermitEmptyPasswords yes/' ${IMAGE_ROOTFS}${sysconfdir}/ssh/sshd_config_readonly
	fi
}

# Set the default compression for gamut updates
UPDATE_ROOTFS_COMPRESSION ??= ".gz"

###
# Below is all for the Gamut update image type

TMP_UPDATE_DIR = "${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}-update"
ROOTFS_TAR = "rootfs.tar${UPDATE_ROOTFS_COMPRESSION}"

# Generate the meta data file for use in the update bundle
# arg 1 : file name to output the meta data to (will be overwritten if exists)
generate_meta () {
	FILE=$1
	cat >> $FILE <<EOF
META_ROOTFS_FILE="${ROOTFS_TAR}"
EOF
}

# the update image format is a bundle for updating Gamuts using the gamut-update script
# It's essentially a tar file that contains a hash of all files in it, a meta file (which
# is just a shell script to be run on update), and the rootfs itself.
# There are some influential variables for this image type:
#   ${UPDATE_ROOTFS_COMPRESSION} - the compression to use on the rootfs tar (e.g. ".gz" or ".bz")
IMAGE_CMD_update () {
	mkdir -p ${TMP_UPDATE_DIR}

	generate_meta ${TMP_UPDATE_DIR}/meta

	ln -s ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.tar${UPDATE_ROOTFS_COMPRESSION} ${TMP_UPDATE_DIR}/${ROOTFS_TAR}

	# make an MD5 sum for all files in the folder
	(cd ${TMP_UPDATE_DIR}; sha256sum `find -L -type f ` > sha256)
	# create the update bundle, dereference symlinks so we don't have to copy large files
	(cd ${TMP_UPDATE_DIR}; tar cf ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.update --dereference ./*)
	# cleanup the tmp folder
	rm -rf ${TMP_UPDATE_DIR}
}

# have the update depend on the compress tar it wants to use
IMAGE_TYPEDEP_update = "tar${UPDATE_ROOTFS_COMPRESSION}"

	

# The following is an sdcard generation. This depends on the image_types_fsl.bbclass
# We override the sdcard generation command to make a custom SD card layout.
IMAGE_CLASSES += "image_types_fsl"

generate_gamut_sdcard () {
	# Create partition table
	echo "in gamut_sdcard"
	parted -s ${SDCARD} mklabel msdos
	parted -s ${SDCARD} unit KiB mkpart primary $(expr  ${IMAGE_ROOTFS_ALIGNMENT} \+ ${BOOT_SPACE_ALIGNED}) $(expr ${IMAGE_ROOTFS_ALIGNMENT} \+ ${BOOT_SPACE_ALIGNED} \+ $ROOTFS_SIZE)
	parted ${SDCARD} print
	echo "after parted"
	# Burn bootloader
	case "${IMAGE_BOOTLOADER}" in
		imx-bootlets)
		bberror "The imx-bootlets is not supported for i.MX based machines"
		exit 1
		;;
		u-boot)
		if [ -n "${SPL_BINARY}" ]; then
			dd if=${DEPLOY_DIR_IMAGE}/${SPL_BINARY} of=${SDCARD} conv=notrunc seek=2 bs=512
			dd if=${DEPLOY_DIR_IMAGE}/u-boot-${MACHINE}.${UBOOT_SUFFIX_SDCARD} of=${SDCARD} conv=notrunc seek=69 bs=1K
		else
			dd if=${DEPLOY_DIR_IMAGE}/u-boot-${MACHINE}.${UBOOT_SUFFIX_SDCARD} of=${SDCARD} conv=notrunc seek=2 bs=512
		fi
		;;
		*)
		bberror "Unknown IMAGE_BOOTLOADER value"
		exit 1
		;;
	esac
	echo "after after burn uboot"
	
	# Burn Partition
	dd if=${SDCARD_ROOTFS} of=${SDCARD} conv=notrunc,fsync seek=1 bs=$(expr ${BOOT_SPACE_ALIGNED} \* 1024 + ${IMAGE_ROOTFS_ALIGNMENT} \* 1024)
	echo "after after ext4 burn"
}

# override the image_types_fsl so we use the gamut sd card generation
SDCARD_GENERATION_COMMAND_mx6 = "generate_gamut_sdcard"
# No boot partition on gamut, but this can't be zero. Just eat the 4MiB
BOOT_SPACE = "4096"