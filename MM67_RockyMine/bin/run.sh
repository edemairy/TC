#!/bin/bash -xv
export OPTIONS_DEBUG="-Xdebug -agentlib:jdwp=transport=dt_socket,server=y,address=8000,quiet=y"
cd `dirname $0`
java -jar RockyMineVis.jar -delay 10 -exec "java ${OPTIONS_DEBUG} -cp $PWD/../build/classes RockyMineAdapter" -seed 10
