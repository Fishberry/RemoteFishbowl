const express = require('express');
const http = require('http');
const app = express();
const cookieParser = require('cookie-parser');
const path = require('path');
const server = http.createServer(app);
const morgan = require('morgan');

const mainpageRouter = require('./routes/mainpage');
const webSocket = require('./socket');

// 템플릿 엔진 및 views 폴더 지정
app.set('views', path.join(__dirname, 'views'));
app.set('view-engine', 'ejs');
app.set('port', 3000);		// 포트번호를 3000으로 지정

// 미들웨어 구성
app.use(morgan('dev'));		// 요청에 대한 정보를 콘솔에 기록
app.use(express.static(path.join(__dirname, 'public')));	// 정적파일(css, js등)의 위치지정
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());

// 라우터 지정
app.use('/', mainpageRouter);
// 웹 소켓 js파일에 데이터 전달
webSocket(server, app);

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

server.listen(app.get('port'), () => {
  console.log('Server Start...!! Port : ', app.get('port'));
});
