const isOn = false;

setInterval(() => {
    if (!isOn) {        //OFF면
        setLED(1);
    }
    else {              //ON이면
        setLED(0);
    }
}, 1000);       //1초마다 한번씩 본다.

function setLED(flag) {
    const fs = require('fs');

    //Appending 모드로 '/dev/ttyUSB0' 장치를 open
    fs.open('/dev/ttyUSB0', 'a', 666, (e, fd) => {
        //flag가 0이 아니면 1을 보내고
        //0이면 0을 보낸다.
        fs.write(fd, flag ? '1' : '0', null, null, null, () => {
            //1을 잘 보냈다면
            fs.close(fd, () => {    //연결을 종료한다.
                console.log('Sending ', flag);
            });
        });
    });
}