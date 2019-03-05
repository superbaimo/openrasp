#!/bin/bash
set -ev

pushd agent/java
mkdir temp
cp -r boot/target/classes temp
cp -r engine/target/classes/com/baidu/openrasp/* tmp/classes/com/baidu/openrasp
pushd integration-test/jacoco
dataFile=/home/travis/build/baidu/openrasp/agent/java
java -jar jacococli.jar report $dataFile/jacoco.exec --classfiles ../../temp/classes/ --xml jacoco.xml
popd
rm -rf temp
popd
curl -s https://codecov.io/bash