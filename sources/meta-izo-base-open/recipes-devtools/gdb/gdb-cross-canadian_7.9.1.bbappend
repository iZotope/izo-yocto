EXTRA_OECONF_append = " --without-system-readline"

FILESEXTRAPATHS_prepend := "${THISDIR}:"
SRC_URI += "file://0050-adjust-gdb-demangler-api.patch \
	    file://0050-demangler-crash-on-casts-in-template-parameters.patch"

