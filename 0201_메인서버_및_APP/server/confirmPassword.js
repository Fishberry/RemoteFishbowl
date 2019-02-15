const fs = require('fs');

module.exports = function(password) {
	var number;

	fs.readFile('/home/pi/Desktop/FishberryServer/password.txt', (err, data) => {
		console.log('file password : ' + data);
		if(data == password) {
			console.log("1");
			number = 1;
		}
		else {
			console.log("2");
			number = 2;
		}
	});

	return number;
}
