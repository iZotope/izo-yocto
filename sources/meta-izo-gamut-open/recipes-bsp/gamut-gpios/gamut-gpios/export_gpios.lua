#!/usr/bin/lua
-- Copyright (C) 2017, iZotope, Inc. All Rights Reserved
-- Released under the BSD-3 license (see COPYING.BSD-3)


local posix = require 'posix'
posix.libgen = require 'posix.libgen'
posix.unistd = require 'posix.unistd'
posix.sys = {}
posix.sys.stat = require 'posix.sys.stat'

local izo = require 'izo'
local json = require 'izo.json'

local program_name = "export_gpios: "

local function export_gpio(gpio)
	-- only try to export if it isn't already
	local stat = posix.sys.stat.stat('/sys/class/gpio/gpio'..tostring(gpio.num))
	if not (stat and posix.sys.stat.S_ISDIR(stat.st_mode) ~= 0) then
		local f, err = io.open('/sys/class/gpio/export','w')
		if not f then
			return nil, string.format("Couldn't export gpio '%d': %s", 1000, err)
		end
		f:write(tostring(math.floor(gpio.num)))
		local success, err, errno = f:close()
		if not success then
			if errno == 16 then
				-- EBUSY: GPIO was already exported or claimed by kernel, no problem, push on
				return nil, string.format("Warning: gpio %d '%s' already exported or owned by kernel.",gpio.num, gpio.name)
			elseif errno == 22 then
				-- EINVAL: a non existent GPIO number or malformed gpio was passed in
				return nil, string.format("tried to export invalid gpio: gpio %d '%s'", gpio.num,  gpio.name)
			else
				-- unknown error
				return nil, string.format("Unknown error exporting gpio %d '%s': %d %s", gpio.num,  gpio.name, errno, err)
			end
		end
	end

	-- make sure /dev/gpio exisits and link to the sysfs entry
	posix.mkdir('/dev/gpio')
	posix.unistd.link(string.format('/sys/class/gpio/gpio%d/', gpio.num), '/dev/gpio/'..gpio.name, true)

	-- set the polarity
	local active_low = (gpio.polarity == 'active_low') and 1 or 0
	f = io.open(string.format('/dev/gpio/%s/active_low', gpio.name), 'w')
	f:write(tostring(active_low))

	-- set permissions on the files for the group if it exists
	if gpio.group then
		local files = {'direction','edge','value'}
		for i, v in ipairs(files) do
			files[i] = string.format('/dev/gpio/%s/%s', gpio.name, files[i])
		end

		for _,file in pairs(files) do
			posix.unistd.chown(file, -1, gpio.group)
			posix.chmod(file, "rw-rw-r--")
		end
	end

	return true
end

print(program_name .. "Exporting GPIO pins for use in userspace...")

-- try to read in the gpios json file
gpios, err = json.decodefile('/etc/gpios.json')
if not gpios then
	print('failed to parse /etc/gpios.json: '..err)
	sys.exit(1)
end

-- now export the gpios
local had_error = false
for _,gpio in ipairs(gpios) do
	if gpio.export == 'export' then
		success, err = export_gpio(gpio)
		if not success then
			print(err)
			had_error = true
		end
	end
end

if had_error then
	os.exit(1)
end
