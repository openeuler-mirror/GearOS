#require libcap.inc
#require ${OPEN_SRC_DIR}/${BPN}/series_yocto.conf
SUMMARY = "Library for getting/setting POSIX.1e capabilities"
HOMEPAGE = "http://sites.google.com/site/fullycapable/"

# no specific GPL version required
LICENSE = "BSD | GPLv2"

LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/GPL-2.0-only;md5=801f80980d171dd6425610833a22dbe6"

SRC_URI = "file://libcap-2.32.tar.gz \
	   file://libcap-buildflags.patch \
	   file://Avoid-segfaulting-when-the-kernel-is-ahead-of-libcap.patch \
	  "

DEPENDS = "hostperl-runtime-native gperf-native"


inherit lib_package

# do NOT pass target cflags to host compilations
#
do_configure() {
        # libcap uses := for compilers, fortunately, it gives us a hint
        # on what should be replaced with ?=
        sed -e 's,:=,?=,g' -i Make.Rules
        sed -e 's,^BUILD_CFLAGS ?= $(.*CFLAGS),BUILD_CFLAGS := $(BUILD_CFLAGS),' -i Make.Rules
}

PACKAGECONFIG ??= "${@bb.utils.filter('DISTRO_FEATURES', 'pam', d)}"
PACKAGECONFIG_class-native ??= ""

PACKAGECONFIG[pam] = "PAM_CAP=yes,PAM_CAP=no,libpam"


EXTRA_OEMAKE = " \
  INDENT=  \
  lib='${baselib}' \
  RAISE_SETFCAP=no \
  DYNAMIC=yes \
  BUILD_GPERF=yes \
"
INSANE_SKIP += "installed-vs-shipped"

#EXTRA_OEMAKE_append_class-target = " SYSTEM_HEADERS=${STAGING_INCDIR}"

# these are present in the libcap defaults, so include in our CFLAGS too
CFLAGS += "-D_LARGEFILE64_SOURCE -D_FILE_OFFSET_BITS=64"

do_compile() {
        oe_runmake ${PACKAGECONFIG_CONFARGS}
}

do_install() {
	rm ${D}${libdir}/security/pam_cap.so -rf
        oe_runmake install \
                ${PACKAGECONFIG_CONFARGS} \
                DESTDIR="${D}" \
                prefix="${prefix}" \
                SBINDIR="${sbindir}"
}

do_install_append() {
        # Move the library to base_libdir
        install -d ${D}${base_libdir}
        if [ ! ${D}${libdir} -ef ${D}${base_libdir} ]; then
                mv ${D}${libdir}/libcap* ${D}${base_libdir}
                if [ -d ${D}${libdir}/security ]; then
                        mv ${D}${libdir}/security ${D}${base_libdir}
                fi
        fi
	rm ${D}${base_libdir}/security -rf
}

#FILES_${PN}-dev += "${base_libdir}/*.so"

# pam files
FILES_${PN} += "/lib/security/*.so"

BBCLASSEXTEND = "native nativesdk"

