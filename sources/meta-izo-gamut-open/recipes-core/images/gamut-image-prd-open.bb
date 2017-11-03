require gamut-image.inc
inherit izo_buildinfo

IMAGE_INSTALL += "${pkg_all} ${pkg_prd}"

GAMUT_IMAGE_NO_ROOT_PASSWORD = "empty-root-password allow-empty-password"

IMAGE_FEATURES += "ssh-server-openssh ${GAMUT_IMAGE_NO_ROOT_PASSWORD}"
