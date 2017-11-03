#!/bin/sh
# Copyright (C) 2017, iZotope, Inc. All Rights Reserved
# Released under the BSD-3 license (see COPYING.BSD-3)

LOGPATH=/data/nonvolatile/var/log/UIBoardFirmwareUpdater
mkdir -p $LOGPATH

echo "Checking UI board firmware..."
/usr/bin/UIBoardFirmwareUpdater |& svlogd -tt $LOGPATH
result=${PIPESTATUS[0]}

if [ $result -eq 0 ]; then
	echo "UI board update successful"
else
	echo "UI board update failed. Check the log file for details. Doing a recovery reboot."
	/usr/sbin/gamut-boot recovery-reboot
fi
