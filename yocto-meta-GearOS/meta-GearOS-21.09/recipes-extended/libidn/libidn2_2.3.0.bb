SUMMARY = "Internationalized Domain Name support library"
DESCRIPTION = "Implementation of the Stringprep, Punycode and IDNA specifications defined by the IETF Internationalized Domain Names (IDN) working group."
HOMEPAGE = "http://www.gnu.org/software/libidn/"
SECTION = "libs"
LICENSE = "(GPLv2+ | LGPLv3) & GPLv3+"
LIC_FILES_CHKSUM = "file://COPYING;md5=2d834ea7d480438ada04e5d846152395 \
                    file://COPYING.LESSERv3;md5=e6a600fd5e1d9cbde2d983680233ad02 \
                    file://COPYINGv2;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
                    file://src/idn2.c;endline=16;md5=426b74d6deb620ab6d39c8a6efd4c13a \
                    file://lib/idn2.h.in;endline=27;md5=c2cd28d3f87260f157f022eabb83714f"

SRC_URI = "file://libidn2-2.3.0.tar.gz"

DEPENDS = "virtual/libiconv libunistring"

inherit pkgconfig autotools gettext texinfo gtk-doc lib_package

EXTRA_OECONF += "--disable-rpath \
                 --with-libunistring-prefix=${STAGING_EXECPREFIXDIR} \
                 "

do_install_append() {
	# Need to remove any duplicate whitespace too for reproducibility
	sed -i -e 's|-L${STAGING_LIBDIR}||' -e 's/  */ /g' ${D}${libdir}/pkgconfig/libidn2.pc
}

LICENSE_${PN} = "(GPLv2+ | LGPLv3)"
LICENSE_${PN}-bin = "GPLv3+"

BBCLASSEXTEND = "native nativesdk"
