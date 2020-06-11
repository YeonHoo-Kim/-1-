const path = require('path');
const express = require('express');
const WebSocket = require('ws');
const app = express();

const WS_PORT  = 8080;
const WS2_PORT = 8081;
const HTTP_PORT = 8000;

const wsServer = new WebSocket.Server({port: WS_PORT}, ()=> console.log(`WS stream Server is listening at ${WS_PORT}`));
//const wsServer2 = new WebSocket.Server({port: WS2_PORT}, ()=> console.log(`WS2 picture Server is listening as ${WS_PORT}`));

let connectedClients = [];
wsServer.on('connection', (ws, req)=>{
    console.log('Stream server Connected');
    connectedClients.push(ws);

    ws.on('message', data => {
        connectedClients.forEach((ws,i)=>{
            if(ws.readyState === ws.OPEN){
                ws.send(data);
            }else{
                connectedClients.splice(i ,1);
            }
        })
    });
});
/*
let connectedClients2 = [];
wsServer2.on('connection', (ws2, req)=>{
    console.log('Picture server Connected');
    connectedClients2.push(ws2);

    ws2.on('message', data2 => {
        connectedClients2.forEach((ws2,j)=>{
            if(ws2.readyState === ws2.OPEN){
                ws2.send(data2);
            }else{
                connectedClients2.splice(j ,1);
            }
        })
    });
});
*/
/*
app.get('/stream',function(req,res){
	r = req.query;
	var device_id = r.device_id;
	if(device_id == ''){
		res.sendFile(path.resolve(__dirname, './stream.html'));
	}else{
		res.sendFile(path.resolve(__dirname, './picture.html'));
	}
});
*/
app.get('/stream',(req,res)=>res.sendFile(path.resolve(__dirname, './stream.html')));
app.get('/picture',(req,res)=>res.sendFile(path.resolve(__dirname, './picture.html')));

app.listen(HTTP_PORT, ()=> console.log(`HTTP server listening at ${HTTP_PORT}`));
