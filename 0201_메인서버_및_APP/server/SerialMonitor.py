import serial

port = "/dev/ttyACM0"
serialFromArduino = serial.Serial(port, 9600);
serialFromArduino.flushInput();
a=1

while 1:
    input = serialFromArduino.readline();
    print input[:-1]
