# Recipe created by recipetool
# This is the basis of a recipe and may need further editing in order to be fully functional.
# (Feel free to remove these comments when editing.)

# WARNING: the following LICENSE and LIC_FILES_CHKSUM values are best guesses - it is
# your responsibility to verify that the values are complete and correct.
#
# The following license files were not able to be identified and are
# represented as "Unknown" below, you will need to check them yourself:
#   LICENSE
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "git://git@github.com/cu-ecen-aeld/assignment-7-Raj-097.git;protocol=ssh;branch=main \
           file://0001-Modified-ldd3-makefile.patch \
           "

# Modify these as desired
PV = "1.0+git${SRCPV}"
SRCREV = "6f550e09db79eb270b54794fee666785b7cd2369"

S = "${WORKDIR}/git"

inherit module update-rc.d
#Kernel modules built using the yocto build system (extra_oemake)
#installed to /lib/modules/$(uname -r)/extra/ in root fs
# update-rc.d helps install and enable an init script

INITSCRIPT_NAME = "misc-modules"
INITSCRIPT_PARAMS = "defaults 99"

SRC_URI += "file://misc-modules-init.sh"

# Building the misc modules
EXTRA_OEMAKE:append:task-install = " -C ${STAGING_KERNEL_DIR} M=${S}/misc-modules"
EXTRA_OEMAKE += "KERNELDIR=${STAGING_KERNEL_DIR}"

FILES:${PN} += "${sysconfdir}/init.d/misc-modules"
FILES:${PN} += "${nonarch_base_libdir}/modules/*.ko"

do_install() {
    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/misc-modules-init.sh ${D}${sysconfdir}/init.d/misc-modules
    
    install -d ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/
    install -m 0755 ${S}/misc-modules/*.ko ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/

    
}
#B refers to the build directory where the recipe's compiled output (like .ko files) is stored

