inherit core-image
# tells Yocto that this is an image recipe
# pulls default logic to create a bootable root filesystem
# package installation, filesystem generation, compression

CORE_IMAGE_EXTRA_INSTALL += "aesd-assignments openssh scull misc-modules"
CORE_IMAGE_EXTRA_INSTALL += "openssh"

inherit extrausers
# lets you add users, set passwords, modify user permissions

# See https://docs.yoctoproject.org/singleindex.html#extrausers-bbclass
# We set a default password of root to match our busybox instance setup
# Don't do this in a production image
# PASSWD below is set to the output of
# printf "%q" $(mkpasswd -m sha256crypt root) to hash the "root" password
# string

PASSWD = "\$5\$2WoxjAdaC2\$l4aj6Is.EWkD72Vt.byhM5qRtF9HcCM/5YpbxpmvNB5"
EXTRA_USERS_PARAMS = "usermod -p '${PASSWD}' root;"  



