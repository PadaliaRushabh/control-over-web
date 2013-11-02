/**
 * New node file
 */

var cow = require('./controloverweb.js');
var xml2js = require('xml2js').parseString;
var controller = require('./controller.js');

var path = require('path');

var HOST = '192.168.1.92';
var PORT = 80;
var PATH="GET /state.xml HTTP/1.1\nAuthorization: Basic bm9uZTp3ZWJyZWxheQ==\r\n\r\n";


setInterval(function(){
var client = cow.init(HOST , PORT , PATH);

	controller.init(path.basename(__filename));

	client.on('data' , function(xml){
		xml2js(xml,function(err , result){
			var temp = {
				time : new Date()
				,unit :result.datavalues.units.toString()
				,home : result.datavalues.sensor1temp.toString()
				,outside : result.datavalues.sensor2temp.toString()		
			};
			controller.insertToDatabase(temp);
		}); 
	});

}, 60000);
