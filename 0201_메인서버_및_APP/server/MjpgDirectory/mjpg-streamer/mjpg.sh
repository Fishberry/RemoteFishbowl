sudo modprobe bcm2835-v4l2
./mjpg_streamer -i "./input_uvc.so -d /dev/video0 -n -f 30 -r 640x480 -y" -o "./output_http.so -n -w ./www"
