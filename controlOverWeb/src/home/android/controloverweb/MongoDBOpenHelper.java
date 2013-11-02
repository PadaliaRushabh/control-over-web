package home.android.controloverweb;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bson.BSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoURI;

public class MongoDBOpenHelper {

	public MongoDBOpenHelper() throws UnknownHostException{};
	
	
	public DBObject getTemperature(){
		String host = "mongodb://Rushabh:rishi724@ds049548.mongolab.com:49548/controloverweb";
		MongoURI uri = new MongoURI(host);
		DBCursor cursorObj = null;
		try {
				DB db = uri.connectDB();
				//db.authenticate(uri.getUsername(), uri.getPassword());
				boolean auth = db.authenticate(uri.getUsername(),uri.getPassword());
				DBCollection collection = db.getCollection("temperatures");
				cursorObj = collection.find().sort(new BasicDBObject("time" , -1)).limit(1);
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cursorObj.next();
	}
	
	public DBObject getIntrusion(){
		String host = "mongodb://Rushabh:rishi724@ds049548.mongolab.com:49548/controloverweb";
		MongoURI uri = new MongoURI(host);
		
		DBCursor cursorIntrusionObj = null;
	

		try {
			DB db = uri.connectDB();
			boolean auth = db.authenticate(uri.getUsername(), uri.getPassword());
			DBCollection collection = db.getCollection("intrusionandfires");
			
			BasicDBObject searchQuery = new BasicDBObject();
			searchQuery.put("type", "intrusion");
			
			cursorIntrusionObj = collection.find(searchQuery).sort(new BasicDBObject("time", -1)).limit(1);
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cursorIntrusionObj.next();
	}
	
	public DBObject getFire(){
		String host = "mongodb://Rushabh:rishi724@ds049548.mongolab.com:49548/controloverweb";
		MongoURI uri = new MongoURI(host);
		
		DBCursor cursorFireObj = null;
		

		try {
			DB db = uri.connectDB();
			boolean auth = db.authenticate(uri.getUsername(), uri.getPassword());
			DBCollection collection = db.getCollection("intrusionandfires");
			
			BasicDBObject searchQuery = new BasicDBObject();
			searchQuery.put("type", "fire");
			cursorFireObj = collection.find(searchQuery).sort(new BasicDBObject("time", -1)).limit(1);
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cursorFireObj.next();
	}
	
}

