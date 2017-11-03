#!/bin/sh
# Copyright (C) 2017, iZotope, Inc. All Rights Reserved
# Released under the BSD-3 license (see COPYING.BSD-3)

echo "Setting WiFi to AP mode..."

/etc/init.d/connman stop
/etc/init.d/hostapd stop
/etc/init.d/udhcpd stop

count=0
while pgrep -x '(connmand|hostapd|udhcpd)' > /dev/null; do
		echo "waiting on kill $count"
		sleep .2
		if [[ "$count" -gt 50 ]]; then
				echo "Error: Timed out waiting on processes termination."
				exit 1
		fi
		(( count += 1 ))
done

rfkill unblock all

/etc/init.d/hostapd start
ifconfig wlan0 192.168.0.1
/etc/init.d/udhcpd start

# set the txpower if the file exists
power_file="/etc/wifi_power.ap"
if [[ -e "$power_file" ]]; then
	re='^[0-9]+$'
	power=`cat $power_file`
	if [[ "$power" =~ $re ]]; then
		echo "Setting wifi power to $power mBm"
		iw wlan0 set txpower limit $power
	else
		echo "Invalid power in $power_file"
	fi
fi

echo "Set WiFi to AP mode."