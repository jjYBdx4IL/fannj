#!/bin/bash

set -o pipefail
set -Eex

basedir=$1
fannversion=$2
shift 2

JAVA_INC=$JAVA_HOME/include/linux
if which cygpath; then
    basedir=$(cygpath -u "$basedir")
    JAVA_INC=$JAVA_HOME/include/win32
fi

srcdir=$basedir/target/fann-$fannversion

for toolchainfile in $basedir/src/main/c/cmake-toolchain-*; do
    
 #   if [[ $toolchainfile =~ linux ]]; then
 #       unset CFLAGS
        # disable openmp dependency for windows cross-compilation target
#        export CFLAGS="-DDISABLE_PARALLEL_FANN"
#    else
#        continue
 #   fi
    
    target=${toolchainfile##*/cmake-toolchain-}
    cd $srcdir
    builddir=$srcdir/build.$target
    rm -rf $builddir
    install -d $builddir
    cd $builddir
    # -DCMAKE_BUILD_TYPE=Debug
    cmake $srcdir -DCMAKE_TOOLCHAIN_FILE=$toolchainfile
    #make doublefann 
    make floatfann

    case $target in
        x86_64-linux-gnu)
            destdir=$basedir/target/classes/linux-x86-64
            libprefix=lib
            libext=.so
            CARGS="-shared -static-libgcc -fPIC -O2 -I${JAVA_HOME#$basedir/}/include -I${JAVA_INC#$basedir/}"
            OBJEXT=.c.o
            LIBS=
            ;;
        x86_64-w64-mingw32)
            destdir=$basedir/target/classes/win32-x86-64
            libprefix=lib
            libext=.dll
            CARGS="-shared -static-libgcc -O2 -I${JAVA_HOME#$basedir/}/include -I${JAVA_INC#$basedir/}"
            OBJEXT=.c.obj
            LIBS="-l:libgomp.a -l:libpthread.a"
            ;;
    esac

    install -d $destdir
    #cp src/libfloatfann$libext $destdir/${libprefix}fann${libext}

    if [[ $target =~ linux ]]; then
        make fann_tests
        ./tests/fann_tests
    fi

    # build JNI native layer
    # `find $builddir -name "*$OBJEXT"` \
    cd $basedir
    libjniname=${destdir#$basedir/}/${libprefix}fann${libext}
    $target-gcc $CARGS \
        -I./target/generated-sources/c -I${srcdir#$basedir/}/src/include \
        -o ${libjniname#$basedir/}  \
        `find $builddir -name "*floatfann$OBJEXT"` \
        $LIBS \
        ./src/main/c/FannJNI.c 
done
