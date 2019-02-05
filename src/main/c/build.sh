#!/bin/bash

set -o pipefail
set -Eex

basedir=$1
fannversion=$2
shift 2

# disable openmp dependency
export CFLAGS="-DDISABLE_PARALLEL_FANN"

JAVA_INC=$JAVA_HOME/include/linux
if which cygpath; then
    basedir=$(cygpath -u "$basedir")
    JAVA_INC=$JAVA_HOME/include/win32
fi

srcdir=$basedir/target/fann-$fannversion

for toolchainfile in $basedir/src/main/c/cmake-toolchain-*; do
    
    #if [[ $toolchainfile =~ linux ]]; then continue; fi
    
    target=${toolchainfile##*/cmake-toolchain-}
    cd $srcdir
    builddir=$srcdir/build.$target
    rm -rf $builddir
    install -d $builddir
    cd $builddir
    # -DCMAKE_BUILD_TYPE=Debug
    cmake $srcdir -DCMAKE_TOOLCHAIN_FILE=$toolchainfile
    make fann 

    case $target in
        x86_64-linux-gnu)
            destdir=$basedir/target/classes/linux-x86-64
            libprefix=lib
            libext=.so
            CARGS="-shared -fPIC -O2 -I${JAVA_HOME#$basedir/}/include -I${JAVA_INC#$basedir/}"
            ;;
        x86_64-w64-mingw32)
            destdir=$basedir/target/classes/win32-x86-64
            libprefix=lib
            libext=.dll
            CARGS="-shared -O2 -I${JAVA_HOME#$basedir/}/include -I${JAVA_INC#$basedir/}"
            ;;
    esac

    install -d $destdir
    cp src/libfann$libext $destdir/${libprefix}fann${libext}

    if [[ $target =~ linux ]]; then
        make fann_tests
        ./tests/fann_tests
    fi

    # build JNI native layer
    cd $basedir
    libjniname=${destdir#$basedir/}/${libprefix}jnifann${libext}
    $target-gcc $CARGS \
        -I./target/generated-sources/c -I${srcdir#$basedir/}/src/include \
        -o ${libjniname#$basedir/} \
        -L${destdir#$basedir/} -lfann \
        ./src/main/c/FannJNI.c
done
