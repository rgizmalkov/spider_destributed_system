#!/bin/bash

port=8002
target=node-0.0.1-SNAPSHOT.jar

function run () {
    cd $1
    java -jar "$target"
    echo 'Node of distributed system started on port = '$1
    cd ..
}

run "$port"