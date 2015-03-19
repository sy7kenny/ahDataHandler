package mongoDataHandler;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class DataHandle {
	private DBCollection houseData;

	public DataHandle(DBCollection houseData) {
		this.houseData = houseData;
	}

	public ArrayList<String> getData(int offset) {
		ArrayList<String> resultArray = new ArrayList<String>();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, offset);
		SimpleDateFormat ft = new SimpleDateFormat("E MMM d");
		String dateString = ft.format(cal.getTime());
		BasicDBObject query = new BasicDBObject("time",
				java.util.regex.Pattern.compile(dateString));
		DBCursor cursor = houseData.find(query);
		
		BasicDBObject tobj = null;
		while (cursor.hasNext()) {
			//BObject tobj = cursor.next();
			// power = (BasicDBList) tobj.get("power");
			//String jsonObj = JSON.serialize(cursor.next());

			//String result = (cursor.next().toString());

			//resultArray.add(result);
			
			tobj = (BasicDBObject) cursor.next();
			BasicDBList power = (BasicDBList) tobj.get("power");
			
			System.out.println(power);

			
		}

		return resultArray;

	}

}
