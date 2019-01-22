const socketio = require('socket.io');

module.exports = (server, temperature) => {
    const io = socketio(server, { path: '/socket.io'});

    io.on('connection', (socket) => {
        const req = socket.request;
        const ip = req.headers['x-forwarded-for'] || req.connection._remoteAddress;
        console.log('Socket ID : ', socket.id, ', Connect IP: ', req.ip, ip);

        socket.on('error', (error) => {
            console.error(error);
        });

        socket.on('reqMsg', (data) => {
            console.log('Client Message : ', data);
            //arduinoPort.write(data);
            socket.emit('serverMessage', temperature);
        });
    });
};