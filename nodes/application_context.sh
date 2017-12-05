#!/bin/bash

declare -a ports=("8001" "8002" "8003" "8004" "8005" "8006")

function update_application_properties () {
    cd $1
    jar uvf node-0.0.1-SNAPSHOT.jar application.yml
    cd ..
}

for port in "${ports[@]}"
do
    update_application_properties "$port"
done
