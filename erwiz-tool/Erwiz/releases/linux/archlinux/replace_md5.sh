#!/bin/sh
echo "sed -i \"s/^md5sums=(\(.*\))/\md5sums=($1)/g\" $2"
sed -i "s/^md5sums=(\(.*\))/\md5sums=($1)/g" $2