package mongoDataHandler;


import java.util.ArrayList;

import smaReader.SMAReader;


public class mongoDataHandler {

	public static void main(String[] args) {
		MongoConnection connection = new MongoConnection();
		ArrayList<String> todayArray = new ArrayList<String>();
		ArrayList<String> yestArray = new ArrayList<String>();

		connection.connect();
		connection.getDBCollection(connection.getDb());
		DataHandle dataHandle = new DataHandle(connection.getHouseData());
		todayArray = dataHandle.getData(0); // input must be <0 0 =  today, -1 = yesterday.
		yestArray = dataHandle.getData(-1);
		String nowData = dataHandle.getDataNow(todayArray);
		printArray(todayArray);
		SMAReader sma = new SMAReader();
		String smaValue = sma.httpResponse();
		
	}


	public static void printArray(ArrayList<String>array){
		for (int i =0; i<array.size();i++){
			System.out.println(array.get(i));
		}
	}
}
