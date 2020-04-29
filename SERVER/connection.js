var mysql = require('mysql');

console.log('Get connection ...');

var conn = mysql.createConnection({
  database: 'location',
  host: "localhost",
  user: "root",
  password: "123456"
});

