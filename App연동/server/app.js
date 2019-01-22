const express = require('express');
const http = require('http');
const fs = require('fs');
const app = express();
const path = require('path');
const socketio = require('socket.io');
const server = http.createServer(app);
const morgan = require('morgan');

const mainpageRouter = require('./routes/mainpage');

const serialPort = require('serialport');
const com6 = new serialPort('COM6', {
	baudRate: 9600,
	dataBits: 8,
	parity: 'none',
	stopBits: 1,
	flowControl: false
});
com6.on('open', () => {
	console.log('open serial communication');
});

let temperature = 0.0;
fs.readFile('/sys/bus/w1/devices/28-020d9246133d/w1_slave', 'utf8', (err, data) => {
	console.log(data);

	var text = data;
	//var first = text.substring(69,71);
	//var second = text.substring(71,74);
	var first = 24;
	var second = 512;
	temperature = `${first}.${second}`;
	console.log("Temperature : " + temperature);
});

app.set('views', path.join(__dirname, 'views'));
app.set('view-engine', 'ejs');
app.set('port', 3000);

// 미들웨어 구성
app.use(morgan('dev'));		// 요청에 대한 정보를 콘솔에 기록
app.use(express.static(path.join(__dirname, 'public')));	// 정적파일(css, js등)의 위치지정
app.use(express.json());
app.use(express.urlencoded({extended: false}));

// 라우터 지정
app.use('/', mainpageRouter);

// 라우터 지정 이후 404에러가 들어오면 처리
app.use((req, res, next) => {
	const err = new Error('404 Not Found');
	err.status = 404;
	next(err);	// 에러 핸들러로 에러내용 전송
});

// 에러 핸들러
app.use((err, req, res) => {
	res.locals.message = err.message;
	res.locals.error = req.app.get('env') === 'development' ? err : {};
	res.status(err.status || 500);
	res.render('error');
});

const io = socketio.listen(server);
io.sockets.on('connection', function (socket){ 
    console.log('Socket ID : ' + socket.id + ', Connect');
    socket.on('reqMsg', function(data){
        console.log('Client Message : ' + data);
        com6.write(data);
        socket.emit('serverMessage', temperature);
    });
 });

server.listen(app.get('port'), () => {
	console.log('Server Start...!! Port : ', app.get('port'));
});