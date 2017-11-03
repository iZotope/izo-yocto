#/bin/sh

hostapd_running="$(pgrep -x hostapd)"
udhcpd_running="$(pgrep -x udhcpd)"
connman_running="$(pgrep -x connmand)"
ipaddr="$(/sbin/ifconfig wlan0 | grep 'inet addr:' | cut -d: -f2 | awk '{ print $1}')"

#echo hostapd $hostapd_running
#echo udhcpd $udhcpd_running
#echo connman $connman_running
#echo ip $ipaddr
#echo

if [[ \
		-n "$hostapd_running" && \
		-n "$udhcpd running" && \
		-z "$connman_running" && \
		"$ipaddr" == "192.168.0.1" \
		]]; then
		echo "ap"
		exit
fi

if [[ \
		-z "$hostapd_running" && \
		-z "$udhcpd_running" && \
		-n "$connman_running" \
		]]; then
		echo "wifi"
		exit
fi

echo "unconfigured"