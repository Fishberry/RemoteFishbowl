const express = require('express'); 
const path = require('path');
const socketio = require('socket.io');
const fs = require('fs');
const mysql = require('mysql');
const crypto = require('crypto');
const db = require('./models/findDB');
const confirmPW = require('./confirmPassword');
const router = express.Router();

const connection = mysql.createConnection({
	host: 'localhost',
	user: 'root',
	password: '1234',
	database: 'Fishberry'
});

connection.connect();

let tty = '';
fs.readdir('/dev', (err, data) => {
    if(err) { return done(err); }
    data.forEach((file) => {
	if(file === 'ttyACM0' || file === 'ttyACM1') {
	    tty = '/dev/'+file;
	    console.log('connect tty : ', tty);
	}
    });
});

module.exports = (server, app) => {
    let temperature = 0.0;
    let phValue = 0.0;
    var waterArduinoReadValue;
    var waterValue = 0;

    let getHour = 0;
    let getMinute = 0;
    let getSecond = 0;

    let timer = 0;

    setInterval(() => {

    connection.query('select * from FeedSetting', (error, results, fields) => {
	if(results[0].timer <= 0) {
	    // 여기에 지정된 시간 되면 서보모터 작동되도록 코드추가
	    fs.open(tty, 'a', 666, (e, fd) => {
		fs.write(fd, 'StartServo', null, null, null, (err) => {
		    if(err) throw err;
		    console.log('ok!');	
		    fs.close(fd, (err) => {
	    	        console.log(err);
	            });
	        });
	    });

	    // 아래 28800으로 임의로 적은건 나중에 바꿀예정
	    connection.query('update FeedSetting set timer=28800', () => {});
	}
	if(results[0].timer > 0) {
	    timer = results[0].timer-1;
	    connection.query('update FeedSetting set timer='+timer, () => {});
    	    getHour = parseInt(timer/60/60);
    	    getMinute = parseInt(timer/60%60);
    	    getSecond = parseInt(timer%60);
    	    console.log(getHour+"시간 "+getMinute+"분 "+getSecond+"초 뒤 먹이급여 시작");
	}
    });
    }, 1000);

    setInterval(() => {
    fs.readFile('/sys/bus/w1/devices/28-020d9246133d/w1_slave', 'utf8', (err, data) => {
        //console.log('읽어온 온도 : ', data);a // 전체 파일의 내용을 로그찍음

        var text = data;
        var first = text.substring(69,71);
        var second = text.substring(71,74);

        temperature = `${first}.${second}`;
        console.log("Temperature : " + temperature);
    })}, 5000);

    setInterval(() => {
    fs.readFile('/home/pi/Desktop/FishberryServer/background/pH_log', 'utf8', (err, data) => {

        var text = data;
        var ph = text.substring(0,4);
        phValue = `${ph}`;
        console.log("phValue : " + phValue);
    })}, 5000);

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
            console.log('app에서 받은 메세지 : ', data);
            socket.emit('serverMsg', temperature, phValue);
        });

        socket.on('reqTime', (data) => {
            console.log('app에서 받은 시간요청 메세지 : ', data);
            socket.emit('resTime', getHour, getMinute, getSecond);
        });

	//App에서 아두이노를 동작시키기 위한 코드    
        socket.on('reqData', (data) => {
            console.log('app에서 받은 입력 : ', data);
	    
	    if(data == 'StartWater')
		waterValue = 1;

	    fs.open(tty, 'a', 666, (e, fd) => {
		fs.write(fd, data, null, null, null, (err) => {
		    if(err) throw err;
		    console.log('ok!');	
		    fs.close(fd, (err) => {
	    	        console.log(err);
	            });
	        });
	    });
        });

	socket.on('confirmPassword', (data) => {
		console.log('data : ' + data);
		
		connection.query('select * from passwordSetting', (error, results, fields) => {
			if(error)
				console.log(error);
			else {
				console.log(results);

				var decipher = crypto.createDecipher('aes256', 'password');
				decipher.update(results[0].password, 'hex', 'ascii');
				var decipherd = decipher.final('ascii');

				if(data == decipherd)
					socket.emit('passwordResult', "OK");
				else
					socket.emit('passwordResult', "NO");
			}
		});
	});

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

		db.insertFeed(timer, circle);

	});
	   
        socket.on('disconnect', () => {
            console.log('연결 해제');
        });

    });
};
