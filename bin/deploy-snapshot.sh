#!/bin/bash

reg = '^v(\d+.){1,2}\d+.*&'
if [[ !"$TRAVIS_BRANCH" =~ $reg ]]; then
  mvn -s bin/settings.xml deploy -DskipTests -Dmaven.javadoc.skip=true
fi