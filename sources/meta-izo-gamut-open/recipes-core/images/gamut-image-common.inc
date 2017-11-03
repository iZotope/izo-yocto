# this file holds things that are common to all gamut images

#### enable an auto-login session for root on serial
ROOTFS_POSTPROCESS_COMMAND += "root_autologin_over_serial; "
root_autologin_over_serial () {
    sed -i 's/^mxc0:.*/mxc0:12345:respawn:\/bin\/login -f root/' ${IMAGE_ROOTFS}${sysconfdir}/inittab
}

# a python function to generate a prompt prefix from the image name
def get_prefix(mystr):
    prefix = 'gamut-image-'
    if mystr.startswith(prefix):
        mystr = mystr[len(prefix):]
        return mystr.upper() + ' '
    return ''

### Set the bash prompt addition based on the image name
python () {
    image_name = d.getVar('PN', True)
    prompt_addtion = get_prefix(image_name)
    d.setVar('PROMPT_ADDITION', prompt_addtion)
}

### Add a note to the prompt to let the user know what image they are in
ROOTFS_POSTPROCESS_COMMAND += "set_prompt_prefix; "
set_prompt_prefix() {
    echo "PS1=\"${PROMPT_ADDITION}\$PS1\"" >> ${IMAGE_ROOTFS}/etc/profile
    echo "export PS1" >> ${IMAGE_ROOTFS}/etc/profile
}
