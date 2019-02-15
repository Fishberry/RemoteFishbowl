const express = require('express');
const fs = require('fs');
const router = express.Router();
const serialPort = require('serialport');
const http = require('http');
const url = require('url');
const mysql = require('mysql');
const db = require('../findDB');
const connection = mysql.createConnection({
	host: 'localhost',
	user: 'root',
	password: '1234',
	database: 'Fishberry'
});
connection.connect();

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

router.get('/water', (req, res) => {
	res.status(200).render('water.ejs');
});

router.get('/feed', (req, res) => {
	res.status(200).render('feed.ejs');
});

router.get('/exchange', (req, res) => {
	res.status(200).render('exchange.ejs');
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

router.get('/inputValue', (req, res) => {
	const _url = req.url;
	const queryData = url.parse(_url, true).query;
	db.insertTemper(queryData.minTemper, queryData.maxTemper);
	db.insertPH(queryData.minPH, queryData.maxPH);
	res.status(200).render('water.ejs');
});

module.exports = router;
