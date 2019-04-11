let waterTimer = 0, totalPercent = 0;
let waterTimer1 = 0, waterTimer2 = 0;

function startAutoExchange(connection, tty, fs) {
  connection.query('select * from ExchangeSetting', (error, results, fields) => {
    waterTimer = results[0].exTime_save;

    if (!results[0].isChanged) {
      fs.readFile('/home/pi/Desktop/FishberryServer/background/arduino_log', 'utf8', (err, data) => {
        if (err) throw err;
        var text = data.replace(/[^0-9/]/g, "");
	console.log("아두이노 시간 : " + text + " , 환수예약시간 : " + results[0].exTime_save);
        if (text === results[0].exTime_save) {
          connection.query('update ExchangeSetting set isChanged=true', () => { });
	  console.log("자동환수 시간맞았음");
        }
      });
    }

    if (results[0].isChanged) {
      fs.open(tty, 'a', 666, (e, fd) => {
        console.log('totalPercent : ' + totalPercent);
        if (results[0].exTimer1 > 0) {
          if (results[0].exTimer1 == 30) {
            fs.write(fd, 'StartOUT', null, (err) => {
              if (err) throw err;
              console.log('StartOUT');
              fs.close(fd, (err) => {
                //console.log(err);
              });
            });
          }

          else if (results[0].exTimer1 == 2) {
            fs.write(fd, 'StopOUT', null, (err) => {
              if (err) throw err;
              console.log('StopOUT');
              fs.close(fd, (err) => {
                //console.log(err);
              });
            });
          }

          waterTimer1 = results[0].exTimer1 - 1;
          totalPercent = parseInt((1 - (waterTimer1 / 32)) * 50);
          connection.query('update ExchangeSetting set exTimer1=' + waterTimer1 + ', totalPercent=' + totalPercent, () => { });
        }
        else {
          if (results[0].exTimer2 == 30) {
            fs.write(fd, 'StartIN', null, (err) => {
              if (err) throw err;
              console.log('StartIN');
              fs.close(fd, (err) => {
                //console.log(err);
              });
            });
            waterTimer2 = results[0].exTimer2 - 1;
            connection.query('update ExchangeSetting set exTimer2=' + waterTimer2, () => { });
          }
          else if (results[0].exTimer2 == 0) {
            fs.write(fd, 'StopIN', null, (err) => {
              if (err) throw err;
              console.log('StopIN');
              fs.close(fd, (err) => {
                //console.log(err);
              });
            });
            totalPercent = 0;
            waterTimer1 = 32;
            waterTimer2 = 32;
            results[0].isChanged = false;
            connection.query('update ExchangeSetting set exTimer1=' + waterTimer1 + ', exTimer2=' + waterTimer2 + ', isChanged=false, totalPercent = 0', () => { });
          }

          else {
            waterTimer2 = results[0].exTimer2 - 1;
            totalPercent = 50 + parseInt((1 - (waterTimer2 / 32)) * 50);
            connection.query('update ExchangeSetting set exTimer2=' + waterTimer2 + ', totalPercent=' + totalPercent, () => { });
          }
        }
      });
    }
  });
}

module.exports = startAutoExchange;
