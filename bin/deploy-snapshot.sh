#!/bin/bash

if [[ $TRAVIS_BRANCH == master ]] || \
   [[ $TRAVIS_BRANCH == dev* ]] || \
   [[ $TRAVIS_BRANCH == release* ]] ;
then
  echo -e "Publishing maven snapshot...\n"
  mvn -s bin/settings.xml deploy -Dmaven.install.skip=true -Dmaven.javadoc.skip=true -B
  echo -e "Published maven snapshot"
fi