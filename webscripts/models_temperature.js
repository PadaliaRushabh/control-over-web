/**
 * New node file
 */
var mongoose = require('mongoose');

var Temperature = new mongoose.Schema({
	
	time:Date
	,unit:String
	,home:Number
	,outside:Number
});



module.exports = mongoose.model('Temperature' , Temperature);
