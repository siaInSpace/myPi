#!/bin/bash
git pull
javac -cp /opt/pi4j/lib/*:./myPi/src/main/java/sia20/myPi/App -d ./myPi/target/classes
java -cp /opt/pi4j/lib/*:./myPi/target/classes/ sia20.myPi.App
