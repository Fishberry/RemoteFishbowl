const mysql = require('mysql');

const connection = mysql.createConnection({
	host: 'localhost',
	user: 'root',
	password: '1234',
	database: 'Fishberry'
});

connection.connect();

exports.insertTemper = function(min, max) {
	connection.query('delete from TemperSetting;', () => {});
	connection.query('insert into TemperSetting values (' + min + ', ' + max + ');', (error, results) => {
		if(error)
			console.log(error);
		else
			console.log(results);
	});
};

exports.insertPH = function(min, max) {
	connection.query('delete from PHSetting', () => {});
	connection.query('insert into PHSetting values (' + min + ', ' + max + ');', (error, results) => {
		if(error)
			console.log(error);
		else
			console.log(results);
	});
};

exports.insertFeed = function(timer, circle) {
	connection.query('delete from FeedSetting;', () => {});
	connection.query('insert into FeedSetting values (' + timer + ', ' + circle + ');', (error, results) => {
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
