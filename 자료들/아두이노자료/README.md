# 아두이노 관련 자료들 모음

* 아두이노에 사용할 센서 및 부품 : 방수온도센서(DS18B20), pH수질측정센서(Analog pH Meter), 워터펌프(샤플로펌프(DC모터)), 
서보모터(SG5010), 각종 LED센서, Buzzer, RTC모듈(RS1307)  
* 제작해야 할 아두이노 동작 : 자동부분환수기, 자동먹이지급기, 수온&수질 측정기, LED 상태표시, 자동 수온조절기, 산소농도 측정기

각 라이브러리 설치방법 : 아두이노 설치폴더 내 libraries 폴더에 zip파일 풀어서 저장.  

Adafruit-Motor-Shield-library-master : L293D 모터드라이터에서 사용되는 라이브러리  
OneWire-master : 방수온도센서에 사용되는 라이브러리  

방수온도센서는 노란색은 D2\~D13중 D2에 꽂아보았다. 빨간색은 5V, 검정색은 GND이다.  
ph센서는 파란색은 A0\~A6 아날로그입력, 빨간색은 5V, 검정색은 GND이다.  

방수온도센서 관련 자료페이지 : https://www.dfrobot.com/wiki/index.php/Terminal_sensor_adapter_V2.0_(SKU:DFR0055)
pH센서 관련 자료페이지 : https://blog.naver.com/roboholic84/220550642030
