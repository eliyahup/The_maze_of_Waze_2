package gameClient;

import org.json.JSONException;
import org.json.JSONObject;

import utils.Point3D;

/**
 *This class represents Robot on a graph from the game 
 *
 */
public class Robot {
	private int id;
	private int src;	
	private int dest;
	private Point3D pos;


	/**
	 * Init from json info about the Robot.
	 * @param s
	 */
	public Robot(String s) {
		try {
			JSONObject Robot = new JSONObject(s);//deserialize
			JSONObject Robot2 = Robot.getJSONObject("Robot");
			String pi = Robot2.getString("pos"); //get location
			this.pos = new Point3D(pi); 
			this.id = Robot2.getInt("id");//get id;
			this.src = Robot2.getInt("src");// get src
			this.dest = Robot2.getInt("dest");// get dest
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}  


	}

	/**
	 * @return the src of this Robot.
	 */
	public int getSrc() {
		return src;
	}
	/**
	 * Allow setting the src edge of this Robot.
	 * @param src
	 */
	public void setSrc(int src) {
		this.src = src;
	}
	/**
	 * @return the destination edge  of this Robot.
	 */
	public int getDest() {
		return dest;
	}
	/**
	 * Allow setting the  destination edge  of this Robot.
	 * @param dest
	 */
	public void setDest(int dest) {
		this.dest = dest;
	}
	/**
	 * @return the Id of this Robot.
	 */
	public int getId() {
		return id;
	}
	/**
	 * @return the location of this Robot.
	 */
	public Point3D getPos() {
		return pos;
	}
}
