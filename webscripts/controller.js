/**
 * New node file
 */

var mongoose = require('mongoose');
mongoose.connect("mongodb://Rushabh:rishi724@ds049548.mongolab.com:49548/controloverweb");

var database = null ;
exports.init = function(filename){
	if(filename == 'temperature.js'){
		database = require('./models_temperature.js');
		console.log("temp");
	}
	else if(filename == "intrusion.js"){
		database = require('./models_intrusionandfire.js');
		console.log("intru");
	}
};

exports.insertToDatabase = function(temperature_data){
	new database(temperature_data).save(function(err){
		if(err)
			console.log(err);
		//mongoose.connection.close();
	});	
};

exports.getTemperatureFromDatabase = function(){
	database.find({}).sort({time:-1}).limit(1).execFind(function(err , data){
		if(err){
			console.log(err);
			return;
		}
		
		console.log(data);
	});
};


exports.getIntrusionFromDatabase = function(callback){
	database.findOne({"type":"intrusion"} , "count" , { _id: false }).sort({time:-1}).execFind(function(err , data){
		if(err){
			console.log(err);
			return;
		}
		callback(data);
	});
};


exports.getFireFromDatabase = function(callback){
	database.findOne({"type":"fire"} , "count" , { _id: false }).sort({time:-1}).execFind(function(err , data){
		if(err){
			console.log(err);
			return;
		}
		callback(data);
	});
};
