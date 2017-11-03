#!/bin/sh
# Copyright (C) 2017, iZotope, Inc. All Rights Reserved
# Released under the BSD-3 license (see COPYING.BSD-3)

DAEMON=/usr/sbin/imx-pwrkey
NAME=imx-pwrkey
DEC="IMX power daemon"
ARGS="/dev/input/by-path/platform-20cc000.snvs-pwrkey-event"

test -f $DAEMON || exit 0

set -e

case "$1" in
    start)
	echo -n "Starting $DESC: "
	start-stop-daemon -S -x $DAEMON -- $ARGS
	echo "$NAME."
	;;
    stop)
	echo -n "Stopping $DESC: "
	start-stop-daemon -K --oknodo -x $DAEMON
	echo "$NAME."
	;;
    restart)
	$0 stop
	$0 start
	;;
    reload)
	echo -n "Reloading $DESC: "
	killall -HUP ${NAME}
	echo "$NAME."
	;;
    status)
	status $DAEMON
	exit $?
	;;
    *)
	echo "Usage: $0 {start|stop|restart|reload|status}"
	exit 1
	;;
esac

exit 0
