#!/bin/bash
DIR=$1

for filename in $( ls $DIR/*.$2 );
do
        echo $filename
	./check_drel.py $filename
#       ./insert_null.py $filename $filename.null
#       diff $filename $filename.tmp
#       rm $filename.tmp
done

