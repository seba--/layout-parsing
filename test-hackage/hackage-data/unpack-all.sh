#!/bin/sh

for dir in `ls -p | egrep '/'`
do
  cd $dir
  for subdir in `ls -p | egrep '/'`
  do
    for file in `ls $subdir | egrep '.tar.gz'`
    do
      echo "unpacking $file"
      gunzip -c $subdir/$file | tar xv
    done
  done
  cd ..
done