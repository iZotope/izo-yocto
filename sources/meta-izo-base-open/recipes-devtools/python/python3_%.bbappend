# we use subprocess on Gamut, but it doesn't include all it's dependencies so some end up in python3-misc
# that package is a catch all which is too big, so we're adding thim into subprocess here
FILES_${PN}-subprocess+=" \
	${libdir}/python3.4/lib-dynload/_posixsubprocess.cpython-34m.so \
	${libdir}/python3.4/selectors.py \
	${libdir}/python3.4/operator.py \
	"
