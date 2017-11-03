#!/bin/sh
# Copyright (C) 2017, iZotope, Inc. All Rights Reserved
# Released under the BSD-3 license (see COPYING.BSD-3)

BASEDIR=/data/nonvolatile

echo "Generating file system hierarchy for nonvolatile in '$BASEDIR'..."

if ! mountpoint -q $BASEDIR; then
	echo "$BASEDIR not mounted. Aborting nonvolatile hierarchy generation."
	exit 1
fi

mkdir -p $BASEDIR/bin
mkdir -p $BASEDIR/boot
mkdir -p $BASEDIR/dev
mkdir -p $BASEDIR/etc
mkdir -p $BASEDIR/etc/opt
mkdir -p $BASEDIR/home
mkdir -p $BASEDIR/lib
mkdir -p $BASEDIR/media
mkdir -p $BASEDIR/mnt
mkdir -p $BASEDIR/opt
mkdir -p $BASEDIR/proc
mkdir -p $BASEDIR/root
mkdir -p $BASEDIR/run
mkdir -p $BASEDIR/sbin
mkdir -p $BASEDIR/srv
mkdir -p $BASEDIR/tmp
mkdir -p $BASEDIR/usr
mkdir -p $BASEDIR/usr/bin
mkdir -p $BASEDIR/usr/include
mkdir -p $BASEDIR/usr/lib
mkdir -p $BASEDIR/usr/sbin
mkdir -p $BASEDIR/usr/share
mkdir -p $BASEDIR/var
mkdir -p $BASEDIR/var/cache
mkdir -p $BASEDIR/var/lib
mkdir -p $BASEDIR/var/lock
mkdir -p $BASEDIR/var/log
mkdir -p $BASEDIR/var/mail
mkdir -p $BASEDIR/var/opt
mkdir -p $BASEDIR/var/run
mkdir -p $BASEDIR/var/spool
mkdir -p $BASEDIR/var/spool/mail
mkdir -p $BASEDIR/var/tmp