/**
 * New node file
 */
var http = require('http');
var api = require('./API.js');

exports.message = function(req , res){
	api.getAllKeys(function(err , data){
		if(err) return;
		
		var arr = new Array();
		
		for(var i = 0 ; i < data.length ; i++){
			arr.push(data[i].ID);
		}
		var options = {
			
			host: GMC_HOST,
			path: GMC_PATH,
			method:'POST',
			headers :{
					'Content-Type': "application/json",
					'Authorization':'key=' + GOOGLE_API_KEY
				}
		};
		
		var datatowrite = {
				"collapse_key": "cow_update"
				,"time_to_live": 108
				,"delay_while_idle": true,
				"data": {
			    	"type": req.body.type,
			    	"time": new Date().toISOString().
			    				   replace(/T/, ' ').      // replace T with a space
			    				   replace(/\..+/, '') 
				},
				//"registration_ids":["APA91bETlpjbz0M37Hn-9FXOd3R4Sk-poXk5c415AOL0vokRg3NLTDzt0kRfGGZERpHpJG20Ib5W3x_n3osdlcEvI4kMrZu6BHg3EMb9Nwi3U1vVPC6fL8IUYs1WY1AQWfV9cuMgCrSP-dsEeUVNEYB6C0H9r7gFKBccUhUihAQzW7gWQ_5Ucp4"]
				"registration_ids":arr
		};

		var request = http.request(options , function(request){
			request.setEncoding('utf8');
			request.on("data" , function(chunk){
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
	
		res.render('index' , {message:'true'});
	

	});
};