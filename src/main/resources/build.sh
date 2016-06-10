#!/bin/bash

srcdir="$1"
destdir="$2"
toolchainfile="$3"

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


