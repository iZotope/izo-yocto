# This will split out start-stop-daemon into it's own package so we can install that without everything else

PR .= ".0"

# Note, must prepend so we steal this from any other package
PACKAGES =+ "${PN}-start-stop-daemon"

FILES_${PN}-start-stop-daemon = "${sbindir}/start-stop-daemon"

RDEPENDS_${PN} += "${PN}-start-stop-daemon"
