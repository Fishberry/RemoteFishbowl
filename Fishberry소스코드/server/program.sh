#!/bin/bash

cd ./background
python SerialMonitor.py &
python pH_Sensor.py &
cd ./MjpgDirectory/mjpg-streamer
sh mjpg.sh &

forever start /home/pi/Desktop/FishberryServer/app.js
