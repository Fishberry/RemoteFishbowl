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
	if(file === 'ttyACM0' || file === 'ttyACM1' || file === 'ttyACM2') {
	    tty = '/dev/'+file;
	    console.log('connect tty : ', tty);
	}
    });
});

module.exports = (server, app) => {
    let temperature = 0.0;
    let phValue = 0.0;
    let minTemper = 0.0, maxTemper = 0.0, minPH = 0.0, maxPH = 0.0;

    let getHour = 0;
    let getMinute = 0;
    let getSecond = 0;

    let servoTimer = 0;
    let waterTimer1 = 0;
    let waterTimer2 = 0;

    let inWaterPercent = 0;
    let outWaterPercent = 0;
    let isChanged = false;
    let isBothWarnning = false;

    setInterval(() => {

    connection.query('select * from FeedSetting', (error, results, fields) => {
	if(results[0].timer <= 0) {
	    if(results[0].circle == '1') {
	    	// 여기에 지정된 시간 되면 서보모터 작동되도록 코드추가
	    	fs.open(tty, 'a', 666, (e, fd) => {
			fs.write(fd, 'StartServo1', null, null, null, (err) => {
			    if(err) throw err;
			    console.log('Servo1');	
			    fs.close(fd, (err) => {
	    		        console.log(err);
	      	      });
	    	    });
	   	 });
	    }

	    else if(results[0].circle == '2') {
	    	// 여기에 지정된 시간 되면 서보모터 작동되도록 코드추가
	    	fs.open(tty, 'a', 666, (e, fd) => {
			fs.write(fd, 'StartServo2', null, null, null, (err) => {
			    if(err) throw err;
			    console.log('Servo2');	
			    fs.close(fd, (err) => {
	    		        console.log(err);
	      	      });
	    	    });
	   	 });
	    }

	    else if(results[0].circle == '3') {
	    	// 여기에 지정된 시간 되면 서보모터 작동되도록 코드추가
	    	fs.open(tty, 'a', 666, (e, fd) => {
			fs.write(fd, 'StartServo3', null, null, null, (err) => {
			    if(err) throw err;
			    console.log('Servo3');	
			    fs.close(fd, (err) => {
	    		        console.log(err);
	      	      });
	    	    });
	   	 });
	    }

	    // 아래 28800으로 임의로 적은건 나중에 바꿀예정
	    connection.query('update FeedSetting set timer='+results[0].save_time, () => {});
	}
	if(results[0].timer > 0) {
	    servoTimer = results[0].timer-1;
	    connection.query('update FeedSetting set timer='+servoTimer, () => {});
    	    getHour = parseInt(servoTimer/60/60);
    	    getMinute = parseInt(servoTimer/60%60);
    	    getSecond = parseInt(servoTimer%60);
    	    console.log(getHour+"시간 "+getMinute+"분 "+getSecond+"초 뒤 먹이급여 시작");
	}
    });
    }, 1000);

    setInterval(() => {
	
    	connection.query('select * from ExchangeSetting', (error, results, fields) => {
		if(!isChanged) {
    			fs.readFile('/home/pi/Desktop/FishberryServer/background/arduino_log', 'utf8', (err, data) => {
				var text = data;
				if(text==results[0].exTime_save) isChanged = true;
			});
		}

		if(isChanged) {
	   		if(results[0].exTimer1 >= 0) {
				if(results[0].exTimer1 == 599) {
	    			fs.open(tty, 'a', 666, (e, fd) => {
					fs.write(fd, 'StartOUT', null, null, null, (err) => {
					    if(err) throw err;
					    console.log('StartOUT');	
					    fs.close(fd, (err) => {
	    				        console.log(err);
	      			      });
	    			    });
	   			});
				}
				else if(results[0].exTimer1 <= 0) {
	    			fs.open(tty, 'a', 666, (e, fd) => {
					fs.write(fd, 'StopOUT', null, null, null, (err) => {
					    if(err) throw err;
					    console.log('StopOUT');	
					    fs.close(fd, (err) => {
	    				        console.log(err);
	      			      });
	    			    });
	   			});
				}
				waterTimer1 = results[0].exTimer1 - 1;
	        		connection.query('update ExchangeSetting set exTimer1='+waterTimer1, () => {});
				console.log(waterTimer1);
				outWaterPercent = (1 - (waterTimer1/600))*100;
	   		}
	   		else if(results[0].exTimer1 < 0) {
				waterTimer2 = results[0].exTimer2 - 1;
	        		connection.query('update ExchangeSetting set exTimer2='+waterTimer2, () => {});
				console.log(waterTimer2);
				inWaterPercent = (1 - (waterTimer1/600))*100;

				if(results[0].exTimer2 == 599) {
	    			fs.open(tty, 'a', 666, (e, fd) => {
					fs.write(fd, 'StartIN', null, null, null, (err) => {
					    if(err) throw err;
					    console.log('StartIN');	
					    fs.close(fd, (err) => {
	    				        console.log(err);
	      			      });
	    			    });
	   			});
				}
				else if(results[0].exTimer2 < 0) {
	    				fs.open(tty, 'a', 666, (e, fd) => {
				  	    fs.write(fd, 'StopIN', null, null, null, (err) => {
					    	if(err) throw err;
					    	console.log('StopIN');	
					    	fs.close(fd, (err) => {
	    						console.log(err);
	      			 	    	});
	    				    });
	   				});
		    			waterTimer1 = 600;
		    			waterTimer2 = 600;
	        			connection.query('update ExchangeSetting set exTimer1='+waterTimer1+', exTimer2='+waterTimer2, () => {});
					isChanged = false;
				}
	   		}
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

    setInterval(() => {
	if ((temperature >= maxTemper || temperature <= minTemper) && (phValue >= maxPH || phValue <= minPH)) {
	   	fs.open(tty, 'a', 666, (e, fd) => {
	  	    fs.write(fd, 'BothWN', null, null, null, (err) => {
		    	if(err) throw err;
		    	console.log('Temperature&pH Warnning!!');	
		    	fs.close(fd, (err) => {
	    			console.log(err);
	      	    	});
	    	    });
	   	});
	}

	else if ((phValue >= maxPH || phValue <= minPH) && (temperature < maxTemper && temperature > minTemper)) {
	   	fs.open(tty, 'a', 666, (e, fd) => {
	  	    fs.write(fd, 'pHWN', null, null, null, (err) => {
		    	if(err) throw err;
		    	console.log('pH Warnning!!');	
		    	fs.close(fd, (err) => {
	    			console.log(err);
	      	    	});
	    	    });
	   	});
	}

	else if ((temperature >= maxTemper || temperature <= minTemper) && (phValue < maxPH && phValue > minPH)) {
	   	fs.open(tty, 'a', 666, (e, fd) => {
	  	    fs.write(fd, 'TempWN', null, null, null, (err) => {
		    	if(err) throw err;
		    	console.log('Temperature Warnning!!');	
		    	fs.close(fd, (err) => {
	    			console.log(err);
	      	    	});
	    	    });
	   	});
	}

	else if (temperature > minTemper && temperature < maxTemper && phValue > minPH && phValue < maxPH) {
	   	fs.open(tty, 'a', 666, (e, fd) => {
	  	    fs.write(fd, 'NotWN', null, null, null, (err) => {
		    	if(err) throw err;
		    	console.log('Not Warnning!!');	
		    	fs.close(fd, (err) => {
	    			console.log(err);
	      	    	});
	    	    });
	   	});
	}

    }, 2000);

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

	    connection.query('select * from TemperSetting', (error, results, fields) => {
	    	if(error)
			console.log(error);
		else {
			minTemper = results[0].min;
			maxTemper = results[0].max;
		}
	    });

	    connection.query('select * from PHSetting', (error, results, fields) => {
	    	if(error)
			console.log(error);
		else {
			minPH = results[0].min;
			maxPH = results[0].max;
		}
	    });

	    socket.emit('serverMsg', temperature, phValue, minTemper, maxTemper, minPH, maxPH);
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

		db.insertFeed(timer, circle, timer);

	});
	
	socket.on('insertWater', (timerWater) => {
		console.log('timerWater : ' + timerWater);

		db.insertExchange(600, 600, timerWater);
	});
	
	socket.on('WaterChangeConfirm', () => {
		console.log('현재 환수 작업도 : ' + waterPercent + '%');
		socket.emit('waterPercent', inWaterPercent, outWaterPercent);
	});
	   
        socket.on('disconnect', () => {
            console.log('연결 해제');
        });

    });
};
