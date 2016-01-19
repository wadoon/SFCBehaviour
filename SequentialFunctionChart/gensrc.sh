#!/bin/bash 

P=$(pwd)
rm -rf generated
mkdir -p generated

cd share

java -jar $P/lib/antlr-4.5.1-complete.jar \
    -o $P/generated/edu/kit/iti/sfc/input \
    -encoding utf-8 \
    -package edu.kit.iti.sfc.input \
    SFCA.g4

