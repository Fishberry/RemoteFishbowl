const express = require('express');
const fs = require('fs');
const router = express.Router();
const serialPort = require('serialport');
const http = require('http');
const url = require('url');
const mysql = require('mysql');
const crypto = require('crypto');
const db = require('../models/findDB');

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
	if(file === 'ttyACM0' || file === 'ttyACM1' || file === 'ttyACM2') {
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
	var cookieValue = req.cookies.password;
	if(cookieValue == null)
		res.status(200).render('passwordPage.ejs');
	else
		res.status(200).render('main.ejs');
});

router.post('/password', (req, res) => {
	connection.query('select * from passwordSetting', (error, results, fields) => {
		if(error)
			console.log(error);
		else {
			var cipher = crypto.createCipher('aes256', 'password');
			cipher.update(req.body.PASSWORD, 'ascii', 'hex');
			var cipherd = cipher.final('hex');
	
			if(cipherd == results[0].password) {
				res.cookie('password', req.body.PASSWORD, {
					maxAge: 1800000
				});

				res.status(200).render('main.ejs');
			}
			else
				res.send('<script type="text/javascript">alert("비밀번호가 올바르지 않습니다."); history.back();</script>');
		}
	});
});

router.post('/setPassword', (req, res) => {
	var cookieValue = req.cookies.password;
	if(cookieValue == null)
		res.status(200).render('passwordPage.ejs');
	else {
	connection.query('select * from passwordSetting', (error, results, fields) => {
		if(error)
			console.log(error);
		else {
			console.log('before password : ' + req.body.beforePW);
			var cipher = crypto.createCipher('aes256', 'password');
			cipher.update(req.body.beforePW, 'ascii', 'hex');
			var cipherd = cipher.final('hex');
	
			if(cipherd == results[0].password) {
				var cipher2 = crypto.createCipher('aes256', 'password');
				cipher2.update(req.body.newPW, 'ascii', 'hex');
				var cipherd2 = cipher2.final('hex');
				connection.query("update passwordSetting set password='" + cipherd2 + "';");

				res.send('<script type="text/javascript">alert("비밀번호가 변경되었습니다."); history.back();</script>');
			}
			else
				res.send('<script type="text/javascript">alert("현재 비밀번호가 올바르지 않습니다."); history.back();</script>');
		
		}
	});
	}
});

router.get('/water', (req, res) => {
	var cookieValue = req.cookies.password;
	if(cookieValue == null)
		res.status(200).render('passwordPage.ejs');
	else
		res.status(200).render('water.ejs');
});

router.get('/feed', (req, res) => {
	var cookieValue = req.cookies.password;
	if(cookieValue == null)
		res.status(200).render('passwordPage.ejs');
	else
		res.status(200).render('feed.ejs');
});

router.get('/exchange', (req, res) => {
	var cookieValue = req.cookies.password;
	if(cookieValue == null)
		res.status(200).render('passwordPage.ejs');
	else
		res.status(200).render('exchange.ejs');
});

router.get('/streaming', (req, res) => {
	var cookieValue = req.cookies.password;
	if(cookieValue == null)
		res.status(200).render('passwordPage.ejs');
	else
		res.status(200).render('streaming.ejs');
});

router.get('/changePW', (req, res) => {
	var cookieValue = req.cookies.password;
	if(cookieValue == null)
		res.status(200).render('passwordPage.ejs');
	else
		res.status(200).render('changePW.ejs');
});

router.get('/logout', (req, res) => {
	res.clearCookie('password');
	res.redirect('/');
});

router.get('/main/StartServo', (req, res) => {
	var cookieValue = req.cookies.password;
	if(cookieValue == null)
		res.status(200).render('passwordPage.ejs');
	else {
		console.log('서보모터 동작');
		//arduinoPort.write('StartServo1');
		arduinoPort.write('StartServo1');
		res.status(200).send('Serial Controll OK!!');
	}
});

router.get('/main/StartWater', (req, res) => {
	var cookieValue = req.cookies.password;
	if(cookieValue == null)
		res.status(200).render('passwordPage.ejs');
	else {
		console.log('워터펌프  동작');
		arduinoPort.write('StartWater');
		res.status(200).send('Serial Controll OK!!');
	}
});

router.get('/inputWaterValue', (req, res) => {
	var cookieValue = req.cookies.password;
	if(cookieValue == null)
		res.status(200).render('passwordPage.ejs');
	else {
		const _url = req.url;
		const queryData = url.parse(_url, true).query;
		db.insertTemper(queryData.minTemper, queryData.maxTemper);
		db.insertPH(queryData.minPH, queryData.maxPH);
		res.send('<script type="text/javascript">alert("설정값이 저장되었습니다."); history.back();</script>');
	}
});

router.get('/inputFeedValue', (req, res) => {
	var cookieValue = req.cookies.password;
	if(cookieValue == null)
		res.status(200).render('passwordPage.ejs');
	else {
		const _url = req.url;
		const queryData = url.parse(_url, true).query;
		let setTime = (parseInt(queryData.feedTimerHour)*3600) + (parseInt(queryData.feedTimerMinute)*60);
		db.insertFeed(setTime, queryData.feedcnt, setTime);
		res.send('<script type="text/javascript">alert("설정값이 저장되었습니다."); history.back();</script>');
	}
});

module.exports = router;
