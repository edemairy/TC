#!/bin/bash 
export MR1_ROOT=${HOME}/Documents/Personal/TC/TCO11_MR1_ImageScanner/
cd ${MR1_ROOT}/files
set i=0
for ((i=0; i<10; i++)); do
result=`java -jar ImageScannerVis.jar -novis -exec "java -jar ${MR1_ROOT}/dist/TCO11_MR1_ImageScanner.jar" -seed $i | grep "Score ="| sed -e 's/Score = //'`
echo "$i) $result"
done
