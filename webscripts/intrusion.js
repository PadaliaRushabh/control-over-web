/**
 * New node file
 */

var cow = require('./controloverweb.js');
var xml2js = require('xml2js').parseString;
var controller = require('./controller.js');
var MessagingClient = require('./MessagingClient.js');
var path = require('path');

var HOST = '192.168.1.94';
var PORT = 80;
var PATH="GET /state.xml HTTP/1.1\nAuthorization: Basic bm9uZTp3ZWJyZWxheQ==\r\n\r\n";

var client = cow.init(HOST , PORT , PATH);

controller.init(path.basename(__filename));

client.on('data' , function(xml){
	xml2js(xml,function(err , result){
		var count = parseInt(result.datavalues.count1.toString());
		controller.getIntrusionFromDatabase(function(data){
		console.log(data[0].count);
			if(count > data[0].count ||  (count !=0 && count < data[0].count)){
				console.log("in");
				var intrusion = {
					time : new Date()
					,type :"intrusion"
					,count : count	
				};
			
				controller.insertToDatabase(intrusion);
				MessagingClient.SendMesage("Intrusion");
			}
		});
	}); 
	
	xml2js(xml,function(err , result){
		var count = parseInt(result.datavalues.count2.toString());
		controller.getFireFromDatabase(function(data){
			
			if(count > data[0].count ||  (count !=0 && count < data[0].count)){
				var fire = {
					time : new Date()
					,type :"fire"
					,count :count		
				};
			
				controller.insertToDatabase(fire);
				MessagingClient.SendMesage("Fire");
			} 
		});
	}); 
	
	//console.log(xml);
});