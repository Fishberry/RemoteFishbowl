const fishberryWrite = require('./fishberryWrite');

function startCheckLED(tty, fs, temperature, phValue, maxTemper, minTemper, maxPH, minPH) {
  // 수온과 수질 전부 이상이 있을 때 하얀색
  fs.open(tty, 'a', 666, (e, fd) => {
    if ((temperature >= maxTemper || temperature <= minTemper) && (phValue >= maxPH || phValue <= minPH)) {
      fishberryWrite.input(fd, 'BothWN', 'Temperature&pH Warnning!!!');
    }

    // 수질만 이상 있을 때 보라색
    else if ((phValue >= maxPH || phValue <= minPH) && (temperature < maxTemper && temperature > minTemper)) {
      fishberryWrite.input(fd, 'pHWN', 'pH Warnning!!');
    }

    // 수온만 이상 있을 때 빨간색
    else if ((temperature >= maxTemper || temperature <= minTemper) && (phValue < maxPH && phValue > minPH)) {
      fishberryWrite.input(fd, 'TempWN', 'Temperature Warnning!!');
    }

    // 이상이 없을 때 초록색
    else if (temperature > minTemper && temperature < maxTemper && phValue > minPH && phValue < maxPH) {
      fishberryWrite.input(fd, 'NotWN', 'Not Warnning^-^');
    }

    else {
      fishberryWrite.input(fd, 'NotWN', 'Not Warnning!!');
    }
  });
}

module.exports = startCheckLED;
