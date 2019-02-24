const mysql = require('mysql');

const connection = mysql.createConnection({
	host: 'localhost',
	user: 'root',
	password: '1234',
	database: 'Fishberry'
});

connection.connect();

exports.insertTemper = function(min, max) {
	connection.query('update TemperSetting set min='+min+', max='+max, (error, results) => {
		if(error)
			console.log(error);
		else
			console.log(results);
	});
};

exports.insertPH = function(min, max) {
	connection.query('update PHSetting set min='+min+', max='+max, (error, results) => {
		if(error)
			console.log(error);
		else
			console.log(results);
	});
};

exports.insertFeed = function(timer, circle, save_time) {
	connection.query('update FeedSetting set timer='+timer+', circle='+circle+', save_time='+save_time, (error, results) => {
		if(error)
			console.log(error);
		else
			console.log(results);
	});
};

exports.confirmPassword = function(password) {
	connection.query('select * from passwordSetting', (error, results, fields) => {
		if(error)
			console.log(error);
		else {
			console.log('database : ' + results[0].password);
			if(password == results[0].password)
				return "OK"
			else
				return "NO"
		}
	});
};
