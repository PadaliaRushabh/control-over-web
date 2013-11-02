/**
 * New node file
 */
var mongoose = require('mongoose');

var IntrusionAndFire = new mongoose.Schema({
	time:Date
	,type:String
	,count:Number
});

module.exports = mongoose.model('IntrusionAndFire' , IntrusionAndFire);