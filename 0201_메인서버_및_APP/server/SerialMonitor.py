import serial
import os

if os.path.exists("/dev/ttyACM0") :
    tty = "/dev/ttyACM0"
elif os.path.exists("/dev/ttyACM1") :
    tty = "/dev/ttyACM1"

serialFromArduino = serial.Serial(tty, 9600);
serialFromArduino.flushInput();

while 1:
    input = serialFromArduino.readline();
    print input[:-1]
