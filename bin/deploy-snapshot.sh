#!/bin/bash

if [ !"$TRAVIS_BRANCH" =~  ^v(\d+.){1,2}\d+.*& ]; then
  mvn -s bin/settings.xml deploy -DskipTests -Dmaven.javadoc.skip=true
fi