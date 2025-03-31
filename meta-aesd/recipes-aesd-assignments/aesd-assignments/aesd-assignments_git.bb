# See https://git.yoctoproject.org/poky/tree/meta/files/common-licenses
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

# TODO: Set this with the path to your assignments repo. Use ssh protocol and see lecture notes
# about how to setup ssh-agent for passwordless access
# Git repository and commit hash
SRC_URI = "git://github.com/cu-ecen-aeld/assignments-3-and-later-Raj-097.git;protocol=https;branch=main"

PV = "1.0+git${SRCPV}"
# SRCPV expands to a short hash from SRCREV (aesd-assignments-1.0+git1f993ece38)

# TODO: set to reference a specific commit hash in your assignment repo
SRCREV = "227a998d1476e4c3e63912587d52069306be15c9"

# This sets your staging directory based on WORKDIR, where WORKDIR is defined at 
# https://docs.yoctoproject.org/ref-manual/variables.html?highlight=workdir#term-WORKDIR
# We reference the "server" directory here to build from the "server" directory
# in your assignments repo

S = "${WORKDIR}/git/server"  
# Yocto's working directory for this recipe

# TODO: Add the aesdsocket application and any other files you need to install
# See https://git.yoctoproject.org/poky/plain/meta/conf/bitbake.conf?h=kirkstone
# Files in the target that u wanna use for the image
FILES:${PN} += "${bindir}/aesdsocket ${sysconfdir}/init.d/aesdsocket-start-stop.sh" 

# TODO: customize these as necessary for any libraries you need for your application
TARGET_LDFLAGS += "-pthread -lrt"
# Dependencies (Build-Time)
DEPENDS += "libgcc glibc"

# Dependencies (Runtime)
RDEPENDS:${PN} += "libgcc glibc"

# Inherit Yocto build system support
inherit pkgconfig

do_configure () {
    :     
}
# : is a no-op, yocto will skip this step

do_compile () {
    cd ${S}
    oe_runmake
}

do_install () {
    # TODO: Install your binaries/scripts here.
    # Be sure to install the target directory with install -d first
    # Yocto variables ${D} and ${S} are useful here, which you can read about at 
    # https://docs.yoctoproject.org/ref-manual/variables.html?highlight=workdir#term-D
    # and
    # https://docs.yoctoproject.org/ref-manual/variables.html?highlight=workdir#term-S
    # See example at https://github.com/cu-ecen-aeld/ecen5013-yocto/blob/ecen5013-hello-world/meta-ecen5013/recipes-ecen5013/ecen5013-hello-world/ecen5013-hello-world_git.bb
    install -d ${D}${bindir}
    install -m 0755 ${S}/aesdsocket ${D}${bindir}

    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${S}/aesdsocket-start-stop.sh ${D}${sysconfdir}/init.d/S99aesdsocket
    
    install -d ${D}${sysconfdir}/rcS.d
    ln -sf ${sysconfdir}/init.d/S99aesdsocket ${D}${sysconfdir}/rcS.d/S99aesdsocket
    
    install -d ${D}${sysconfdir}/rc0.d
    ln -sf ${sysconfdir}/init.d/S99aesdsocket ${D}${sysconfdir}/rc0.d/K99aesdsocket
    
}

