#!/bin/bash

srcdir="$1"
destdir="$2"
toolchainfile="$3"
basedir="$4"
compiler="$5"
shift 5

set -o pipefail
set -Eex

cd "$srcdir"
rm -rf build
install -d build
cd build
cmake "$srcdir" -DCMAKE_TOOLCHAIN_FILE="$toolchainfile"
make
install -d "$destdir"
cp src/libfann.so "$destdir/libfann.so" || cp src/libfann.dll "$destdir/fann.dll"

libjniname="$destdir/jnifann.dll"
if [[ "x$compiler" != "x${compiler##*linux}" ]]; then
    libjniname="$destdir/libjnifann.so"
fi
$compiler -shared -fPIC -g -O2 -I$JAVA_HOME/include -I$JAVA_HOME/include/linux \
    -I"$basedir/target/generated-sources/c" -I"$srcdir/src/include" \
    -L"$destdir" -lfann \
    "$basedir/src/main/c/FannJNI.c" \
    -o "$libjniname"
