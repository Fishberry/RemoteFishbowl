온도 설정 전 세팅법

1. /boot/config.txt의 마지막에 다음의 한 줄 추가 후 재부팅
dtoverlay=w1-gpio

2. 재부팅 후 w1 통신 관련 커널 모듈 로드
sudo modprobe w1-gpio
sudo modprobe w1-therm

================================================

센서의 개수를 보고싶으면?
cat /sys/bus/w1/devices/w1_bus_master1/w1_master_slave_count
결과값(한 개 있을 경우) : 1

센서의 고유 아이디를 알고싶으면?
cat /sys/bus/w1/devices/w1_bus_master1/w1_master_slaves
결과값(온도센서의 경우(28로 시작함)) : 28-020d9246133d

================================================

원리

'/sys/bus/w1/devices/28-020d9246133d(예시임)/w1_slave' 파일 안의 내용에

(아래의 항목은 예시)
b5 01 4b 46 7f ff 0b 10 a3 : crc=a3 YES
b5 01 4b 46 7f ff 0b 10 13 t=27312

라고 적혀있을 때, 온도는 맨 끝의 숫자인 27312(예시)고, 실제 온도는 '섭씨 27.312도'가 된다. 이 글자를 가져오는 것.