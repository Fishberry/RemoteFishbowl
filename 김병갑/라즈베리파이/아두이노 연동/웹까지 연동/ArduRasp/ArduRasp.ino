#include <OneWire.h>
#include <Servo.h>
#include <Wire.h>
#include "RTClib.h"

const int DS18S20_Pin = 2;
const int WaterPumpA1 = 6;
const int WaterPumpA2 = 7;
const int WaterPumpB1 = 4;
const int WaterPumpB2 = 5;
const int SERVO = 8;

Servo myServo;
RTC_DS1307 RTC;
String year, month, day, hour, minute, second, time, date;
OneWire ds(DS18S20_Pin);

void setup()
{
  Serial.begin(9600);
  myServo.attach(SERVO);
  
  Wire.begin();
  RTC.begin();
  
  if(!RTC.isrunning())
  {
    RTC.adjust(DateTime(__DATE__, __TIME__));
  }
  
  pinMode(WaterPumpA1, OUTPUT);
  pinMode(WaterPumpA2, OUTPUT);
  pinMode(WaterPumpB1, OUTPUT);
  pinMode(WaterPumpB2, OUTPUT);
}

void loop()
{
  String userHour = "14";
  String userMinute = "8";
  
  if(Serial.available())
  {
    char c = Serial.read();
    
    if(c == '1')
    {
      getTemp();
    }
    if(c == '2')
    {
      operateServo();
    }
    if(c == '3')
    {
      operateMotor();
    }
    
    c = '4';
  }
}

boolean getTime(String userHour, String userMinute)
{
  DateTime now = RTC.now();
  Serial.print(now.year(), DEC);
  year = String(now.year(), DEC);
  Serial.print('/');
  Serial.print(now.month(), DEC);
  month = String(now.month(), DEC);
  Serial.print('/');
  Serial.print(now.day(), DEC);
  day = String(now.day(), DEC);
  Serial.print('/');
  Serial.print(now.hour(), DEC);
  hour = String(now.hour(), DEC);
  Serial.print('/');
  Serial.print(now.minute(), DEC);
  minute = String(now.minute(), DEC);
  Serial.print('/');
  Serial.print(now.second(), DEC);
  second = String(now.second(), DEC);
  Serial.print('   ');
  Serial.println();
  delay(1000);
  
  if(hour == userHour && minute == userMinute)
  {
    return true;
  }
  else
  {
    return false;
  }
}

float getTemp()
{
  byte data[12];
  byte addr[8];
  
  if(!ds.search(addr))
  {
    Serial.println("no more sensors on chain, reset search!");
    ds.reset_search();
    return -1000;
  }
  
  if(OneWire::crc8(addr, 7) != addr[7])
  {
    Serial.println("CRC is not vaild!");
    return -1000;
  }
  
  if(addr[0] != 0x10 && addr[0] != 0x28)
  {
    Serial.print("Device is not recognized");
    return -1000;
  }
  
  ds.reset();
  ds.select(addr);
  ds.write(0x44, 1);
  
  byte present = ds.reset();
  ds.select(addr);
  ds.write(0xBE);
  
  for(int i = 0; i < 9; i++)
  {
    data[i] = ds.read();
  }
  
  ds.reset_search();
  
  byte MSB = data[1];
  byte LSB = data[0];
  
  float tempRead = ((MSB << 8) | LSB);
  float Temper = tempRead / 16;
  
  return Temper;
}

void operateMotor()
{
  analogWrite(WaterPumpA1, 255);
  analogWrite(WaterPumpA2, 0);
  delay(1000);
  
  analogWrite(WaterPumpA1, 0);
  analogWrite(WaterPumpA2, 0);
  delay(3000);
  
  analogWrite(WaterPumpB1, 255);
  analogWrite(WaterPumpB2, 0);
  delay(1000);
  
  analogWrite(WaterPumpB1, 0);
  analogWrite(WaterPumpB2, 0);
  delay(3000);
}

void operateServo()
{
  for(int i = 0; i < 180; i++)
  {
    myServo.write(i);
    delay(15);
  }
  
  myServo.write(0);
  delay(1000);
}
