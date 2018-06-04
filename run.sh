
#!/bin/bash
rm -rf ./myPi/target
rm -rf ./data
git pull
mkdir -p myPi/target/classes
mkdir -p ./data/measurements
mkdir -p ./data/CalibrationValues
javac -d "./myPi/target/classes" -cp "/opt/pi4j/lib/*" ./myPi/src/main/java/sia20/myPi/*
java -cp /opt/pi4j/lib/*:./myPi/target/classes/ sia20.myPi.App
