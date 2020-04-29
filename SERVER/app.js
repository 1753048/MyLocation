var express = require("express");
var app = express();
var server = require("http").createServer(app);
var io = require("socket.io").listen(server);
var fs = require("fs");
server.listen(process.env.PORT || 3000);

/*Connect to db*/
var mysql = require("mysql");

console.log("Get connection ...");

var conn = mysql.createConnection({
  database: "location",
  host: "localhost",
  user: "root",
  password: "123456"
});

conn.connect();
/* ------------------------------*/
app.get("/", function(req, res) {
  res.sendFile(__dirname + "/index.html");
});

io.sockets.on("connection", function(socket) {
  var today = new Date();
  console.log("New connection at " + today.getTime());

  socket.on("client-login", function(username, password) {
    console.log("New login: ", username + " " + password);
    var query =
      "Select password from account where username = '" + username + "';";
    conn.query(query, function(err, res) {
      if (err) throw err;
      if (res.length == 0) {
        console.log("Username does not exist! ");
        socket.emit("Server-login", { isExist: false, isCorrect: false });
      } else {
        console.log(res[0]["password"]);
        if (password == res[0]["password"])
          socket.emit("Server-login", { isExist: true, isCorrect: true });
        else socket.emit("Server-login", { isExist: true, isCorrect: false });
      }
    });
  });

  socket.on("client-createUser", function(username, password, email) {
    console.log("Username: " + username);
    var query = "Select * from account where username = '" + username + "';";
    conn.query(query, function(err, res) {
      if (err) throw err;
      console.log(res.length);
      if (res.length != 0) {
        console.log("Exist");
        socket.emit("Server-checkExist", { exist: true });
      } else {
        var query2 = "select count(*) from account;";
        conn.query(query2, function(err2, res2) {
          if (err2) throw err2;
          var count = parseInt(res2[0]["count(*)"]) + 1;
          var queryInsert =
            "insert into account(id,username,password,email) values (" +
            count +
            ",'" +
            username +
            "','" +
            password +
            "','" +
            email +
            "');";
          console.log(queryInsert);
          conn.query(queryInsert, function(err3, res3) {
            if (err3) {
              console.log("err");
              socket.emit("Server-createUser", { isSuccess: false });
              throw err3;
            }
            console.log("Insert success");
            socket.emit("Server-createUser", { isSuccess: true });
          });
        });
      }
    });
  });
});
