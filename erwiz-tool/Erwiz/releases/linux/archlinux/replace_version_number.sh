#!/bin/sh
echo "sed -i \"s/^pkgver=\(.*\)/\pkgver=($1)/g\" $2"
sed -i "s/^pkgver=\(.*\)/\pkgver=($1)/g" $2