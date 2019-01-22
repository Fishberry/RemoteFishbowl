const express = require('express');
const http = require('http');
const fs = require('fs');
const app = express();
const path = require('path');
const socketio = require('socket.io');

const server = http.createServer(app);

server.listen(3000, () => {
	console.log('Server Start');
});

const serialPort = require('serialport');
const com6 = new serialPort('COM6', {
	baudRate: 9600,
	dataBits: 8,
	parity: 'none',
	stopBits: 1,
	flowControl: false
});
com6.on('open', () => {
	console.log('open serial communication');
});

let temperature = 0.0;
fs.readFile('/sys/bus/w1/devices/28-020d9246133d/w1_slave', 'utf8', (err, data) => {
	console.log(data);

	var text = data;
	//var first = text.substring(69,71);
	//var second = text.substring(71,74);
	var first = 24;
	var second = 512;
	temperature = `${first}.${second}`;
	console.log("Temperature : " + temperature);
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
const io = socketio.listen(server);
io.sockets.on('connection', function (socket){ 
    console.log('Socket ID : ' + socket.id + ', Connect');
    socket.on('reqMsg', function(data){
        console.log('Client Message : ' + data);
        com6.write(data);
        socket.emit('serverMessage', temperature);
    });
 });