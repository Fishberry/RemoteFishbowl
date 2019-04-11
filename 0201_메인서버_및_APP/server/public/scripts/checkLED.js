function startCheckLED(tty, fs, temperature, phValue, maxTemper, minTemper, maxPH, minPH) {
  // 수온과 수질 전부 이상이 있을 때 하얀색
  fs.open(tty, 'a', 666, (e, fd) => {
    if ((temperature >= maxTemper || temperature <= minTemper) && (phValue >= maxPH || phValue <= minPH)) {
      fs.write(fd, 'BothWN', null, (err) => {
        if (err) throw err;
        console.log('Temperature&pH Warnning!!!');
        fs.close(fd, (err) => {
          if (err) throw err;
        });
      });
    }

    // 수질만 이상 있을 때 보라색
    else if ((phValue >= maxPH || phValue <= minPH) && (temperature < maxTemper && temperature > minTemper)) {
      fs.write(fd, 'pHWN', null, (err) => {
        if (err) throw err;
        console.log('pH Warnning!!');
        fs.close(fd, (err) => {
          if (err) throw err;
        });
      });
    }

    // 수온만 이상 있을 때 빨간색
    else if ((temperature >= maxTemper || temperature <= minTemper) && (phValue < maxPH && phValue > minPH)) {
      fs.write(fd, 'TempWN', null, (err) => {
        if (err) throw err;
        console.log('Temperature Warnning!!');
        fs.close(fd, (err) => {
          if (err) throw err;
        });
      });
    }

    // 이상이 없을 때 초록색
    else if (temperature > minTemper && temperature < maxTemper && phValue > minPH && phValue < maxPH) {
      fs.write(fd, 'NotWN', null, (err) => {
        if (err) throw err;
        console.log('Not Warnning^-^');
        fs.close(fd, (err) => {
          if (err) throw err;
        });
      });
    }

    else {
      fs.write(fd, 'NotWN', null, (err) => {
        if (err) throw err;
        console.log('Not Warnning!!');
        fs.close(fd, (err) => {
          if (err) throw err;
        });
      });
    }
  });
}

module.exports = startCheckLED;
