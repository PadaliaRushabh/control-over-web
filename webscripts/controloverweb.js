var net = require('net');

exports.init = function(HOST , PORT , PATH){

	var client = new net.Socket();

	//TODO:Log to Text File

	client.connect(PORT , HOST, function(err){
		console.log("connected");
	});


	client.setEncoding('utf8');

	//TODO:Log to Text File
	client.on('connect' , function(){
		client.write(PATH);
	});

	//TODO:Log to text file
	client.on('error' , function(err){
		console.log("error");
	});
	
	return client;
};
