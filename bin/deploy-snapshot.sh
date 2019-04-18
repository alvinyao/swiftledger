#!/bin/bash

if [[ $TRAVIS_BRANCH == master ]] || \
   [[ $TRAVIS_BRANCH == release* ]] || \
   [[ $TRAVIS_BRANCH == dev* ]] ;
then
  echo -e "Publishing maven snapshot...\n"
  mvn -s bin/settings.xml deploy -DskipTests -Dmaven.install.skip=true -B
  echo -e "Published maven snapshot"
else
  echo -e "branch $TRAVIS_BRANCH no need publish"
fi