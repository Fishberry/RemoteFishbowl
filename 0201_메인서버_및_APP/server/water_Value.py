import serial
import os

if os.path.exists("/dev/ttyACM0") :
    tty = "/dev/ttyACM0"
elif os.path.exists("/dev/ttyACM1") :
    tty = "/dev/ttyACM1"

serialFromArduino = serial.Serial(tty, 9600);
serialFromArduino.flushInput();

while True:
    input = serialFromArduino.readline();

    if input == "FinalWater":
        with open('water_log', 'w') as f:
            f.write(input)

        time.sleep(1)
