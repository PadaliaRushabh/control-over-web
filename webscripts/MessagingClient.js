/**
 * New node file
 */

var HOST = "controloverweb.herokuapp.com";
var PATH = '/message';
var http = require('http');

exports.SendMesage = function(type){
	var options = {
			host:HOST,
			path:PATH ,
			method:'POST',
			headers :{
					'Content-Type': "application/json"
				}
		};
	var datatowrite = {
		    	"type": type,
	};
	
	var request = http.request(options , function(request){
		request.setEncoding('utf8');
		request.on('data' , function(chunk){
			console.log(chunk);
		});
	});
	
	request.on('error' , function(err){
		//console.log("on error:" + err);
		console.log(err.message); 
		console.log( err.stack );
	});

	requestBody = JSON.stringify(datatowrite);
	request.write(requestBody);
	request.end();

};