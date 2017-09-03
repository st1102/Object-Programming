#!/bin/sh
[ -f "$1" ] || { echo "usage: $0 datafile.dat"; exit 1; }

output=`basename $1 .dat`

gnuplot <<EOF
set size ratio 1.0
set terminal postscript
unset key
set output "$output"
plot "$1" with lines
EOF
open $output
