#!/usr/bin/env python
# This script goes into each submodule in this folder and adds the upstream
# repo as a remote with the name 'upstream'. This is useful for fetching
# changes that upstream makes, vetting them, then pushing to monster.

from subprocess import call
import os

repos = [ 
['meta-fsl-arm','https://git.yoctoproject.org/git/meta-fsl-arm'] ,
['meta-openembedded','https://github.com/openembedded/meta-openembedded'],
['poky','https://git.yoctoproject.org/git/poky'],
]

for tmp in repos:
    folder = tmp[0]
    remote = tmp[1]
    cmd = 'cd ' + folder + '; git remote add upstream ' + remote
    print 'adding remote for ' + folder
    call(cmd,shell=True)
