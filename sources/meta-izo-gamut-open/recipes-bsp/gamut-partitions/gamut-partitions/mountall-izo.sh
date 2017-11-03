#!/bin/bash
# Copyright (C) 2017, iZotope, Inc. All Rights Reserved
# Released under the BSD-3 license (see COPYING.BSD-3)

DIR=/etc/izomount.d/

echo "Mounting all iZotope partitions..."
if [[ -d "$DIR" ]]; then
	for file in $DIR/*; do
		echo "   processing '$file'"
		while read line; do
			echo -n "      '$line... '"
			output=$(gamut-mount mount -l $line)
			retval=$?
			if [[ $retval ]]; then
				echo "Failed!"
				echo "$output"
				echo
			else
				echo "Okay!"
			fi
		done <$file
	done
fi
