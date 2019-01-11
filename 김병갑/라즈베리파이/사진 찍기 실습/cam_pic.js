var wpi = require('wiringpi-node');		//nodejs에서 GPIO 정보를 받기 위한 라이브러리
var exec = require('child_process').exec	//쉘 명령어를 사용하기 위한 라이브러리

wpi.setup('wpi');	//wpi를 사용하기 위한 메소드.

var pin = 29;	//핀 변수 (29번에 꽂혀있다. 하지만 일반적으로는 21번을 사용한다.)
var result = 0;	//실제 핀에 입력이 있는지에 대한 변수 (초기화를 위해 0으로)
var i = 1;		//사진 저장을 위한 변수(1.jpg, 2.jpg 등등)

wpi.pinMode(pin, wpi.INPUT);	//푸시 버튼을 누를 시, wPi GPIO. 29로 입력상태를 확인. 따라서 이 핀을 INPUT 모드로 설정

setInterval(() => {		//0.5초(2번째 인자값)마다 한번씩 setInterval을 실행하여 푸시버튼이 눌렸는지에 대한 여부 확인
	result = wpi.digitalRead(pin);		//푸시버튼 입력이 있었는지에 대하여 digitalRead() 메소드로 확인하고 result에 저장

	if(result) {		//푸시버튼을 눌렀다면
		console.log('Take a picture');
		exec('raspistill -o /home/pi/Desktop/nodeJS/ + i + .jpg');		//raspistill 명령어로 사진 촬영
		i++;
	}
}, 500);