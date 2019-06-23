const socketio = require('socket.io');
const fs = require('fs');
const mysql = require('mysql');
const crypto = require('crypto');
const os = require('os');
const utils = require('node-os-utils');
const cpu = utils.cpu;

const db = require('./models/findDB');
var exec = require('child_process').exec, child;

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
  let currentDay = '';
  let current12 = '';
  let temperature = 24.0, phValue = 7.0;
  let minTemper = 18.0, maxTemper = 24.0, minPH = 6.0, maxPH = 9.0;
  let minTemp_day = 0.0, maxTemp_day = 0.0, minPH_day = 0.0, maxPH_day = 0.0, feed_day=0;
  let Temp3 = 0.0, PH3 = 0.0, feed3 = 0;

  let servoTimer = 0, servoCircle = 0;
  let waterTimer = 0, totalPercent = 0;
  let isChanged = false;
  let cpuinfo = '';

  setInterval(() => {

    // 서버의 현재시간을 저장한다
    var dt = new Date();
    currentDate = "\"" + dt.getFullYear() + '/' + (dt.getMonth() + 1) + '/' + dt.getDate() + '/' + dt.getHours() + '/' + dt.getMinutes() + "\"";
    currentDate2 = "\"" + dt.getFullYear() + '/' + (dt.getMonth() + 1) + '/' + dt.getDate() + '/' + dt.getHours() + "\"";
    currentDay = "\"" + dt.getFullYear() + '/' + (dt.getMonth() + 1) + '/' + dt.getDate() + "\"";

    current12 = "\"" + dt.getHours() + '/' + dt.getMinutes() + "\"";

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

  // 3시간마다 온도, ph, 먹이지급여부 기록
  setInterval(() => {
    var dt = new Date();
    if(dt.getHours()==0||dt.getHours()==3||dt.getHours()==6||dt.getHours()==9||dt.getHours()==12||dt.getHours()==15||dt.getHours()==18||dt.getHours()==21) {
	connection.query('insert into Hour3Value values(' + currentDate2 + ',' + temperature + ',' + phValue + ',' + feed_day + ')', () => { });
    }
  }, 3600000);

  // 1분마다 DailyValue DB에 최대온도 등의 값들을 update하고, 12시에 새로운 값 추가
  setInterval(() => {
      connection.query('select * from DailyValue', (error, results, fields) => {
	if(current12=="\"0/0\"") connection.query('insert into DailyValue value (' + currentDay + ',' + temperature + ',' + temperature + ',' + phValue + ',' + phValue + ', 0, NULL)');
	for(var i=0; i<results.length; i++) {
  	  if(("\"" + results[i].date + "\"" )== currentDay) {
	    if(results[i].maxTemp < temperature) connection.query('update DailyValue set maxTemp='+temperature+' where date='+currentDay);
	    if(results[i].minTemp > temperature) connection.query('update DailyValue set minTemp='+temperature+' where date='+currentDay);
	    if(results[i].maxPH < phValue) connection.query('update DailyValue set maxPH='+phValue+' where date='+currentDay);
	    if(results[i].minPH > phValue) connection.query('update DailyValue set minPH='+phValue+' where date='+currentDay);
	  }
	maxTemp_day = results[i].maxTemp;
	minTemp_day = results[i].minTemp;
	maxPH_day = results[i].maxPH;
	minPH_day = results[i].minPH;
	feed_day = results[i].feed;
	}
      });
    
  }, 60000);

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
    cpu.usage().then(info=> {
	cpuinfo = info;
    });

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
        fishberryWrite.input(fd, data);
      });
    });

    socket.on('reqDailyValue', (data, err) => {
      connection.query('select * from DailyValue where date=' + data, (error, results, fields) => {
        if (error) {
          console.log(error);
          console.log("reqDailyValue에서 오류!");
	}
        else {
          socket.emit('resDailyValue', results[0].maxTemp, results[0].minTemp, results[0].maxPH, results[0].minPH, results[0].feed, data);
		/*
	  console.log('app에서 받은 입력 : ', data); // data = StartServo1
          console.log('app에서 받은 입력 : ', results[0].maxTemp); // data = StartServo1
          console.log('app에서 받은 입력 : ', results[0].minTemp); // data = StartServo1
          console.log('app에서 받은 입력 : ', results[0].maxPH); // data = StartServo1
          console.log('app에서 받은 입력 : ', results[0].minPH); // data = StartServo1
          console.log('app에서 받은 입력 : ', results[0].feed); // data = StartServo1
	  */
        }
      });
      //socket.emit(minTemp_day, maxTemp_day, minPH_day, maxPH_day, feed_day);
    });

    socket.on('reqDailyValueCount', (data, err) => {
      connection.query('select count(*) AS count from DailyValue', (error, results, fields) => {
        if (error)
          console.log(error);
        else {
          socket.emit('resDailyValueCount', results[0].count);
        }
      });
    });

    socket.on('reqDaily3HourValue', (data, err) => {
      console.log('app에서 받은 입력(hour3) : ', data);
      connection.query('select * from Hour3Value where date like "2019/' + data + '%"', (error, results, fields) => {
      //connection.query('select * from Hour3Value where date like "2019/6/22%"', (error, results, fields) => {
        if (error) {
          console.log(error);
      	  console.log('app에서 받은 입력(hour3) : ', data);
	}
        else {
	  var daily3HourValues = new Array();
	  for(var i=0; i<results.length; i++) {
		  daily3HourValues[i] = results[i].Temp;
	  }
	  for(var i=0; i<results.length; i++) {
		  daily3HourValues[results.length + i] = results[i].PH;
	  }
          socket.emit('resDaily3HourValue', daily3HourValues, maxTemper, minTemper, maxPH, minPH); 
          //socket.emit('resDaily3HourValue', results[0].Temp, results[1].Temp, results[2].Temp, results[3].Temp, results[4].Temp, results[5].Temp, results[6].Temp, results[7].Temp, results[8].Temp, results[0].PH, results[1].PH, results[2].PH, results[3].PH, results[4].PH, results[5].PH, results[6].PH, results[7].PH, results[8].PH); 
          //console.log('보내는 입력(hour3) : ', data, results[0].Temp, results[1].Temp, results[2].Temp, results[3].Temp, results[4].Temp, results[5].Temp, results[6].Temp, results[7].Temp, results[8].Temp, results[0].PH, results[1].PH, results[2].PH, results[3].PH, results[4].PH, results[5].PH, results[6].PH, results[7].PH, results[8].PH);
        }
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
        fishberryWrite.input(fd, data);
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
          fishberryWrite.input(fd, 'StopOUT');

        else
          fishberryWrite.input(fd, 'StopIN');
      });
    });

    // 환수 일시정지 후 다시시작 버튼을 눌렀을 때
    socket.on('reqWaterNowRestart', (data) => {
      console.log('app에서 받은 다시시작 메세지 : ', data);

      fs.open(tty, 'a', 666, (e, fd) => {
        db.startExchange(true);

        if (totalPercent < 50)
          fishberryWrite.input(fd, 'StartOUT');

        else
          fishberryWrite.input(fd, 'StartIN');
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

    socket.on('changePassword', (previewPw, forwardPw) => {
      
      connection.query('select * from passwordSetting', (error, results, fields) => {
      	
	if (error) throw error;

	else {
	  console.log(results);

	  var decipher = crypto.createDecipher('aes256', 'password');
	  decipher.update(results[0].password, 'hex', 'ascii');
	  var decipherd = decipher.final('ascii');

	  if (decipherd == previewPw) {
	    var cipher = crypto.createCipher('aes256', 'password');
	    cipher.update(forwardPw, 'ascii', 'hex');
	    var cipherd = cipher.final('hex');
	    connection.query("update passwordSetting set password='" + cipherd + "';");

	    socket.emit('changePasswordResult', 1);
	  }
	  else
	    socket.emit('changePasswordResult', 0);
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

      db.insertExchange(30, 30, timerWater);
    });

    // App에서 nodejs로의 연결 해제
    socket.on('disconnect', () => {
      console.log('연결 해제');
    });

    socket.on('reqOS', (data, err) => {
        if (err) throw err;
        //console.log('app에서 받은 입력 : ', data);
        var version = os.hostname() + os.release();
        socket.emit('resOS', version, os.freemem(), os.totalmem(), cpuinfo);
    });

    socket.on('reqReboot', () => {
      console.log('서버 재부팅');
      child = exec("sudo reboot", function(error) {
	      if(error != null) {
		      console.log('exec error: ' + error);
	      }
      });
    });

    socket.on('reqHalt', () => {
      console.log('서버 종료');
      child = exec("sudo halt", function(error) {
	      if(error != null) {
		      console.log('exec error: ' + error);
	      }
      });
    });

  });
};
