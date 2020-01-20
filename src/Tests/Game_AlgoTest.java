package Tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import dataStructure.edgeData;
import gameClient.Game_Algo;
import gameClient.Scenario;

/**
 * This class is a test for GameAlgo class 
 */
class Game_AlgoTest {
	public static Scenario scenario;

	/**
	 * This class is a test for GameAlgo class 
	 */
	@Test
	void test() {
		System.out.println("**********test getFruitEdge*************"); 
		scenario = new Scenario(2);
		edgeData t1 = scenario.fruit.get(0).getEdge();
		edgeData t2 = scenario.fruit.get(1).getEdge();
		edgeData t3 = scenario.fruit.get(2).getEdge();

		if(t1.getSrc()!=9||t1.getDest()!=8)
			fail("shoulbe be equals");

		if(t2.getSrc()!=4||t2.getDest()!=3)
			fail("shoulbe be equals");

		if(t3.getSrc()!=3||t3.getDest()!=2)
			fail("shoulbe be equals");
	}
	/**
	 * test if a robot is near a fruit. 
	 */
	@Test
	void test2() {                 
		System.out.println("**********test addRobotNearFruit*************"); 
		scenario = new Scenario(2);
		edgeData t1 = scenario.fruit.get(0).getEdge();
		
		if(t1.getSrc()!=9||t1.getDest()!=8)
			fail("shoulbe be equals");

		if(scenario.robot.get(0).getSrc()!=9)
			fail("shoulbe be placed in src of fruit");
	}
	
	/**
	 * test if next node is the best path to fruit. 
	 */
	@Test
	void test3() {                 
		System.out.println("**********test nextNode*************"); 
		scenario = new Scenario(2);
		edgeData t1 = scenario.fruit.get(0).getEdge();
		
		if(t1.getSrc()!=9||t1.getDest()!=8)
			fail("shoulbe be equals");

		if(scenario.robot.get(0).getSrc()!=9)
			fail("shoulbe be placed in src of fruit");
		
		if(Game_Algo.nextNode(scenario,scenario.robot.get(0).getSrc())!=8)
			fail("The next step is wrong");
	}
}
