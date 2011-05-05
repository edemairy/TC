#!/bin/bash 

#####################################################################
# Default scale used by float functions.

float_scale=2


#####################################################################
# Evaluate a floating point number expression.

function float_eval()
{
    local stat=0
    local result=0.0
    if [[ $# -gt 0 ]]; then
        result=$(echo "scale=$float_scale; $*" | bc -q 2>/dev/null)
        stat=$?
        if [[ $stat -eq 0  &&  -z "$result" ]]; then stat=1; fi
    fi
    echo $result
    return $stat
}


#####################################################################
# Evaluate a floating point number conditional expression.

function float_cond()
{
    local cond=0
    if [[ $# -gt 0 ]]; then
        cond=$(echo "$*" | bc -q 2>/dev/null)
        if [[ -z "$cond" ]]; then cond=0; fi
        if [[ "$cond" != 0  &&  "$cond" != 1 ]]; then cond=0; fi
    fi
    local stat=$((cond == 0))
    return $stat
}

export MR1_ROOT=${HOME}/Documents/Personal/TC/TCO11_MR1_ImageScanner/
cd ${MR1_ROOT}/files
total=0
declare -i nbcases=5
for ((i=1;i<=${nbcases};++i)); do
  val=`java -jar ImageScannerVis.jar -novis -exec "java -jar ${MR1_ROOT}/dist/TCO11_MR1_ImageScanner.jar" -seed $i | grep Score | sed -e 's/Score.*= //'` 
  echo "$i) $val"
  total=$(float_eval "$total+$val")
done
echo "total = $total"
echo "score = $(float_eval "$total/$nbcases")" 
