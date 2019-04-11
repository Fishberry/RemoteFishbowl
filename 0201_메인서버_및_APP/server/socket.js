const socketio = require('socket.io');
const fs = require('fs');
const mysql = require('mysql');
const crypto = require('crypto');

const db = require('./models/findDB');

const autoFeed = require('./public/scripts/autofeed');
const autoExchange = require('./public/scripts/autoExchange');
const checkTemper = require('./public/scripts/checkTemper');
const checkPH = require('./public/scripts/checkPH');
const checkLED = require('./public/scripts/checkLED');

const fishberryWrite = require('./public/scripts/fishberryWrite');

const connection = mysql.createConnection({
  host: 'localhost',
  user: 'root',
  password: '1234',
  database: 'Fishberry'
});

connection.connect();

let tty = '';
fs.readdir('/dev', (err, data) => {
  if (err) { return done(err); }
  data.forEach((file) => {
    if (file === 'ttyACM0' || file === 'ttyACM1') {
      tty = '/dev/' + file;
      console.log('connect tty : ', tty);
    }
  });
});

module.exports = (server, app) => {
  let currentDate = '';
  let temperature = 0.0, phValue = 0.0;
  let minTemper = 0.0, maxTemper = 0.0, minPH = 0.0, maxPH = 0.0;

  let getTime = 0, getHour = 0, getMinute = 0, getSecond = 0;

  let servoTimer = 0, servoCircle = 0;
  let waterTimer = 0, waterTimer1 = 0, waterTimer2 = 0, totalPercent = 0;
  let isChanged = false;

  setInterval(() => {

    // 서버의 현재시간을 저장한다
    var dt = new Date();
    currentDate = "\"" + dt.getFullYear() + '/' + (dt.getMonth() + 1) + '/' + dt.getDate() + '/' + dt.getHours() + '/' + dt.getMinutes() + "\"";

    // 자동 먹이지급 관련 제어코드
    autoFeed(connection, tty, fs);
    connection.query('select * from FeedSetting', (error, results, fields) => {
      if (error)
        console.log(error);
      else {
        servoTimer = results[0].timer;
        servoCircle = results[0].circle;
      }
    });

    // 부분환수 관련 제어코드
    autoExchange(connection, tty, fs);
    connection.query('select * from ExchangeSetting', (error, results, fields) => {
      if (error)
        console.log(error);
      else {
        waterTimer = results[0].exTime_save;
        totalPercent = results[0].totalPercent;
        if (results[0].isChanged == '1') isChanged = true;
        else isChanged = false;
        //console.log("환수로그 :  isChanged, totalPercent = ", isChanged, totalPercent);
      }
    });
  }, 1000);


  setInterval(() => {
    connection.query('insert into DataRepository values(' + currentDate + ',' + temperature + ',' + phValue + ')', () => { });
  }, 1800000);

  // 5초마다 수온측정, 수질측정을 진행하고, 그에 따른 LED상태변화도 진행한다
  setInterval(() => {
    // 수온측정
    checkTemper.startCheckTemper(fs);
    connection.query('select * from TemperValue', (error, results, fields) => {
      if (error)
        console.log(error);
      else
        temperature = results[0].Tvalue;
    });

    // 수질측정
    checkPH.startCheckPH(fs);
    connection.query('select * from PHValue', (error, results, fields) => {
      if (error)
        console.log(error);
      else
        phValue = results[0].Pvalue;
    });

    console.log("온도: " + temperature + ", PH : " + phValue);

    // 수온 및 수질에 따른 아두이노 LED 제어
    checkLED(tty, fs, temperature, phValue, maxTemper, minTemper, maxPH, minPH);

  }, 5000);

  // 소켓통신 관련 코드
  const io = socketio.listen(server);

  io.sockets.on('connection', (socket) => {
    //const req = socket.request;
    //const ip = req.headers['x-forwarded-for'] || req.connection._remoteAddress;
    console.log('Socket ID : ', socket.id, ', Connect');

    socket.on('error', (error) => {
      console.error(error);
    });

    //App으로 온도 출력    
    socket.on('reqMsg', (data) => {
      //console.log('app에서 받은 메세지 : ', data);

      connection.query('select * from TemperSetting', (error, results, fields) => {
        if (error)
          console.log(error);
        else {
          minTemper = results[0].min;
          maxTemper = results[0].max;
        }
      });

      connection.query('select * from PHSetting', (error, results, fields) => {
        if (error)
          console.log(error);
        else {
          minPH = results[0].min;
          maxPH = results[0].max;
        }
      });

      socket.emit('serverMsg', temperature, phValue, minTemper, maxTemper, minPH, maxPH);
    });

    //App에서 아두이노를 동작시키기 위한 코드    
    socket.on('reqFeedNow', (data) => {
      console.log('app에서 받은 입력 : ', data); // data = StartServo1

      fs.open(tty, 'a', 666, (e, fd) => {
        fishberryWrite.input(e, fd, data);
      });
    });

    // App에 먹이지급 타이머와 회전수를 전송
    socket.on('reqTimerFeed', (data, err) => {
      if (err) throw err;
      //console.log('app에서 받은 입력 : ', data);
      socket.emit('resTimerFeed', servoCircle, servoTimer);
    });

    // App에 환수예약시간을 전송
    socket.on('reqTimerWater', (data, err) => {
      if (err) throw err;
      //console.log('app에서 받은 입력 : ', data);
      socket.emit('resTimerWater', waterTimer);
    });

    socket.on('reqChanged', (data, err) => {
      if (err) throw err;
      //console.log('app에서 받은 입력 : ', data);
      socket.emit('resChanged', isChanged);
    });

    // App에서 환수시작 버튼 처음 눌렀을 때 첫 시작
    socket.on('reqWaterNow', (data) => {
      console.log('app에서 받은 입력 : ', data); // data = StartOUT
      db.startExchange(true);
      fs.open(tty, 'a', 666, (e, fd) => {
        fishberryWrite.input(e, fd, data);
      });
    });

    // App으로 환수 진행률 전송
    socket.on('reqPercent', (data) => {
      socket.emit('resPercent', String(totalPercent));
    });

    // App에서 일시정지 버튼을 눌렀을 때
    socket.on('reqWaterNowPause', (data) => {
      console.log('app에서 받은 일시정지 메세지 : ', data);

      fs.open(tty, 'a', 666, (e, fd) => {
        db.startExchange(false);

        if (totalPercent < 50)
          fishberryWrite.input(e, fd, 'StopOUT');

        else
          fishberryWrite.input(e, fd, 'StopIN');
      });
    });

    // 환수 일시정지 후 다시시작 버튼을 눌렀을 때
    socket.on('reqWaterNowRestart', (data) => {
      console.log('app에서 받은 다시시작 메세지 : ', data);

      fs.open(tty, 'a', 666, (e, fd) => {
        db.startExchange(true);

        if (totalPercent < 50)
          fishberryWrite.input(e, fd, 'StartOUT');

        else
          fishberryWrite.input(e, fd, 'StartIN');
      });
    });

    // App에서 로그인 할 때 비밀번호 확인
    socket.on('confirmPassword', (data) => {

      connection.query('select * from passwordSetting', (error, results, fields) => {

        if (error) throw error;

        else {
          console.log(results);

          var decipher = crypto.createDecipher('aes256', 'password');
          decipher.update(results[0].password, 'hex', 'ascii');
          var decipherd = decipher.final('ascii');

          if (data == decipherd)
            socket.emit('passwordResult', "OK");
          else
            socket.emit('passwordResult', "NO");
        }
      });
    });

    // App에서 DB에 값 등록
    socket.on('insertTemper', (min, max) => {
      console.log('minTemper : ' + min);
      console.log('maxTemper : ' + max);

      db.insertTemper(min, max);
    });

    socket.on('insertPH', (min, max) => {
      console.log('minPH : ' + min);
      console.log('maxPH : ' + max);

      db.insertPH(min, max);
    });

    socket.on('insertFeed', (timer, circle) => {
      console.log('timer : ' + timer);
      console.log('circle : ' + circle);

      db.insertFeed(timer, circle, timer);

    });

    socket.on('insertWater', (timerWater) => {
      console.log('timerWater : ' + timerWater);

      db.insertExchange(32, 32, timerWater);
    });

    // App에서 nodejs로의 연결 해제
    socket.on('disconnect', () => {
      console.log('연결 해제');
    });

  });
};
