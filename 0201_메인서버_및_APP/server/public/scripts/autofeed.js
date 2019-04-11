let getHour = 0, getMinute = 0, getSecond = 0;
let servoTimer = 0, servoCircle = 0;

function startAutoFeed(connection, tty, fs) {
  connection.query('select * from FeedSetting', (error, results, fields) => {
    if (results[0].timer <= 0) {
      // 1회전으로 설정시 동작
      if (results[0].circle == '1') {
        fs.open(tty, 'a', 666, (e, fd) => {
          fs.write(fd, 'StartServo1', null, (err) => {
            if (err) throw err;
            console.log('Servo1');
            fs.close(fd, (err) => {
              //console.log(err);
            });
          });
        });
      }

      // 2회전으로 설정시 동작
      else if (results[0].circle == '2') {
        fs.open(tty, 'a', 666, (e, fd) => {
          fs.write(fd, 'StartServo2', null, (err) => {
            if (err) throw err;
            console.log('Servo2');
            fs.close(fd, (err) => {
              //console.log(err);
            });
          });
        });
      }

      // 3회전으로 설정시 동작
      else if (results[0].circle == '3') {
        fs.open(tty, 'a', 666, (e, fd) => {
          fs.write(fd, 'StartServo3', null, (err) => {
            if (err) throw err;
            console.log('Servo3');
            fs.close(fd, (err) => {
              //console.log(err);
            });
          });
        });
      }

      connection.query('update FeedSetting set timer=' + results[0].save_time, () => { });
    }

    // 타이머가 1초씩 떨어지도록 구성
    if (results[0].timer > 0) {
      servoTimer = results[0].timer - 1;
      servoCircle = results[0].circle;
      connection.query('update FeedSetting set timer=' + servoTimer, () => { });
      getHour = parseInt(servoTimer / 60 / 60);
      getMinute = parseInt(servoTimer / 60 % 60);
      getSecond = parseInt(servoTimer % 60);
      console.log(getHour + "시간 " + getMinute + "분 " + getSecond + "초 뒤 먹이급여 시작");
      //console.log(maxTemper + " " + minTemper + " " + maxPH + " " + minPH); //값 테스트
    }
  });
}

module.exports = startAutoFeed;
