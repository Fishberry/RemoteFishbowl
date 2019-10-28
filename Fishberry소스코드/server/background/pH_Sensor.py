# -*- coding: utf-8 -*-

import time # sleep 사용을 위해 임포트

# 아래 두개 임포트는 라즈베리파이의 GPIO사용과 MCP3008 부품을 이용하기 위해서.
# MCP3008은 라즈베리로 들어오는 아날로그 신호를 디지털 신호로 변환해줌(PH센서 때문에 사용)
import Adafruit_GPIO.SPI as SPI
import Adafruit_MCP3008

# Hardware SPI configuration(SPI 설정):
SPI_PORT   = 0
SPI_DEVICE = 0
mcp = Adafruit_MCP3008.MCP3008(spi=SPI.SpiDev(SPI_PORT, SPI_DEVICE))


print('Reading MCP3008 values, press Ctrl-C to quit...')

# Main program loop.
while True:
    print(mcp.read_adc(0), mcp.read_adc(1), mcp.read_adc(2), mcp.read_adc(3), mcp.read_adc(4), mcp.read_adc(5), mcp.read_adc(6), mcp.read_adc(7))
    readValue = mcp.read_adc(0) # PH센서의 입력값이 디지털신호로 바뀐 값을 받아서 저장. 아래 값은 계산

    buf = []

    for i in range(0, 50):
        buf.append(mcp.read_adc(0))
        time.sleep(0.01)

    buf.sort()

    avgValue = 0
    for i in range(10, 40):
        avgValue += buf[i]
    
    sensorValue = avgValue/30
    #phph = 7-1000*(sensorValue-365)*4.95/59.16/1023.2

    voltage = sensorValue*5.0/1024
    #pHValue = 3.5*voltage - 2
    pHValue = 3.5*voltage + 1; 
    print("Read Value : {0}".format(readValue))
    print("Voltage : %0.5fmV"%(voltage))
    print("pH Value : %0.2f"%(pHValue))
    print("-")*20

    # with~as 구문은 파일을 열고 자동으로 f.close 해준다
    with open('pH_log', 'w') as f:
        f.write(str(pHValue)) # PH값을 String형으로 변환 후 파일 첫번째줄에 저장

    # 1초마다 루프 반복
    time.sleep(1)
