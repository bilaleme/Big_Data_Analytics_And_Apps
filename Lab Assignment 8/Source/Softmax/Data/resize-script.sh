#!/bin/bash

#simple script for resizing images in all class directories
#also reformats everything from whatever to png

if [ `ls Test/*/*.jpg 2> /dev/null | wc -l ` -gt 0 ]; then
  echo hi
  for file in Test/*/*.jpg; do
    convert "$file" -resize 28x28\! "${file%.*}.png"
    file "$file" #uncomment for testing
    rm "$file"
  done
fi

if [ `ls Test/*/*.png 2> /dev/null | wc -l ` -gt 0 ]; then
  echo hi
  for file in Test/*/*.png; do
    convert "$file" -resize 28x28\! "${file%.*}.png"
    file "$file" #uncomment for testing
  done
fi

if [ `ls Train/*/*.jpg 2> /dev/null | wc -l ` -gt 0 ]; then
  echo hi
  for file in Train/*/*.jpg; do
    convert "$file" -resize 28x28\! "${file%.*}.png"
    file "$file" #uncomment for testing
    rm "$file"
  done
fi

if [ `ls Train/*/*.png 2> /dev/null | wc -l ` -gt 0 ]; then
  echo hi
  for file in Train/*/*.png; do
    convert "$file" -resize 28x28\! "${file%.*}.png"
    file "$file" #uncomment for testing
  done
fi
