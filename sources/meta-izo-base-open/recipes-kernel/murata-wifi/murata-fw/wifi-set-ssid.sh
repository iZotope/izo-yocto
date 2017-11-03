#!/bin/sh
# Copyright (C) 2017, iZotope, Inc. All Rights Reserved
# Released under the BSD-3 license (see COPYING.BSD-3)

serial_file=/serial/manufacturing_data.json
MAC_ADDRESS=$(cat /sys/class/net/wlan0/address)
MAC_ADDRESS=${MAC_ADDRESS//\:/}
HOSTAPD_COPY_DIR=/tmp/etc

p() {
	echo "wifi-set-ssid: ${1}"
}

# Returns the last six characters of a string, or the whole
# string if the string is less than six characters
last_six_of_string() {
	str=$1
	echo ${str:${#str}<6?0:-6}
}

set_ssid() {
	p "Setting WIFI SSID to $1"
	if ! sed -i s/ssid=test/ssid=${1}/g $HOSTAPD_COPY_DIR/hostapd.conf; then
		return 1
	fi

	mkdir -p $HOSTAPD_COPY_DIR
	cp $HOSTAPD_COPY_DIR/hostapd.conf $HOSTAPD_COPY_DIR/hostapd.conf.bak
	return 0
}

# copy the file over to the nonvolatile if this hasn't happened yet
if [ ! -e $HOSTAPD_COPY_DIR/hostapd.conf ]; then
	mkdir -p $HOSTAPD_COPY_DIR
	cp /etc/hostapd.conf $HOSTAPD_COPY_DIR/hostapd.conf
fi

if [ ! -e $HOSTAPD_COPY_DIR/hostapd.conf.bak ]; then

	mac_last_six=$(last_six_of_string $MAC_ADDRESS)

	if [ ! -e $serial_file ]; then
		p "Could not find serial file $serial_file. Using MAC address."
		if ! set_ssid "SpireStudio-MAC${mac_last_six}"; then
			exit 1
		fi
	fi

	serial_number=$(jq -r .serial_unit $serial_file)
	if [[ "$serial_number" == "null" ]]; then
		p "Could not retrieve serial number from $serial_file. Using MAC address."
		if ! set_ssid "SpireStudio-MAC${mac_last_six}"; then
			exit 1
		fi
	fi
	
	serial_last_six=$(last_six_of_string $serial_number)
	if ! set_ssid "SpireStudio-${serial_last_six}"; then
		exit 1
	fi
fi
