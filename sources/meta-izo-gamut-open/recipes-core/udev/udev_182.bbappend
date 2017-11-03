# Add a patch to remove udev caching which can throw off device numbers
# which we depend on being static
FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
