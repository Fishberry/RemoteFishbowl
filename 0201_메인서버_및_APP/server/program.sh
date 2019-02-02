#!/bin/bash

python SerialMonitor.py &
cd ./MjpgDirectory/mjpg-streamer
sh mjpg.sh &
