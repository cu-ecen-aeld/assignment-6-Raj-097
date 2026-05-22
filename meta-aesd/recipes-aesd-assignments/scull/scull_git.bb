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

SRC_URI = "git://github.com/cu-ecen-aeld/Building_Example_Driver_Modules.git;protocol=ssh;branch=main"

# Modify these as desired
PV = "1.0+git${SRCPV}"
SRCREV = "f8e32fe2a21e8663f27501f6dd75b5c048fe7eea"

S = "${WORKDIR}/git"

#If your recipe inherits module or uses EXTRA_OEMAKE, Yocto automatically runs oe_runmake

inherit module update-rc.d

INITSCRIPT_NAME = "scull"
INITSCRIPT_PARAMS = "defaults 99"

SRC_URI += "file://scull-init.sh"
# copies from recipe's files/ directory into WORKDIR

# Building the scull module
EXTRA_OEMAKE:append:task-install = " -C ${STAGING_KERNEL_DIR} M=${S}/scull"
EXTRA_OEMAKE += "KERNELDIR=${STAGING_KERNEL_DIR}"

FILES:${PN} += "${sysconfdir}/init.d/scull"

do_install() {
    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/scull-init.sh ${D}${sysconfdir}/init.d/scull
#install -m 0755 /path/to/workdir/scull-init.sh /path/to/tmp/work/rootfs/etc/init.d/scull

    install -d ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/
    install -m 0755 ${S}/scull/*.ko ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/

}

