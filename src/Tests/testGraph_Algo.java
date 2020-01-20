package Tests;


import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.graph;
import dataStructure.nodeData;
import dataStructure.node_data;
import gui.graphGui;
import utils.Point3D;

class testGraph_Algo {

	@Test
	void test1() {
		System.out.println("**********test 1000000V*10E*************");
		DGraph P = new DGraph();                  //build million vertices and 10 edge per vertex
		for (int i = 0; i < 100000; i++) {
			Point3D  pi = new Point3D(10+i,10,0);
			nodeData ti = new nodeData(pi,i,0.0,null,0);
			P.addNode(ti);
			for (int j = 0; j < 10&&i>10; j++) {
				P.connect(i,j,1);
			}
		}
	}


	@Test
	void test2() {
		System.out.println("**********test isConnected*************");
		DGraph g1 = new DGraph();                         //create connected graph.
		Point3D  g = new Point3D(10,10,0);
		Point3D  g2 = new Point3D(20,10,0);
		Point3D  g3 = new Point3D(30,10,0);
		Point3D  g4 = new Point3D(40,10,0);
		Point3D  g5 = new Point3D(50,10,0);
		nodeData t1 = new nodeData(g,0,0.0,null,0);
		nodeData t2 = new nodeData(g2,1,0.0,null,0);
		nodeData t3 = new nodeData(g3,2,0.0 ,null,0);
		nodeData t4 = new nodeData(g4,3,0.0 ,null,0);
		nodeData t5 = new nodeData(g5,4 ,0.0,null,0);
		g1.addNode(t1);
		g1.addNode(t2);
		g1.addNode(t3);
		g1.addNode(t4);
		g1.addNode(t5);
		g1.connect(0, 1, 1); 
		g1.connect(1, 2, 2); 
		g1.connect(2, 3, 6); 
		g1.connect(3, 0, 4); 
		g1.connect(2, 4, 3); 
		g1.connect(4, 2, 5); 
		Graph_Algo G1 = new Graph_Algo(g1);
		if (!G1.isConnected()) {
			fail("shoulbe be connected");
		}
		DGraph G2 = new DGraph();               //create unconnected graph.
		nodeData p1 = new nodeData(g,0,0.0,null,0);
		nodeData p2 = new nodeData(g2,1,0.0,null,0);
		nodeData p3 = new nodeData(g3,2,0.0 ,null,0);
		nodeData p4 = new nodeData(g4,3,0.0 ,null,0);
		G2.addNode(p1);
		G2.addNode(p2);
		G2.addNode(p3);
		G2.addNode(p4);
		G2.connect(0,1,2); 
		G2.connect(1,2,3); 
		G2.connect(2,3,4); 
		Graph_Algo G3 = new Graph_Algo();
		G3.init(G2);
		if (G3.isConnected()) {
			fail("shoulbe be disconnected");
		}
	}

	@Test
	void test3() {
		System.out.println("**********test save and init file*************");
		DGraph P = new DGraph();
		for (int i = 0; i < 10; i++) {    //build graph and save
			Point3D  pi = new Point3D(10+i,10,0);
			nodeData ti = new nodeData(pi,i,0.0,null,0);
			P.addNode(ti);
		}
		for (int i = 0; i < 20; i++) {
			P.connect((int)(Math.random() * 10), (int)(Math.random() * 10),(int)(Math.random() * 20));
		}
		Graph_Algo G1 = new Graph_Algo(P);
		G1.save("graph.txt");
		Graph_Algo G2 = new Graph_Algo();
		G2.init("graph.txt");

	}

	@Test
	void test4() {
		System.out.println("**********test shortestPathDist*************");
		DGraph P = new DGraph();        //build random graph and print shortest distance.
		for (int i = 0; i < 10; i++) {      
			Point3D  pi = new Point3D(10+i,10,0);
			nodeData ti = new nodeData(pi,i,0.0,null,0);
			P.addNode(ti);
		}
		for (int i = 0; i < 20; i++) {
			P.connect((int)(Math.random() * 10), (int)(Math.random() * 10),(int)(Math.random() * 20));
		}
		Graph_Algo G1 = new Graph_Algo(P);
		System.out.println(G1.shortestPathDist((int)(Math.random() * 10), (int)(Math.random() * 10)));

	}

