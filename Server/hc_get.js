var express = require('express');
var app = express();
var moment = require('moment');
require('moment-timezone');
moment.tz.setDefault("Asia/Seoul");

let users = [{flag: 0, date: 0}]

app.get('/hc', function(req,res){
	obj = {};
	
        r = req.query;
        console.log("GET %j", r);

	var date = moment().format('YYYY-MM-DD HH:mm:ss');
	
        var hc_get = r.hc;
        if(hc_get == '1'){
                users[0].date = date;
                users[0].flag = hc_get;
               res.send(users[0]);
      console.log('Car is parked. %s', date);
        }else if(hc_get == '0'){
                users[0].date = 0;
                users[0].flag = 0;
                res.send(users[0]);
        console.log('No car is parked.');
        }else{
                res.send(users[0]);
      console.log('check.');
        }


});

var server = app.listen(8082, function () {
  var host = server.address().address
  var port = server.address().port
  console.log('listening at http://%s:%s', host, port)
});
