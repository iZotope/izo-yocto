# iZotope's Yocto build environment.
## Build instruction
This build process has only been tested on Ubuntu 16.04LTS.

```bash
sudo apt-get install gawk wget git-core diffstat unzip texinfo build-essential chrpath libsdl1.2-dev xterm curl
git clone https://github.com/iZotope/izo-yocto.git
cd izo-yocto
. ./setup-environment build
bitbake gamut-image-prd-open
```
Go grab a cup of coffee (or ten)...

The output will be located in `build/tmp/deploy/images/gamut-r1`
