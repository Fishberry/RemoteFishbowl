const fs = require('fs');

fs.readFile('/sys/bus/w1/devices/28-020d9246133d/w1_slave', 'utf8', (err, data) => {
	console.log(data);

	var text = data;
	var first = text.substring(69,71);
	var second = text.substring(71,74);
	console.log("Temperature : " + first + "." + second);
	//console.log("Temperature : ", temper);
});
