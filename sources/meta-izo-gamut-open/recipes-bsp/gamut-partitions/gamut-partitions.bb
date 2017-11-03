DESCRIPTION = "Gamut partition setup and auto mounting"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://COPYING.BSD-3;md5=58dcd8452651fc8b07d1f65ce07ca8af"

# don't forget to update if you change the script!
PR = "r1"

S = "${WORKDIR}"

PACKAGE_ARCH = "${MACHINE_ARCH}"
COMPATIBLE_MACHINE = "(gamut-r1)"

SRC_URI = " \
    file://COPYING.BSD-3 \
    file://gamut-format-partition  \
    file://gamut-mount \
    file://partition_config.json  \
    file://partition-common.source  \
    file://mountall-izo.sh \
"

PACKAGES = "${PN}-common"

RDEPENDS_${PN}-common += "bash"

FILES_${PN}-common = " \
    /data \
    /serial \
    /recovery \
    ${sbindir} \
    ${sysconfdir}/partition_config.json \
    ${sysconfdir}/init.d \
    ${sysconfdir}/rcS.d \
"

do_install () {
    # install the mounter and partition configuration
    install -d ${D}/${sbindir}
    install -d ${D}/${sysconfdir}
    install -m 774 ${WORKDIR}/gamut-format-partition ${D}/${sbindir}
    install -m 744 ${WORKDIR}/partition-common.source ${D}/${sbindir}
    install -m 774 ${WORKDIR}/gamut-mount ${D}/${sbindir}
    install -m 744 ${WORKDIR}/partition_config.json ${D}/${sysconfdir}/

    # init script for mounting all
    install -d ${D}${sysconfdir}/init.d/
    install -d ${D}${sysconfdir}/rcS.d/
    install -m 0755 ${WORKDIR}/mountall-izo.sh ${D}${sysconfdir}/init.d
    ln -sf ../init.d/mountall-izo.sh ${D}${sysconfdir}/rcS.d/S03mountall-izo

    # create mount point for the data
    # TODO: dynamically generate mountpoint creation as we do mounting itself
    install -d -m 755 ${D}/data/Firmware
    install -d -m 755 ${D}/data/nonvolatile
    install -d -m 755 ${D}/data/Gamut
    install -d -m 755 ${D}/serial
    install -d -m 755 ${D}/recovery
}

# normal gamut run time partitions to mount
GAMUT_PARTITIONS[normal] = "\
    serial \
    firmware \
    system \
    data \
    rcv \
"

# Note: don't mount data in recovery mode
GAMUT_PARTITIONS[recovery] = "\
    serial \
    firmware \
    system \
"

### TODO:
# for future products the common code in this recipe should be broken out
# into either a generic recipe or a bblcass. Likely a generic recipe in the
# meta-izo-base-open layer.


### This generates all the packages based on the GAMUT_PARTITONS variable
# Each configuration is a variable flag with its value being a list of
# partitions to auto mount on that architecture. e.g.
#   GAMUT_PARTITIONS[normal] = "part1 part2"
# would make a package called gamut-partitions-normal that would auto mount part1 and part 2
# if you want to install the partition tools without any auto mount just install 
# ${PN}-common package only
python () {
    PART_VAR = 'GAMUT_PARTITIONS'
    MOUNT_DIR = '${sysconfdir}/izomount.d'
    packages = d.getVarFlags(PART_VAR)
    if packages is None:
        return

    # add the packages to the list
    for package in packages:
        package_name = '{}-{}'.format(d.getVar('PN', True), package)
        d.appendVar('PACKAGES', ' ' + package_name)

        # append the do install to generate the files
        install_cmds = []
        install_cmds.append('') # empty newline
        install_cmds.append("    # setup parts for package {}".format(package))
        install_cmds.append("    install -d ${D}${sysconfdir}/izomount.d")
        for part in d.getVarFlag(PART_VAR, package).split():
            cmd = '    echo {} >> ${{D}}{}/mount.{}'.format(part, MOUNT_DIR, package)
            install_cmds.append(cmd)
        # newline at end
        install_cmds.append('')
        d.appendVar('do_install', '\n'.join(install_cmds))

        # set the run time dependency on the ${PN}-common package
        d.appendVar('RDEPENDS_{}'.format(package_name), ' {}-common'.format(d.getVar('PN', True)))

        # set the files for this package
        d.setVar('FILES_{}'.format(package_name), '{}/mount.{}'.format(MOUNT_DIR, package))
}
