#!/usr/bin/python
# Copyright (C) 2017, iZotope, Inc. All Rights Reserved
# Released under the BSD-3 license (see COPYING.BSD-3)

# Reads in a CSV file of gpios info and print out a json

from __future__ import print_function
import csv
import sys
import json
from collections import OrderedDict

def eprint(row, msg):
	"""print an error to stderr"""
	rowstr = '{}, {}, {}, {}, {}'.format(row['num'], row['name'], row['group'], row['polarity'], row['export'])
	print("row '{}': {}".format(rowstr, msg), file=sys.stderr)

def parse_csv():
	with open('gpios.csv') as csvfile:
		reader = list(csv.DictReader(filter(lambda row: row[0] != '#', csvfile), skipinitialspace=True))

		gpio_json = []
		for row in reader:
			#strip all fields
			for key in row.keys():
				row[key].strip()

			#parse the number
			num = row['num']
			try:
				num = int(num)
			except ValueError:
				eprint(row, "couldn't parse value {} as a nubmer.".format(row['num']))
				sys.exit(1)

			#parse the name
			name = row['name']
			if not name:
				eprint(row, 'name must not be empty.')
				sys.exit(1)

			# parse the group
			group = row['group']

			# parse polarity
			if row['polarity'] not in ('active_high', 'active_low'):
				eprint('polarity must be active_high or active_low')
				sys.exit(1)
			polarity = row['polarity']

			# parse the export
			export = row['export']
			if export not in ('export',''):
				eprint(row, "export must be 'export' or ''")
				sys.exit(1)

			# use OrderedDict to preserve a logical order
			this_gpio = OrderedDict([
				('name', name),
				('num', num),
				('group', group),
				('polarity', polarity),
				('export', export),
			])
			gpio_json.append(this_gpio)
		print(json.dumps(gpio_json, indent=4))

parse_csv()
