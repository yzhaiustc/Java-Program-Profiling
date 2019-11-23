#!/bin/bash

declare -a method_names=("1" "3" "4")
method_nums=${#method_names[@]}

cd sootOutput/dava/classes

for (( i=0; i<${method_nums}; i++ ));
do
    java_name="Test${method_names[$i]}";
    output_name="../../../myOutput/dynamicOutput/Test${method_names[$i]}_dynamic_output.txt"
    java $java_name &> $output_name
done