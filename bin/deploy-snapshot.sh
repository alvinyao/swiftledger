#!/bin/bash

reg='^v(\d+.){1,2}\d+.*&'
if [[ !"$TRAVIS_BRANCH" =~ $reg ]]; then
  echo -e "Publishing maven snapshot...\n"
  mvn -s bin/settings.xml deploy -Dmaven.install.skip=true -Dmaven.javadoc.skip=true -B
  echo -e "Published maven snapshot"
fi