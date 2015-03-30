package smaReader;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

@SuppressWarnings("deprecation")
public class SMAReader {
	private String sma;
	private BasicDBObject enviro;
	HttpPost smaPost = new HttpPost("http://ucdavisvillage.ddns.net:3334/rpc");
	HttpClient httpClient = new DefaultHttpClient();

	public SMAReader() {
		this.enviro = new BasicDBObject();
		ArrayList<String> channels = new ArrayList<String>();
		channels.add("CO2 saved");
		channels.add("E-Total");
		BasicDBObject pvDevice = new BasicDBObject();
		pvDevice.put("key", "WR30U09E:2002225636");
		pvDevice.put("channels", channels);
		BasicDBObject device = new BasicDBObject();

		BasicDBList deviceList = new BasicDBList();
		deviceList.add(pvDevice);
		device.put("devices", deviceList);

		BasicDBObject smaJson = new BasicDBObject();
		smaJson.put("version", "1.0");
		smaJson.put("format", "JSON");
		smaJson.put("passwd", "a289fa4252ed5af8e3e9f9bee545c172");
		smaJson.put("format", "JSON");
		smaJson.put("proc", "GetProcessData");
		smaJson.put("id", "5");
		smaJson.put("params", device);

		this.sma = smaJson.toString();
		// WR30U09E:2002225636
		// CO2 Saved
		// E-Total
		System.out.println(smaJson.toString());
	}

	public String httpResponse() {
		// This function interacts with the sma sunnywebbox

		try {
			
			StringEntity RPC = new StringEntity("RPC=" + this.sma);
			RPC.setContentType("application/json");

			smaPost.setEntity(RPC);
			HttpResponse result = httpClient.execute(smaPost);
			smaPost.reset();

			String json = EntityUtils.toString(result.getEntity());

			System.out.println(json);

			DBObject o = (DBObject) JSON.parse(json);
			BasicDBObject b = (BasicDBObject) o;
			Object devices = b.get("result");

			System.out.println(devices.toString());

			DBObject de = (DBObject) devices;
			BasicDBList deviceList = (BasicDBList) de.get("devices");
			ArrayList<BasicDBObject> deviceArray = (ArrayList) deviceList;

			for (BasicDBObject dev : deviceArray) {
				BasicDBList chList = (BasicDBList) dev.get("channels");
				ArrayList<BasicDBObject> chArray = (ArrayList) chList;

				for (BasicDBObject ch : chArray) {

					enviro.put(ch.getString("name"), ch.get("value"));

				}

			}




		} catch (IOException ex) {
			System.out.println("Unable to get http Response");
			
		}
		return enviro.toString();

	}
}
