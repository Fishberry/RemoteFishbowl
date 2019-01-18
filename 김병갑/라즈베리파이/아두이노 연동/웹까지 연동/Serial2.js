const express = require('express');
const http = require('http');
const app = express();
const path = require('path');

const server = http.createServer(app);
server.listen(3000, () => {
	console.log('Server Start');
});

const serialPort = require('serialport');
const com6 = new serialPort('/dev/ttyACM0', {
	baudRate: 9600,
	dataBits: 8,
	parity: 'none',
	stopBits: 1,
	flowControl: false
});
com6.on('open', () => {
	console.log('open serial communication');
});

app.set('views', path.join(__dirname, 'views'));
app.set('view-engine', 'ejs');
app.use(express.static(path.join(__dirname, 'public')));

app.get('/', (req, res) => {
	res.status(200).render('controller.ejs');
});

app.get('/controller/:id', (req, res) => {
	console.log(req.params.id);
	com6.write(req.params.id);
	res.status(200).send('Serial Controll OK!!');
});
