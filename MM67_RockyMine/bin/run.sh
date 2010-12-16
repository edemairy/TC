#!/bin/bash -xv
cd `dirname $0`
java -jar RockyMineVis.jar -exec "java -cp $PWD/../build/classes RockyMineAdapter" -seed 10
