import time

import Adafruit_GPIO.SPI as SPI
import Adafruit_MCP3008

# Hardware SPI configuration:
SPI_PORT   = 0
SPI_DEVICE = 0
mcp = Adafruit_MCP3008.MCP3008(spi=SPI.SpiDev(SPI_PORT, SPI_DEVICE))


print('Reading MCP3008 values, press Ctrl-C to quit...')

# Main program loop.
while True:
    readValue = mcp.read_adc(0)
    voltage = readValue*5.0/1024
    pHValue = 3.5*voltage
    print("Read Value : {0}".format(readValue))
    print("Voltage : %0.5fV"%(voltage))
    print("pH Value : %0.2f"%(pHValue))
    print("-")*20

    with open('pH_log', 'w') as f:
        f.write(str(pHValue))
    time.sleep(1)
