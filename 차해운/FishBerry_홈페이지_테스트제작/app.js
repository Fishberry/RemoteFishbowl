var express = require('express');
var path = require('path');
var cookieParser = require('cookie-parser');
var logger = require('morgan');

// 라우터 모듈 불러오기
var indexRouter = require('./routes/index');
var usersRouter = require('./routes/users');

var app = express();
// 시리얼 포트 설정
// COMxx : 아두이노가 연결된 xx번 포트
// 시리얼포트 사용을 위해 npm --save install serialport 설치를 진행해야 한다
// 설치오류가 나면 파이썬 2버전을 설치해야 하므로 npm install -g windows-build-tools 명령을 실행한다
var serialPort  = require('serialport');
var com55 = new serialPort('COM55',{
  baudRate : 9600,
  dataBits : 8,
  parity : 'none',
  stopBits: 1,
  flowControl: false
});
com55.on('open', () => {
  console.log('open serial communication');
});

// view engine setup
app.set('views', path.join(__dirname, 'views'));  // view에 대한 폴더는 views로 지정
app.set('view engine', 'ejs');  // 템플릿 엔진은 EJS 사용
app.set('port', process.env.PORT || 8001);

// 미들웨어 구성
app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

app.use('/', indexRouter);
app.use('/users', usersRouter);

// 라우터 이후에 404 에러 처리
app.use((req, res, next) => {
  const err = new Error('Not Found');
  err.status = 404;
  next(err);
});

// 에러 핸들러
app.use((err, req, res) => {
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};
  res.status(err.status || 500);
  res.render('error');
});

// 서버를 실행시킨 후 클라이언트 대기
app.listen(app.get('port'), () => {
  console.log(app.get('port'), '번 포트에서 대기 중...');
});
