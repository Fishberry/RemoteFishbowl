const mysql = require('mysql');
const crypto = require('crypto');

const connection = mysql.createConnection({
	host: 'localhost',
	user: 'root',
	password: '1234',
	database: 'Fishberry'
});
connection.connect();

var pw = '0000';

var cipher = crypto.createCipher('aes256', 'password');
cipher.update(pw, 'ascii', 'hex');
var cipherd = cipher.final('hex');

connection.query("insert into passwordSetting values ('" + cipherd + "');");
