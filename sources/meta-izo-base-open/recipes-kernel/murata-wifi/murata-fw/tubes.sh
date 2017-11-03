#!/bin/sh
# Copyright (C) 2017, iZotope, Inc. All Rights Reserved
# Released under the BSD-3 license (see COPYING.BSD-3)

NETWORK_NAME=$(connmanctl services | grep -i " $1 " | tr '   ' '\n' | tail -n 1)
echo $NETWORK_NAME
if [ $NETWORK_NAME ]; then
     connmanctl connect $NETWORK_NAME
else
    echo "Network $1 not found"
fi

