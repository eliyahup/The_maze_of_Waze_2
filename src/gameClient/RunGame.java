package gameClient;


import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import Server.game_service;
import dataStructure.DGraph;

/**
 *This class Run the Game Which gets from scenario number game.
 */
public class RunGame   {
	public static Scenario scenario;

	/**
	 * This function gets the scenario number.
	 * The function uses the number scenario which places the robots and fruits on the graph and builds it.
	 * The function calculates the shortest path between the robot and the fruit.
	 * @param scenario_num
	 */
	public RunGame(int scenario_num){
		scenario = new Scenario(scenario_num);
		
		Thread startGame = new Thread( new Runnable() {
			@Override
			public void run() {
				scenario.game.startGame();
				int count = 0;
				while(scenario.game.isRunning()) { 
					long t = scenario.game.timeToEnd();
					if(count%10==0) {
						System.out.println( "time to end:"+(t/1000));
					}
					moveRobots(scenario.game, scenario.gr);
					try {

						Thread.sleep(100);
						count++;


					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
				String results = scenario.game.toString();
				System.out.println("Game Over: "+results);
			}
		});
		startGame.start();

	}


	private static void moveRobots(game_service game, DGraph gg) {
		List<String> log = game.move();

		if(log!=null) {
			for(int i=0;i<log.size();i++) {

				String robot_json = log.get(i);
				try {
					JSONObject line = new JSONObject(robot_json);
					JSONObject ttt = line.getJSONObject("Robot");
					int rid = ttt.getInt("id");
					int src = ttt.getInt("src");
					int dest = ttt.getInt("dest");


					if(dest==-1) {
						dest = Game_Algo.nextNode(scenario, src);
						System.out.println(ttt);
						game.chooseNextEdge(rid, dest);
						//System.out.println("Turn to node: "+dest);
					}
				} 
				catch (JSONException e) {e.printStackTrace();}
			}
		}
	}	


}


