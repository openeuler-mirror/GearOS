SUMMARY = "A portable foreign function interface library"
HOMEPAGE = "http://sourceware.org/libffi/"
DESCRIPTION = "The `libffi' library provides a portable, high level programming interface to various calling \
conventions.  This allows a programmer to call any function specified by a call interface description at run \
time. FFI stands for Foreign Function Interface.  A foreign function interface is the popular name for the \
interface that allows code written in one language to call code written in another language.  The `libffi' \
library really only provides the lowest, machine dependent layer of a fully featured foreign function interface.  \
A layer must exist above `libffi' that handles type conversions for values passed between the two languages."

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=492385fe22195952f5b9b197868ba268"

SRC_URI = "file://libffi-3.3.tar.gz \
           file://not-win32.patch \
           file://0001-Fixed-missed-ifndef-for-__mips_soft_float.patch \
           file://0001-arm-sysv-reverted-clang-VFP-mitigation.patch \
           file://0001-powerpc-fix-build-failure-on-power7-and-older-532.patch \
           file://0001-Address-platforms-with-no-__int128.patch \
           file://0001-Address-platforms-with-no-__int128-part2.patch \
           file://0001-ffi_powerpc.h-fix-build-failure-with-powerpc7.patch \
           "
EXTRA_OECONF += "--disable-builddir"
EXTRA_OEMAKE_class-target = "LIBTOOLFLAGS='--tag=CC'"
inherit autotools texinfo multilib_header

do_install_append() {
	oe_multilib_header ffi.h ffitarget.h
}

FILES_${PN}-dev += "${libdir}/libffi-${PV}"

# Doesn't compile in MIPS16e mode due to use of hand-written
# assembly
MIPS_INSTRUCTION_SET = "mips"

BBCLASSEXTEND = "native nativesdk"

