#!/bin/bash

set -o pipefail
set -Eex

basedir=$1
fannversion=$2
shift 2

srcdir=$basedir/target/fann-$fannversion

for toolchainfile in $basedir/src/main/resources/cmake-toolchain-*; do
    target=${toolchainfile##*/cmake-toolchain-}
    cd $srcdir
    builddir=$srcdir/build.$target
    rm -rf $builddir
    install -d $builddir
    cd $builddir
    cmake $srcdir -DCMAKE_TOOLCHAIN_FILE=$toolchainfile # -DCMAKE_BUILD_TYPE=Debug
    make fann

    case $target in
        x86_64-linux-gnu)
            destdir=$basedir/target/classes/linux-x86-64
            libprefix=lib
            libext=.so
            CARGS="-shared -fPIC -O2 -I$JAVA_HOME/include -I$JAVA_HOME/include/linux"
            ;;
        x86_64-w64-mingw32)
            destdir=$basedir/target/classes/win32-x86-64
            libprefix=
            libext=.dll
            CARGS="-shared -O2 -I$JAVA_HOME/include -I$JAVA_HOME/include/linux"
            ;;
    esac

    install -d $destdir
    cp src/libfann.so $destdir/${libprefix}fann${libext} || cp src/libfann.dll $destdir/${libprefix}fann${libext}

    if [[ $target =~ linux ]]; then
        make fann_tests
        ./tests/fann_tests
    fi

    # build JNI native layer
    libjniname=$destdir/${libprefix}jnifann${libext}
    $target-gcc $CARGS \
        -I$basedir/target/generated-sources/c -I$srcdir/src/include \
        -L$destdir -lfann \
        $basedir/src/main/c/FannJNI.c \
        -o $libjniname
done
