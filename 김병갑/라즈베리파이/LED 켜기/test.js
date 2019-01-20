//서버의 설정
const express = require('express');	//익스프레스 사용
const http = require('http');		//http 웹 서버를 위해 사용
const app = express();
const path = require('path');		//파일 경로를 위해 사용
const bodyParser = require('body-parser');	//post로 값을 전달했기 때문에 bodyparser사용

//body-parser를 사용하기 위한 설정
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended : true}));

//웹 서버 생성 및 구동
const server = http.createServer(app);
server.listen(3000, () => {
	console.log('Server Start');
});

//ejs 설정 코드
app.set('views', path.join(__dirname, 'views'));	// '현재폴더/views' 폴더에 ejs가 있다.
app.set('view-engine', 'ejs');				// view 엔진을 ejs로 하겠다!
app.use(express.static(path.join(__dirname, 'public')));

//wiringpi 설정 코드
const wpi = require('wiringpi-node');	//wiringpi는 원래 C코드이지만, node에서 사용할 수 있도록 모듈 사용
const pin = 25;		//wiringpi-node의 모듈 기준으로 25번(물리 핀 : GPIO26)
var isOn = 0;		//LED 스위치 변수

wpi.setup('wpi');	//wiringpi 구동

wpi.pinMode(pin, wpi.OUTPUT);	//GPIO26번 핀을 OUTPUT모드(전기 방출)로 변경한다.

setInterval(() => {	//주기적으로  반복한다.
	if(isOn == 1)	//1이라는 신호가 왔으면 켜라
		wpi.digitalWrite(pin, wpi.HIGH);
	else		//나머지(여기서는 0) 신호가 오면 꺼라.
		wpi.digitalWrite(pin, wpi.LOW);
}, 500);//0.5초마다

//서버 페이지 경로 설정
app.get('/', (req, res) => {	// 메인 페이지로 들어오면 controller.ejs로 보내라. http코드는 200번(정상)으로
	res.status(200).render('controller.ejs');
});

app.post('/data', (req, res) => {// '/data' post 요청을 하면
	console.log('String : ', req.body.led);	//body-parser를 이용해 led라는 이름을 가진 속성의 값을 출력
	var state = req.body.led;	//속성값 저장

	if(state == 'on')	//속성값이 on이면
		isOn = 1;
	else			//속성값이 on이 아니면(여기서는 off)
		isOn = 0;
});
