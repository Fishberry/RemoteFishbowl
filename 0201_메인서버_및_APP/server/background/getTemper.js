const fs = require('fs');

module.exports = function(){
	var temperature = 0.0;

	fs.readFile('/sys/bus/w1/devices/28-020d9246133d/w1_slave', 'utf8', (err, data) => {
		var text = data;
		var first = text.substring(69,71);
		var second = text.substring(71,74);

		temperature = first + '.' + second;
	});

	return temperature;
};
