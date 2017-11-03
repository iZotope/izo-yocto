#!/bin/sh
# Copyright (C) 2017, iZotope, Inc. All Rights Reserved
# Released under the BSD-3 license (see COPYING.BSD-3)

if [[ -f /serial/in_manufacturing_mode ]]; then
	echo "Writing current build info into manufacturing data"
	build_info=$(cat /etc/build)
	sanitized_build_info="${build_info//\"/\'}"
	serial-write.sh "factory_build_info" "$sanitized_build_info"
fi
