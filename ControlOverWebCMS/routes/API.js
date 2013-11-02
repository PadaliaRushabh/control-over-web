/**
 * New node file
 */

var database = require('../models/RegistrationID.js');
var mongoose = require('mongoose');

mongoose.connect("mongodb://Rushabh:rishi724@ds049548.mongolab.com:49548/controloverweb");


exports.insertToDatabase = function(data , callback){
	new database(data).save(function(err){
		if(err)
			console.log(err);
		//mongoose.connection.close();
		
		callback(err);
	});	
};

exports.getAllKeys = function(callback){
	database.find({},"ID" , function(err ,data){
		
		callback(err , data);
	});
	
}
