#!/bin/bash
# Må ha et gyldig token satt i miljøvariabelen TOKEN_Q1, dette kan settes på denne måten:
# export TOKEN_Q1="<gyldig token>"
siege -l -v -i -c15 -t5m --content-type "application/json" --header="Authorization: Bearer $TOKEN_Q1" -f urls-to-hit.txt
