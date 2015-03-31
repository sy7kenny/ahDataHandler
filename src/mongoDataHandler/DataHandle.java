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

	// Constructor that pass the houseData collection from MongoDB to this class
	public DataHandle(DBCollection houseData) {

		this.houseData = houseData;
	}

	/**
	 * Return an ArrayList of the day of data needed to be displayed on D3 The
	 * element in the ArrayList is an json object in string format. This method
	 * will use the houseData collection from MongoDB to find the specific day
	 * of data. Also it will do an iteration on the time portion of the array,
	 * rounding to the nearest ten minutes
	 * 
	 * @param offset
	 *            the offset of day in date, 0 indicates today, -1 = yesterday,
	 *            etc
	 * @return arrayList of the data
	 */
	public ArrayList<String> getData(int offset) {

		ArrayList<String> resultArray = new ArrayList<String>();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, offset);
		SimpleDateFormat ft = new SimpleDateFormat("E MMM d");
		String dateString = ft.format(cal.getTime());
		BasicDBObject query = new BasicDBObject("time",
				java.util.regex.Pattern.compile(dateString)); // this part sets
																// the date
																// pattern to
																// find
		DBCursor cursor = houseData.find(query); // finding part

		BasicDBObject tobj = null;
		while (cursor.hasNext()) {

			/*
			 * The while loop here will collect all the matching date data into
			 * an arrayList
			 */

			tobj = (BasicDBObject) cursor.next();
			Object time = tobj.get("time");
			String roundTime = roundDate(time.toString());
			Object power = tobj.get("power");
			BasicDBObject powerJson = new BasicDBObject();
			powerJson.put("time", roundTime);
			powerJson.put("power", power);
			resultArray.add(powerJson.toString());

		}

		return resultArray;

	}

	/**
	 * Returns a string in json format about this instant moment's power data.
	 * @param todayArray the arrayList that is already generated for faster computation
	 * @return string format of this instant moment's power data.
	 */
	public String getDataNow(ArrayList<String> todayArray) {

		return (todayArray.get(todayArray.size() - 1));
	}

	/**
	 * A dirty way using regex to iterate the time to the nearest 10 min.
	 * @param time the time string that will be evaluated, takes the format E MMM d hh:mm:ss zzz yyyy
	 * @return the iterated time string
	 */
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

	/**
	 * A cleaner way to iterate the time to nearest 10 min
	 * by setting the time string back to Date object
	 * and change the min and sec value inside
	 * @param time the time string to be iterated
	 * @return the rounded min time string
	 */
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
