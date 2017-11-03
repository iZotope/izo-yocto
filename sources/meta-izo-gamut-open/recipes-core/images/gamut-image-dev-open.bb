require gamut-image.inc
IMAGE_INSTALL += "${pkg_all} ${pkg_dev}"

inherit izo_buildinfo

DESCRIPTION = "A bootable system will all production gamut options and software and also \
is suitable for development work."

# add dev-pkgs ans ssh-server
IMAGE_FEATURES += "dev-pkgs ssh-server-openssh"
# Add package managment
IMAGE_FEATURES += "package-management"
# add debuging support
IMAGE_FEATURES += "empty-root-password tools-profile"
# Generate the correct type of debug info
PACKAGE_DEBUG_SPLIT_STYLE = 'debug-file-directory'

