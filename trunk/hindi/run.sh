#!/bin/bash
DIR=$1

for filename in $( ls $DIR/*.prun );
do
        echo $filename
        ./insert_null.py $filename $filename.null
#       diff $filename $filename.tmp
#       rm $filename.tmp
done

