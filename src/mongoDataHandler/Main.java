package mongoDataHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class Main {

	public static void main(String[] args) {
		MongoClientURI uri = new MongoClientURI(
				"mongodb://gsfUser:gsfUser@ds030827.mongolab.com:30827/aggievillage");
		Date date = new Date();

		try {
			MongoClient mongoClient = new MongoClient(uri);
			DB db = mongoClient.getDB(uri.getDatabase());
			System.out.println("Connect to database successfully");
			DBCollection houseData = db.getCollection("houseData");
			getYesterdayData(houseData);

		} catch (Exception e) {
			System.out.println("Failed");
		}

	}

	public static void getYesterdayData(DBCollection houseData) {
		ArrayList<String> resultArray = new ArrayList<String>();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		SimpleDateFormat ft = new SimpleDateFormat("E MMM d");
		String yestString = ft.format(cal.getTime());
		BasicDBObject query = new BasicDBObject("time",
				java.util.regex.Pattern.compile(yestString));
		DBCursor cursor = houseData.find(query);
		int i = 0;
		while (cursor.hasNext()) {
			// System.out.println(cursor.next());
			// System.out.println("Inserted Document: "+i);
			// DBObject doc = cursor.next();
			// String json1 = doc.toString();
			// //System.out.println(json1);
			//
			i++;
			String result = cursor.next().toString();
			if (i % 3 == 0) {
				resultArray.add(result);
			}
		}
		
			System.out.println(resultArray.get(0));
		
	}

	public static void getNowData(DBCollection houseData) {

	}
}
