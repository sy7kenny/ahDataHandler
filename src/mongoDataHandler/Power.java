package mongoDataHandler;

import java.io.Serializable;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class Power implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String pB;
	private String pG;
	private String pH;
	private String pP;

	public Power() {
	}

	public Power(String pB, String pG, String pH, String pP) {
		this.pB = pB;
		this.pG = pG;
		this.pH = pH;
		this.pP = pP;
	}

	public String getpB() {
		return pB;
	}

	public void setpB(String pB) {
		this.pB = pB;
	}

	public String getpG() {
		return pG;
	}

	public void setpG(String pG) {
		this.pG = pG;
	}

	public String getpH() {
		return pH;
	}

	public void setpH(String pH) {
		this.pH = pH;
	}

	public String getpP() {
		return pP;
	}

	public void setpP(String pP) {
		this.pP = pP;
	}

	public DBObject bsonFromPojo() {
		BasicDBObject document = new BasicDBObject();

		document.put("pB", this.pB);
		document.put("pG", this.pG);
		document.put("pH", this.pH);
		document.put("pP", this.pP);

		return document;
	}

	public void makePojoFromBson(DBObject bson) {
		BasicDBObject b = (BasicDBObject) bson;
		this.pB = (String) b.get("pB");
		this.pG = (String) b.get("pG");
		this.pH = (String) b.get("pH");
		this.pP = (String) b.get("pP");
	}
}
