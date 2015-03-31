package mongoDataHandler;


import java.util.ArrayList;

import smaReader.SMAReader;


public class mongoDataHandler {

	public static void main(String[] args) {
		MongoConnection connection = new MongoConnection();
		ArrayList<String> todayArray = new ArrayList<String>();
		ArrayList<String> yestArray = new ArrayList<String>();
		String nowData;
		String smaValue;
		connection.connect(); //connection to the mongoDB
		connection.getDBCollection(connection.getDb()); //getting the DBcollection
		DataHandle dataHandle = new DataHandle(connection.getHouseData()); //then transfer the DBobject to dataHandle class
		
		todayArray = dataHandle.getData(0); // input must be <0 0 =  today, -1 = yesterday.
		yestArray = dataHandle.getData(-1);
		
		nowData = dataHandle.getDataNow(todayArray);
		printArray(todayArray);
		SMAReader sma = new SMAReader();
		smaValue = sma.httpResponse();
		
		
	}

	// Just print to see the array
	public static void printArray(ArrayList<String>array){
		for (int i =0; i<array.size();i++){
			System.out.println(array.get(i));
		}
	}
}
