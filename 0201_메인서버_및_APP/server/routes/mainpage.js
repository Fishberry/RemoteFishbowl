const express = require('express');
const router = express.Router();

const serialPort = require('serialport');
const arduinoPort = new serialPort('/dev/ttyACM0', {
	baudRate: 9600,
	dataBits: 8,
	parity: 'none',
	stopBits: 1,
	flowControl: false
});

router.get('/', (req, res) => {
	res.status(200).render('main.ejs');
});

router.get('/main/:id', (req, res) => {
	console.log(req.params.id);
	arduinoPort.write(req.params.id);
	res.status(200).send('Serial Controll OK!!');
});

module.exports = router;
