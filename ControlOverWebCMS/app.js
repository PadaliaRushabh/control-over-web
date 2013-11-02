
/**
 * Module dependencies.
 */

var express = require('express')
  , routes = require('./routes')
  , message = require('./routes/message')
  , user = require('./routes/user')
  , register = require('./routes/register')
  , http = require('http')
  , path = require('path');

var app = express();

GOOGLE_API_KEY = "AIzaSyDVg65Y27Ga0Y93VBgzeCupw8oJ8AEFxeI";
GMC_HOST = "android.googleapis.com";
GMC_PATH = "/gcm/send";

// all environments
app.set('port', process.env.PORT || 3000);
app.set('views', __dirname + '/views');
app.set('view engine', 'jade');
app.use(express.favicon());
app.use(express.logger('dev'));
app.use(express.bodyParser());
app.use(express.methodOverride());
app.use(app.router);
app.use(express.static(path.join(__dirname, 'public')));

// development only
if ('development' == app.get('env')) {
  app.use(express.errorHandler());
}

app.get('/', routes.index);
app.post('/message', message.message);
app.post('/register', register.register);
app.get('/users', user.list);

http.createServer(app).listen(app.get('port'), function(){
  console.log('Express server listening on port ' + app.get('port'));
});
