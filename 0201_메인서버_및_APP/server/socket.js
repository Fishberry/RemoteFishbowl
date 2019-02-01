const express = require('express'); 
const path = require('path');
const socketio = require('socket.io');
const fs = require('fs');
const router = express.Router();
/*
const serialPort = require('serialport');
const port = new serialPort('/dev/ttyACM0', {
	baudRate: 9600,
	dataBits: 8,
	parity: 'none',
	stopBits: 1,
	flowControl: false
});
*/
module.exports = (server, app) => {
    let temperature = 0.0;
    setInterval(() => {
    fs.readFile('/sys/bus/w1/devices/28-020d9246133d/w1_slave', 'utf8', (err, data) => {
        //console.log('읽어온 온도 : ', data);

        var text = data;
        var first = text.substring(69,71);
        var second = text.substring(71,74);
        //var first = 24;
        //var second = 512;
        temperature = `${first}.${second}`;
        console.log("Temperature : " + temperature);
    })}, 3000);

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
            socket.emit('serverMsg', temperature);
        });

	//App에서 아두이노를 동작시키기 위한 코드    
        socket.on('reqData', (data) => {
            console.log('app에서 받은 입력 : ', data);
	    fs.open('/dev/ttyACM0', 'a', 666, (e, fd) => {
		fs.write(fd, data, null, null, null, (err) => {
		    if(err) throw err;
		    console.log('ok!');	
		});
		fs.close(fd, function(){
	    	    console.log('bye');
	        });
	    });
        });
	   
        socket.on('disconnect', () => {
            console.log('연결 해제');
        });
    });
};
