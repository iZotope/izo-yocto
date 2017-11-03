#!/bin/sh
# Copyright (C) 2017, iZotope, Inc. All Rights Reserved
# Released under the BSD-3 license (see COPYING.BSD-3)

if ! fsck-partition -l data; then
	# Set an error
	echo "Gamut data partition corrupted. Uh oh!"
fi

if ! fsck-partition -l firmware; then
	echo "Firmware partition corrupted. Reformatting it."
	gamut-format-partition -l firmware
fi

if ! fsck-partition -l system; then
	echo "System partition corrupted. Reformatting it."
	gamut-format-partition -l system
fi