#!/bin/bash

python SerialMonitor.py &
python pH_Sensor.py &
cd ./MjpgDirectory/mjpg-streamer
sh mjpg.sh &
