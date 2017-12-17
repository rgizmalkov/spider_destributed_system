#!/bin/bash

declare -a ports=("8001" "8002" "8003" "8004" "8005")
target=node-0.0.1-SNAPSHOT.jar
printf "%s\n" "#!/bin/bash" >| to_kill.sh
function run () {
    cd $1
    java -jar "$target" &
    echo 'Node of distributed system started on port = '$1
    printf "kill -9 %s\n" $! >> ../to_kill.sh
    cd ..
}


for port in "${ports[@]}"
do
    run "$port"
done
