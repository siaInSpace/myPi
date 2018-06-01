#!/bin/bash
git pull
javac ./myPi/src/main/java/sia20/myPi/* -d ./myPi/target/classes
java -cp /opt/pi4j/lib/*:./myPi/target/classes/ sia20.myPi.App
