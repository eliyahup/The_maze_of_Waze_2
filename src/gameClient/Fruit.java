package gameClient;

import org.json.JSONException;
import org.json.JSONObject;

import dataStructure.DGraph;
import dataStructure.edgeData;
import utils.Point3D;


/**
 *This class represents fruit on a graph from the game 
 */
public class Fruit {
	private int type;
	private Point3D pos;	
	private double value;
	private edgeData edge;
	/**
	 * Init from json info about the Fruit.
	 * @param g
	 * @param s
	 */
	public Fruit(String s, DGraph g) {
		try {
			JSONObject fruit = new JSONObject(s);//deserialize
			JSONObject fruit2 = fruit.getJSONObject("Fruit");
			String pi = fruit2.getString("pos"); //get location
			this.pos = new Point3D(pi); 
			this.type = fruit2.getInt("type");//get location int;
			this.value = fruit2.getDouble("value");// get value
			this.edge = (edgeData) Game_Algo.getFruitEdge(s, g);
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}  
	}
	/**
	 * Init from json info about the Fruit.
	 * @param s
	 */
	public Fruit(String s) {
		try {
			JSONObject fruit = new JSONObject(s);//deserialize
			JSONObject fruit2 = fruit.getJSONObject("Fruit");
			String pi = fruit2.getString("pos"); //get location
			this.pos = new Point3D(pi); 
			this.type = fruit2.getInt("type");//get location int;
			this.value = fruit2.getDouble("value");// get valu
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}  
	}
	/**
	 * @return the Type of this Fruit.
	 */
	public int getType() {
		return type;
	}
	/**
	 * @return the location of this Fruit.
	 */
	public Point3D getPos() {
		return pos;
	}
	/**
	 * @return the Value of this Fruit.
	 */
	public double getValue() {
		return value;
	}
	/**
	 * @return the Edge of this Fruit.
	 */
	public edgeData getEdge() {
		return edge;
	}

}
