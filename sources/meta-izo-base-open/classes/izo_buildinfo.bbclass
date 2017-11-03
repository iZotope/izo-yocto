# izo_buildinfo.bbclass
# Copyright (C) 2017, iZotope, Inc. All Rights Reserved
# Released under the BSD-3 license (see COPYING.BSD-3)
#
# This bbcalss is to be inherited by an image recipe.
# It will take various build information and put it into /etc/build_izo of the rootfs
# To use, add "INHERIT += "izo_buildinfo" to your image recipe and set env vars
# as you desire to add to /etc/build_izo
#
# The format of the /etc/build_izo file follows that of the poky image-buildinfo.bbclass
#   "KEY = VALUE"
#
# There are some influential variables for this package:
# IZO_BUILDINFO_FW_VERSION - A string containing the version of the firmware release
# IZO_BUILDINFO_APPEND_FILES - A list of files to append to the /etc/build_izo file
# IZO_BUILDINFO_FILE - a path relative to root for the build info to go into. "/etc/build_izo" by default

IZO_BUILDINFO_FILE ??= "/etc/build_izo"

ROOTFS_POSTPROCESS_COMMAND += "do_izo_buildinfo; "

BUILD_FILE = "${IMAGE_ROOTFS}${IZO_BUILDINFO_FILE}"

python do_izo_buildinfo() {
    with open(d.getVar('BUILD_FILE', True), 'w+') as build_file:
        # Write the image name
        build_file.writelines(('IMAGE = ', d.getVar('PN', True),'\n'))

        # write the firmware version
        build_file.writelines(('GAMUT_FW_REV = ', d.getVar('IZO_BUILDINFO_FW_VERSION', True),'\n'))

        # Add in the files from the append variable
        bbpath = d.getVar('BBPATH', True)
        append_tables = d.getVar('IZO_BUILDINFO_APPEND_FILES', True)
        if append_tables:
            for append_file in append_tables.split():
                file_path = "%s" % bb.utils.which(bbpath, append_file)
                if not file_path:
                    bb.warn("izo_buildinfo: Couldn't find file '{}' to include in izo_buildinfo file".format(append_file))
                    continue
                # TODO: try/catch and raise BB error if missing
                with open(file_path) as content_file:
                    content = content_file.read()
                    build_file.write(content)
                    build_file.write('\n')
}