	@Test
	void test5() {
		System.out.println("**********test shortestPath*************");
		DGraph P = new DGraph();  //build random graph and print shortest path.
		for (int i = 0; i < 10; i++) {
			Point3D  pi = new Point3D(10+i,10,0);
			nodeData ti = new nodeData(pi,i,0.0,null,0);
			P.addNode(ti);
		}
		for (int i = 0; i < 20; i++) {
			P.connect((int)(Math.random() * 10), (int)(Math.random() * 10),(int)(Math.random() * 20));
		}
		Graph_Algo G1 = new Graph_Algo(P);
		List<node_data> n = new ArrayList<node_data>();
		n = G1.shortestPath((int)(Math.random() * 10), (int)(Math.random() * 10));
		for (int i = 0; n!=null&&i < n.size();i++ ) {		      
			System.out.print(n.get(i).getKey()+"->"); 		
		}
		System.out.println();
		if(n==null) {
			System.out.println("not connection between vertics");
		}
	}

	@Test
	void test6() {
		System.out.println("**********test TSP*************");
		DGraph P = new DGraph();           //build random graph and print shortest path between all target list.
		for (int i = 0; i < 10; i++) {
			Point3D  pi = new Point3D(10+i,10,0);
			nodeData ti = new nodeData(pi,i,0.0,null,0);
			P.addNode(ti);
		}
		for (int i = 0; i < 20; i++) {
			P.connect((int)(Math.random() * 10), (int)(Math.random() * 10),(int)(Math.random() * 20));
		}
		Graph_Algo G1 = new Graph_Algo(P);
		List<node_data> n = new ArrayList<node_data>();
		List<Integer> targets = new ArrayList<Integer>();
		targets.add((int)(Math.random() * 10));
		targets.add((int)(Math.random() * 10));
		targets.add((int)(Math.random() * 10));
		targets.add((int)(Math.random() * 10));
		n = G1.TSP(targets);
		for (int i = 0; n!=null&&i < n.size();i++ ) {		      
			System.out.print(n.get(i).getKey()+"->"); 		
		}
		System.out.println();
		if(n==null) {
			System.out.println("not connection between vertics");
		}
	}

	@Test
	void test7() {
		System.out.println("**********test copy*************");  // checks deep
		DGraph g1 = new DGraph();
		Point3D  g = new Point3D(Math.random() * 700 + 10, Math.random() * 500 + 100, 0);
		Point3D  g2 = new Point3D(Math.random() * 700 + 10, Math.random() * 500 + 100, 0);
		Point3D  g3 = new Point3D(Math.random() * 700 + 10, Math.random() * 500 + 100, 0);
		Point3D  g4 = new Point3D(Math.random() * 700 + 10, Math.random() * 500 + 100, 0);
		Point3D  g5 = new Point3D(Math.random() * 700 + 10, Math.random() * 500 + 100, 0);
		nodeData t1 = new nodeData(g,0,0.0,null,0);
		nodeData t2 = new nodeData(g2,1,0.0,null,0);
		nodeData t3 = new nodeData(g3,2,0.0 ,null,0);
		nodeData t4 = new nodeData(g4,3,0.0 ,null,0);
		nodeData t5 = new nodeData(g5,4 ,0.0,null,0);
		g1.addNode(t1);
		g1.addNode(t2);
		g1.addNode(t3);
		g1.addNode(t4);
		g1.addNode(t5);
		g1.connect(0, 1, 1); 
		g1.connect(1, 2, 2); 
		g1.connect(2, 3, 6); 
		g1.connect(3, 0, 4); 
		g1.connect(2, 4, 3); 
		g1.connect(4, 2, 5); 
		Graph_Algo G1 = new Graph_Algo(g1);
		System.out.println(G1.shortestPathDist(2, 4));
		drawGraph(g1);
		DGraph DG2 = new DGraph();
		DG2 = (DGraph) G1.copy();
		DG2.removeEdge(2, 3);
		Graph_Algo G2 = new Graph_Algo(DG2);
		if (!G1.isConnected()) {
			fail("shoulbe be connected");
		}
		if (G2.isConnected()) {
			fail("shoulbe be disconnected");
		}
	}


	boolean drawGraph(graph g) { 
		graphGui G = new graphGui();
		G.buildGraph(g);
		return true;
	}


}










