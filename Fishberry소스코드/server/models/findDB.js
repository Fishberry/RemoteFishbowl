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

exports.insertTvalue = function(Tvalue) {
	connection.query('update TemperValue set Tvalue='+Tvalue, (error, results) => {
		if(error)
			console.log(error);
		else
			//console.log(results.Tvalue);
			console.log();
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

exports.insertPvalue = function(Pvalue) {
	connection.query('update PHValue set Pvalue='+Pvalue, (error, results) => {
		if(error)
			console.log(error);
		else
			//console.log(results);
			console.log();
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

exports.insertExchange = function(t1, t2, save_time) {
	connection.query('update ExchangeSetting set exTimer1='+t1+', exTimer2='+t2+', exTime_save='+ save_time +', isChanged=false, totalPercent=0', (error, results) => {
		if(error)
			console.log(error);
		else
			console.log(results);
	});
};

exports.startExchange = function(isChanged) {
	connection.query('update ExchangeSetting set isChanged = ' + isChanged, (error, results) => {
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
