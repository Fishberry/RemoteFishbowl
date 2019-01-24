const socketio = require('socket.io');
const fs = require('fs');
module.exports = (server) => {
    let temperature = 0.0;
    fs.readFile('/sys/bus/w1/devices/28-020d9246133d/w1_slave', 'utf8', (err, data) => {
        console.log('읽어온 온도 : ', data);

        var text = data;
        //var first = text.substring(69,71);
        //var second = text.substring(71,74);
        var first = 24;
        var second = 512;
        temperature = `${first}.${second}`;
        console.log("Temperature : " + temperature);
    });

    const io = socketio.listen(server);

    io.sockets.on('connection', (socket) => {
        //const req = socket.request;
        //const ip = req.headers['x-forwarded-for'] || req.connection._remoteAddress;
        console.log('Socket ID : ', socket.id, ', Connect');

        socket.on('error', (error) => {
            console.error(error);
        });

        socket.on('reqMsg', (data) => {
            console.log('Client Message : ', data);
            //arduinoPort.write(data);
            socket.emit('serverMsg', temperature+data);
        });

        socket.on('disconnect', () => {
            console.log('연결 해제');
        });
    });
};