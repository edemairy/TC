#!/bin/env bash
for threshold in $(seq 71 73); do
  ((x=0))
  export THRESHOLD=$threshold
  for i in $(seq 1 99); do
    if (( i >= 10 )); then
      echo -ne "\b";
    fi;
    echo -ne "\b$i"
    ((c=`java -jar ~/Dropbox/personnel/devel/TC/CuttingFigures/Visualizer.jar -seed $i -novis -forceexit -exec ./CuttingFigures | sed -e "s/Income = //" `))
#    printf "%d) %f\n" $i $c
    ((x=x+c))
  done
  printf "\b\b%3d |  %e\n" $threshold $x
done
