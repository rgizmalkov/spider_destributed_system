#!/bin/bash

## declare an array variable
declare -a ports=("8001" "8002" "8003" "8004" "8005")
location=../node/target/
file_name=node-0.0.1-SNAPSHOT.jar

function update () {
    cp $1 ./$2
}

from="$location""$file_name"
for port in "${ports[@]}"
do
   update "$from" "$port"
   # or do whatever with individual element of the array
done
