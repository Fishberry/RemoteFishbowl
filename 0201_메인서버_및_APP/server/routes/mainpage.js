const express = require('express');
const fs = require('fs');
const router = express.Router();
const serialPort = require('serialport');
let arduinoPort = '';
let tty = '';
fs.readdir('/dev', (err, data) => {
    if(err) { return done(err); }
    data.forEach((file) => {
	if(file === 'ttyACM0' || file === 'ttyACM1') {
	    tty = '/dev/'+file;
	    console.log('connect tty : ', tty);
	    arduinoPort = new serialPort(tty, {
		baudRate: 9600,
		dataBits: 8,
		parity: 'none',
		stopBits: 1,
		flowControl: false
	    });
	}
    });
});
router.get('/', (req, res) => {
	res.status(200).render('main.ejs');
});

router.get('/main/StartServo', (req, res) => {
	console.log('서보모터 동작');
	arduinoPort.write('StartServo');
	res.status(200).send('Serial Controll OK!!');
});

router.get('/main/StartWater', (req, res) => {
	console.log('워터펌프  동작');
	arduinoPort.write('StartWater');
	res.status(200).send('Serial Controll OK!!');
});

module.exports = router;
