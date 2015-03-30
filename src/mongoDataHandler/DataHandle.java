package mongoDataHandler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.ParseException;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

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
			// BObject tobj = cursor.next();
			// power = (BasicDBList) tobj.get("power");
			// String jsonObj = JSON.serialize(cursor.next());

			// String result = (cursor.next().toString());

			// resultArray.add(result);

			tobj = (BasicDBObject) cursor.next();
			Object time = tobj.get("time");
			// String fixTime = fixTime(time.toString());
			String roundTime = roundDate(time.toString());
			Object power = tobj.get("power");
			// System.out.println(time.toString());

			BasicDBObject powerJson = new BasicDBObject();
			// powerJson.put("time", fixTime);
			powerJson.put("time", roundTime);
			powerJson.put("power", power);
			resultArray.add(powerJson.toString());

		}

		return resultArray;

	}

	public String getDataNow(ArrayList<String> todayArray) {

		return (todayArray.get(todayArray.size() - 1));
	}

	public String fixTime(String time) {
		String regex = "\\w+ \\w+ \\d+ (\\d{2}:\\d{2}:\\d{2}).*";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(time);
		m.matches();
		StringBuffer sb = new StringBuffer();

		String hr = time.replaceFirst(".*?(\\d+).(\\d+).(\\d+).*", "$2");
		String min = time.replaceFirst(".*?(\\d+).(\\d+).(\\d+).*", "$3");

		int minInt = Integer.parseInt(min);
		int roundMin = (int) ((Math.rint((double) minInt / 10) * 10) % 60);
		String roundMinStr = Integer.toString(roundMin);
		if (roundMinStr.equals("0")) {
			roundMinStr = "00";
		}
		String newTime = hr + ":" + roundMinStr + ":" + "00";

		String replace = m.group()
				.replaceAll("(\\d{2}:\\d{2}:\\d{2})", newTime);
		m.appendReplacement(sb, replace);
		m.appendTail(sb);

		return sb.toString();

	}

	@SuppressWarnings("deprecation")
	public String roundDate(String time) {
		DateFormat formatter = new SimpleDateFormat("E MMM d hh:mm:ss zzz yyyy");
		Date date = null;
		try {
			date = formatter.parse(time);
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int min = date.getMinutes();
		int roundMin = (int) ((Math.rint((double) min / 10) * 10) % 60);
		date.setMinutes(roundMin);
		date.setSeconds(0);

		return date.toString();

	}
}
