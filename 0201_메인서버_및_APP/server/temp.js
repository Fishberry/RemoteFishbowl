const mysql = require('mysql');
const crypto = require('crypto');

var cipher = crypto.createCipher('aes256', 'password');
cipher.update('0000', 'ascii', 'hex');
var cipherd = cipher.final('hex');

const connection = mysql.createConnection({
	host: 'localhost',
	user: 'root',
	password: '1234',
	database: 'Fishberry'
});

connection.connect();

connection.query("insert into passwordSetting values('" + cipherd  + "');", (err, result) => {
	if(err)
		console.log(err);
	else
		console.log('OK');
});

connection.end();
