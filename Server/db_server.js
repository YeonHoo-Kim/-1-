var express = require('express');
var app = express();
var moment = require('moment');
require('moment-timezone');
moment.tz.setDefault("Asia/Seoul");
var date = moment().format('YYYY-MM-DD HH:mm:ss');

mysql = require('mysql');
var connection = mysql.createConnection({
	host: 'localhost',
	user: 'parking',
	password: 'park',
	database: 'parking'
});
connection.connect();

function insert_car(car_number){
	obj ={};
	obj.car_number = car_number;
	//obj.parking_date_time = date;
	moment.tz.setDefault("Asia/Seoul");
	obj.parking_date_time = moment().format('YYYY-MM-DD HH:mm:ss');

	//obj.loc = loc; //check location
        var num;
	var string;
	let count = 0;
	var q = connection.query('SELECT COUNT(*) AS COUNT FROM parking.Car_number WHERE car_number = ?',car_number,function(err, rows, cols){
                if (err) throw err;
		console.log("row",rows);
		//num = rows[0].COUNT;
		//num = rows[0].COUNT;
		//string = JSON.stringify(rows);
		// var json = JSON.parse(string);
		// console.log("json:",json);
		let item = rows[0];
		let count = item.COUNT;
		obj.count = count + 1;
		console.log(count, typeof count);
              //  console.log("database insertion ok= %j", obj);
		
		var query = connection.query('INSERT INTO parking.Car_number SET ?', obj, function(err, rows, cols){
			if (err) throw err;
			console.log("database insertion ok= %j", obj);
		});
	 
        });
	
};

function insert_black(car_number){
	obj={};
	obj.car_number = car_number;
	//obj.enrolled_date_time = date;
	
        moment.tz.setDefault("Asia/Seoul");
        obj.enrolled_date_time = moment().format('YYYY-MM-DD HH:mm:ss');

        var n = connection.query('SELECT COUNT(*) AS COUNT FROM parking.Blacklist WHERE car_number = ?',car_number,function(err, rows, cols){
                if (err) throw err;
                //obj.count = rows[0]['COUNT(*)'];
		//console.log("database insertion ok= %j", obj);
		let item = rows[0];
		let count = item.COUNT;
		obj.count = count+1;
		var query = connection.query('INSERT INTO parking.Blacklist SET ?', obj, function(err, rows, cols){
                if (err) throw err;
                console.log("database insertion ok= %j", obj);
	        });


        });

       // obj.count = q[0][count] + 1;

	//res.send(obj);
};


function insert_enroll(car_number, name){
        obj={};
        obj.car_number = car_number;
        //obj.enrolled_date_time = date;
	
        moment.tz.setDefault("Asia/Seoul");
        obj.enrolled_date_time = moment().format('YYYY-MM-DD HH:mm:ss');

	obj.name = name;

        var query = connection.query('INSERT INTO parking.Enrolled_car SET ?', obj, function(err, rows, cols){
                if (err) throw err;
                console.log("database insertion ok= %j", obj);
        });
        //res.send(obj);

};



app.get('/newcar',function(req,res){
	r = req.query;
	console.log("get %j", r);
	
	var info = {
		"car_number" : r.car_number
		//"loc" : r.loc
	}
	insert_car(r.car_number);
	res.send(info);
	res.end('OK: ' + JSON.stringify(req.query));

});

app.get('/newblack',function(req,res){
        r = req.query;
        console.log("get %j", r);

        var info = {
                "car_number" : r.car_number
        }
        insert_black(r.car_number);
        res.send(info);
        res.end('OK: ' + JSON.stringify(req.query));

});


app.get('/newenroll',function(req,res){
        r = req.query;
        console.log("get %j", r);

        var info = {
                "car_number" : r.car_number,
		"name" : r.name
        }
        insert_enroll(r.car_number, r.name);
        res.send(info);
        res.end('OK: ' +JSON.stringify(req.query));

});

app.get('/searchcar',function(req,res){
        
	var query = connection.query('SELECT car_id,car_number,DATE_FORMAT(parking_date_time,\'%Y-%m-%d %H:%i:%s\') AS parking_date_time ,loc,count FROM parking.Car_number ORDER BY parking_date_time desc',function(err,result){
                        if (err) throw err;
                       // res.send(result);
			var string = JSON.stringify(result);
                        var json = JSON.parse(string);
                        res.send(json);

                        console.log('well done');
                });
});

app.get('/searchblack',function(req,res){
        
        var query = connection.query('SELECT car_number, max(count) as record FROM parking.Blacklist GROUP BY car_number ORDER BY record desc',function(err,result){
                        if (err) throw err;
			var string = JSON.stringify(result);
			var json = JSON.parse(string);
                        res.send(json);
			console.log(json);
                        console.log('well done');
                });
});

app.get('/searchenroll',function(req,res){

        var query = connection.query('SELECT ID,car_number,DATE_FORMAT(enrolled_date_time,\'%Y-%m-%d %H:%i:%s\') AS enrolled_date_time,name FROM parking.Enrolled_car ORDER BY enrolled_date_time desc',function(err,result){
                        if (err) throw err;
                        //res.send(result);
			//console.log(result);
                        var string = JSON.stringify(result);
                        var json = JSON.parse(string);
                        res.send(json);

			console.log('well done');
                });
});

app.get('/delenroll',function(req,res){
	r = req.query;

        var query = connection.query('DELETE FROM parking.Enrolled_car where car_number = ?',r.car_number,function(err,result){
                        if (err) throw err;
                        //res.send(result);
                        //console.log(result);
                        var string = JSON.stringify(result);
                        var json = JSON.parse(string);
                        res.send(json);

                        console.log('delete done');
                });
});

app.get('/delblack',function(req,res){
        r = req.query;

        var query = connection.query('DELETE FROM parking.Blacklist where car_number = ?',r.car_number,function(err,result){
                        if (err) throw err;
                        //res.send(result);
                        //console.log(result);
                        var string = JSON.stringify(result);
                        var json = JSON.parse(string);
                        res.send(json);

                        console.log('delete done');
                });
});


app.get('/changeloc',function(req,res){
	r = req.query;

	var query = connection.query(' ALTER TABLE Car_number ALTER loc SET DEFAULT ?', r.loc ,function(err,result){
		if (err) throw err;
		res.send(r.loc);
		console.log('well done');
	});
});

var server = app.listen(8083,function () {
	var host = server.address().address;
	var port = server.address().port
	console.log('listening at http://%s:%s', host, port)
});
