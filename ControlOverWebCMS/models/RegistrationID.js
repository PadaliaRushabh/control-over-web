/**
 * New node file
 */
var mongoose = require('mongoose');

var RegistrationID = new mongoose.Schema({
	time:Date
	,ID:String
});

module.exports = mongoose.model('RegistrationID' , RegistrationID);