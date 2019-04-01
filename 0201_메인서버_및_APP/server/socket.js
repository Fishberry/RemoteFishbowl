const express = require('express'); 
const path = require('path');
const socketio = require('socket.io');
const fs = require('fs');
const mysql = require('mysql');
const crypto = require('crypto');
const db = require('./models/findDB');
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
    let minTemper = 0.0, maxTemper = 0.0, minPH = 0.0, maxPH = 0.0;

    let getHour = 0;
    let getMinute = 0;
    let getSecond = 0;

    let servoTimer = 0;
    let servoCircle = 0;
    let waterTimer = 0;
    let waterTimer1 = 0;
    let waterTimer2 = 0;

    let inWaterPercent = 0;
    let outWaterPercent = 0;
    let totalPercent = 0;
    let isChanged = false;
    let isBothWarnning = false;

    // 자동 먹이지급 관련 제어코드
    setInterval(() => {

      connection.query('select * from FeedSetting', (error, results, fields) => {
	if(results[0].timer <= 0) {
	    // 1회전으로 설정시 동작
	    if(results[0].circle == '1') {
	   	fs.open(tty, 'a', 666, (e, fd) => {
	  	    fs.write(fd, 'StartServo1', null, (err) => {
		    	if(err) throw err;
			console.log('Servo1');	
		    	fs.close(fd, (err) => {
	    			//console.log(err);
	      	    	});
	    	    });
	   	});
	    }

	    // 2회전으로 설정시 동작
	    else if(results[0].circle == '2') {
	    	fs.open(tty, 'a', 666, (e, fd) => {
	 	    fs.write(fd, 'StartServo2', null, (err) => {
		        if(err) throw err;
		        console.log('Servo2');	
		        fs.close(fd, (err) => {
	                    //console.log(err);
	      	        });
	    	    });
	   	 });
	    }

	    // 3회전으로 설정시 동작
	    else if(results[0].circle == '3') {
	    	fs.open(tty, 'a', 666, (e, fd) => {
		    fs.write(fd, 'StartServo3', null, (err) => {
		       if(err) throw err;
		       console.log('Servo3');	
		       fs.close(fd, (err) => {
	                   //console.log(err);
	      	       });
	    	    });
	   	 });
	    }

	    connection.query('update FeedSetting set timer='+results[0].save_time, () => {});
	}

	// 타이머가 1초씩 떨어지도록 구성
	if(results[0].timer > 0) {
	    servoTimer = results[0].timer-1;
	    servoCircle = results[0].circle;
	    connection.query('update FeedSetting set timer='+servoTimer, () => {});
    	    getHour = parseInt(servoTimer/60/60);
    	    getMinute = parseInt(servoTimer/60%60);
    	    getSecond = parseInt(servoTimer%60);
    	    console.log(getHour+"시간 "+getMinute+"분 "+getSecond+"초 뒤 먹이급여 시작");
	    //console.log(maxTemper + " " + minTemper + " " + maxPH + " " + minPH); //값 테스트
	}
      });
    }, 1000);

    // 부분환수 관련 제어코드
    setInterval(() => {
	
    	connection.query('select * from ExchangeSetting', (error, results, fields) => {
		waterTimer = results[0].exTime_save;

		if(!isChanged) {
    			fs.readFile('/home/pi/Desktop/FishberryServer/background/arduino_log', 'utf8', (err, data) => {
				if(err) throw err;
				var text = data.replace(/[^0-9/]/g, "");
				if(text===results[0].exTime_save) 
				{
					isChanged = true;
				}
			});
		}

		if(isChanged) {
	    	    fs.open(tty, 'a', 666, (e, fd) => {
			console.log('totalPercent : ' + totalPercent);
	   		if(results[0].exTimer1 > 0) {
				if(results[0].exTimer1 === 30) {
					fs.write(fd, 'StartOUT', null, (err) => {
					    if(err) throw err;
					    console.log('StartOUT');	
					    fs.close(fd, (err) => {
	    				        //console.log(err);
	      			      });
	    			    });
				}

				else if(results[0].exTimer1 === 2) {
					fs.write(fd, 'StopOUT', null, (err) => {
					    if(err) throw err;
					    console.log('StopOUT');	
					    fs.close(fd, (err) => {
	    				        //console.log(err);
	      			      });
	    			    });
				}

				waterTimer1 = results[0].exTimer1 - 1;
	        		connection.query('update ExchangeSetting set exTimer1='+waterTimer1, () => {});
				totalPercent = parseInt((1 - (waterTimer1/32))*50);
	   		}
	   		else {
				if(results[0].exTimer2 === 30) {
						fs.write(fd, 'StartIN', null, (err) => {
					    	    if(err) throw err;
						    console.log('StartIN');	
						    fs.close(fd, (err) => {
	    					        //console.log(err);
	      				      	    });
	    				    	});
					waterTimer2 = results[0].exTimer2 - 1;
	        			connection.query('update ExchangeSetting set exTimer2='+waterTimer2, () => {});
				}
				else if(results[0].exTimer2 === 0) {
				  	    fs.write(fd, 'StopIN', null, (err) => {
					        if(err) throw err;
					    	console.log('StopIN');	
					    	fs.close(fd, (err) => {
	    						//console.log(err);
	      			 	    	});
	    				    });
					totalPercent = 0;
		    			waterTimer1 = 32;
		    			waterTimer2 = 32;
	        			connection.query('update ExchangeSetting set exTimer1='+waterTimer1+', exTimer2='+waterTimer2, () => {});
					isChanged = false;
				}

				else {
					waterTimer2 = results[0].exTimer2 - 1;
	        			connection.query('update ExchangeSetting set exTimer2='+waterTimer2, () => {});
					totalPercent = 50 + parseInt((1 - (waterTimer2/32))*50);
				}
	   		}
		    });
		}
    	});
	
    }, 1000);


    // 5초마다 수온측정, 수질측정을 진행하고, 그에 따른 LED상태변화도 진행한다
    setInterval(() => {
	fs.access('/sys/bus/w1/devices/28-020d9246133d/w1_slave',fs.constants.F_OK, (err) => {
	    if(err) {
	        temperature = '-999';
	        console.log("온도 읽어올 수 없음.");
	    }
	
	    else {
                fs.readFile('/sys/bus/w1/devices/28-020d9246133d/w1_slave', 'utf8', (err, data) => {

                    //console.log('읽어온 온도 : ', data); // 전체 파일의 내용을 로그찍음

	    	    if(err) throw err;
                    var text = data;
                    var first = text.substring(69,71);
                    var second = text.substring(71,72);

                    temperature = `${first}.${second}`;
                    console.log("Temperature : " + temperature);

                });
	    }
	});

        fs.readFile('/home/pi/Desktop/FishberryServer/background/pH_log', 'utf8', (err, data) => {
	    if(err) throw err;
            var text = data;
            var ph = text.substring(0,4);
            phValue = `${ph}`;
            console.log("phValue : " + phValue);
        });

	// 수온 및 수질에 따른 아두이노 LED 제어
	// 수온과 수질 전부 이상이 있을 때 하얀색
      fs.open(tty, 'a', 666, (e, fd) => {
	if ((temperature >= maxTemper || temperature <= minTemper) && (phValue >= maxPH || phValue <= minPH)) {
	  	    fs.write(fd, 'BothWN', null, (err) => {
		    	if(err) throw err;
		    	console.log('Temperature&pH Warnning!!');	
		    	fs.close(fd, (err) => {
		    	    if(err) throw err;
	      	    	});
	    	    });
	}

	// 수질만 이상 있을 때 보라색
	else if ((phValue >= maxPH || phValue <= minPH) && (temperature < maxTemper && temperature > minTemper)) {
	  	    fs.write(fd, 'pHWN', null, (err) => {
		    	if(err) throw err;
		    	console.log('pH Warnning!!');	
		    	fs.close(fd, (err) => {
		    	    if(err) throw err;
	      	    	});
	    	    });
	}

	// 수온만 이상 있을 때 빨간색
	else if ((temperature >= maxTemper || temperature <= minTemper) && (phValue < maxPH && phValue > minPH)) {
	  	    fs.write(fd, 'TempWN', null, (err) => {
		    	if(err) throw err;
		    	console.log('Temperature Warnning!!');	
		    	fs.close(fd, (err) => {
		    	    if(err) throw err;
	      	    	});
	    	    });
	}

	// 이상이 없을 때 초록색
	else if (temperature > minTemper && temperature < maxTemper && phValue > minPH && phValue < maxPH) {
	  	    fs.write(fd, 'NotWN', null, (err) => {
		    	if(err) throw err;
		    	console.log('Not Warnning!!');	
		    	fs.close(fd, (err) => {
	    		    console.log(err);
		    	    if(err) throw err;
	      	    	});
	    	    });
	}

	else {
	  	    fs.write(fd, 'NotWN', null, (err) => {
		    	if(err) throw err;
		    	console.log('Not Warnning!!');	
		    	fs.close(fd, (err) => {
		    	    if(err) throw err;
	      	    	});
	    	    });
	}
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

        socket.on('reqTime', (data, err) => {
	    if(err) throw err;
            console.log('app에서 받은 시간요청 메세지 : ', data);
            socket.emit('resTime', getHour, getMinute, getSecond);
        });

	//App에서 아두이노를 동작시키기 위한 코드    
        socket.on('reqFeedNow', (data) => {
            console.log('app에서 받은 입력 : ', data); // data = StartServo1
	    
	    fs.open(tty, 'a', 666, (e, fd) => {
		fs.write(fd, data, null, (err) => {
		    if(err) throw err;
		    console.log('ok!');	
		    fs.close(fd, (err) => {
	    	        //console.log(err);
	            });
	        });
	    });
        });

	// App에 먹이지급 타이머와 회전수를 전송
	socket.on('reqTimerFeed', (data, err) => {
	    if(err) throw err;
	    console.log('app에서 받은 입력 : ', data);
	    socket.emit('resTimerFeed', servoCircle, servoTimer);
	});

	// App에 환수예약시간을 전송
	socket.on('reqTimerWater', (data, err) => {
	    if(err) throw err;
	    console.log('app에서 받은 입력 : ', data);
	    socket.emit('resTimerWater', waterTimer);
	});

	socket.on('reqChanged', (data, err) => {
	    if(err) throw err;
	    console.log('app에서 받은 입력 : ', data);
	    socket.emit('resChanged', isChanged);
	});

	// App에서 환수시작 버튼 처음 눌렀을 때 첫 시작
        socket.on('reqWaterNow', (data) => {
            console.log('app에서 받은 입력 : ', data); // data = StartOUT
	    isChanged = true;
	    fs.open(tty, 'a', 666, (e, fd) => {
		fs.write(fd, data, null, (err) => {
		    if(err) throw err;
		    console.log('ok!');	
		    fs.close(fd, (err) => {
	    	        //console.log(err);
	            });
	        });
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
	        isChanged = false;

		if(totalPercent < 50) {
		fs.write(fd, 'StopOUT', null, (err) => {
		    if(err) throw err;
		    fs.close(fd, (err) => {
	    	        //console.log(err);
	            });
	        });
		}

		else {
		fs.write(fd, 'StopIN', null, (err) => {
		    if(err) throw err;
		    fs.close(fd, (err) => {
	    	        //console.log(err);
	            });
	        });
		}

	    });
        });

	// 환수 일시정지 후 다시시작 버튼을 눌렀을 때
        socket.on('reqWaterNowRestart', (data) => {
            console.log('app에서 받은 다시시작 메세지 : ', data);

	    fs.open(tty, 'a', 666, (e, fd) => {
	        isChanged = true;

		if(totalPercent < 50) {
		fs.write(fd, 'StartOUT', null, (err) => {
		    if(err) throw err;
		    console.log('StartOUT ok!');	
		    fs.close(fd, (err) => {
	    	        //console.log(err);
	            });
	        });
		}

		else {
		fs.write(fd, 'StartIN', null, (err) => {
		    if(err) throw err;
		    console.log('StartIN ok!');	
		    fs.close(fd, (err) => {
	    	        //console.log(err);
	            });
	        });
		}

	    });
        });

	// App에서 로그인 할 때 비밀번호 확인
	socket.on('confirmPassword', (data) => {
		
		connection.query('select * from passwordSetting', (error, results, fields) => {

			if(error) throw error;

			else 
			{
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
