package mongoDataHandler;

import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoException;

public class MongoConnection {
	private MongoClientURI uri;
	private String collection;
	private DB db;
	private DBCollection houseData;

	

	public MongoConnection() {
		this.uri = new MongoClientURI(
				"mongodb://gsfUser:gsfUser@ds030827.mongolab.com:30827/aggievillage");
		this.collection = "houseData";
	}

	public void connect() {
		try {
			MongoClient mongoClient = new MongoClient(uri);
			this.db = mongoClient.getDB(uri.getDatabase());

			System.out.println("Connect to database successfully");
			

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		}

	}

	public void getDBCollection(DB db) {
		this.houseData = db.getCollection(collection);

	}
	//getter methods
	public DBCollection getHouseData() {
		return houseData;
	}

	public DB getDb() {
		return db;
	}
}
