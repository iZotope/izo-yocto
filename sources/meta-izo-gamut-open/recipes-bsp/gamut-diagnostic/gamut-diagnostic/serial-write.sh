#!/bin/sh
# Copyright (C) 2017, iZotope, Inc. All Rights Reserved
# Released under the BSD-3 license (see COPYING.BSD-3)

usage() {
	echo ""
	echo "serial-write.sh <key> <value>"
	echo "key: Double-quote escaped key."
	echo "value: Double-quote escaped value under <key>. Will overwrite any existing value at <key>."
}

if [[ $# -lt 2 ]]; then
	usage
	exit 1
fi;

partition_config=/etc/partition_config.json
serial_file=/serial/manufacturing_data.json
key=\"$1\"
value=\"$2\"

if ! gamut-mount remount-rw -l serial; then
	echo "Could not mount the serial partition read-write"
	exit 1
fi

if [[ ! -e $serial_file ]]; then
	echo "Serial file did not exist. Creating it."
	echo "{}" > $serial_file
fi

if ! jq "." $serial_file >> /dev/null; then
	echo "Serial file corrupted. Re-creating it."
	echo "{}" > $serial_file
fi

jq ". + {$key:$value}" $serial_file > $serial_file.tmp
cat $serial_file.tmp > $serial_file
rm -f $serial_file.tmp

gamut-mount remount-ro -l serial