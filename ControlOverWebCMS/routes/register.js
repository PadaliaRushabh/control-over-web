/**
 * New node file
 */

var api = require('./API.js');

exports.register = function(req , res){
	
	var RegID = req.body.registerID;
	
	console.log(RegID);
	var data = {
			time:new Date()
			,ID: RegID
	};
	api.insertToDatabase(data , function(err){
		
		if(err) console.log(err.message);
		console.log("ID Stored");
	});
	
	res.end("true");
};