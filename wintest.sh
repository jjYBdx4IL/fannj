#!/bin/bash
# vim:set sw=4 ts=4 et ai smartindent fileformat=unix fileencoding=utf-8 syntax=sh:
set -Eex ; set -o pipefail
export LANG=C LC_ALL=C
scriptdir=$(readlink -f "$(dirname "$0")")
cd $scriptdir

finalName=$1
shift

rm -rf target/wine
rsync -a --exclude=/target/wine/ ./ target/wine/
pushd target/wine
rm -rf target/failsafe-reports target/classes
cp target/$finalName.jar out.jar
wineconsole cmd /C wintest.cmd
echo OK

