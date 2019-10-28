const fishberryWrite = require('./fishberryWrite');

let getHour = 0, getMinute = 0, getSecond = 0;
let servoTimer = 0, servoCircle = 0;
var dt = new Date();

function startAutoFeed(connection, tty, fs) {
  connection.query('select * from FeedSetting', (error, results, fields) => {
    if (results[0].timer <= 0) {
      let dtdt = "\"" + dt.getFullYear() + '/' + (dt.getMonth() +1) + '/' + dt.getDate() + "\"";
      connection.query('select * from DailyValue where date='+dtdt, (error, results, fields) => {
	let feedvalue = results[0].feed;
	feedvalue = feedvalue+1;
        connection.query('update DailyValue set feed='+feedvalue+' where date=' + dtdt);
      });
      // 1회전으로 설정시 동작
      if (results[0].circle == '1') {
        fs.open(tty, 'a', 666, (e, fd) => {
          fishberryWrite.input(fd, 'StartServo1');
        });
      }

      // 2회전으로 설정시 동작
      else if (results[0].circle == '2') {
        fs.open(tty, 'a', 666, (e, fd) => {
          fishberryWrite.input(fd, 'StartServo2');
        });
      }

      // 3회전으로 설정시 동작
      else if (results[0].circle == '3') {
        fs.open(tty, 'a', 666, (e, fd) => {
          fishberryWrite.input(fd, 'StartServo3');
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
