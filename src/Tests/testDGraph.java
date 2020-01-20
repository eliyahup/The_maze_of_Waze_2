package Tests;

import static org.junit.Assert.fail;

import org.junit.jupiter.api.Test;

import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.nodeData;
import utils.Point3D;

class testDGraph {

	@Test
	void test1() {
		System.out.println("**********test connect*************");

		DGraph G = new DGraph();                    //add node
		Point3D  g = new Point3D(10,10,0);
		nodeData t1 = new nodeData(g,1,0.0,null,0);
		G.addNode(t1);
		if(G.getNode(1).getKey()!=1)
		fail("vertex not added");
	}
	@Test
	void test2() {
		System.out.println("**********test removeNode*************");
		DGraph G = new DGraph();
		Point3D  a = new Point3D(10,10,0);        
		nodeData p = new nodeData(a,1,0.0,null,0);
		G.addNode(p);
		G.removeNode(1);                    //remove node without edges
		if(!G.getV().isEmpty())	
		fail("shoulbe be empty");
		
		DGraph g1 = new DGraph();
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
	g1.removeNode(2);                //remove node with edges
	System.out.println(g1);
	}
	@Test
	void test3() {
		System.out.println("**********test removeEdge*************"); 
		DGraph g1 = new DGraph();
		Point3D  g = new Point3D(10,10,0);           // remove edge
		Point3D  g2 = new Point3D(20,10,0);
		nodeData t1 = new nodeData(g,0,0.0,null,0);
		nodeData t2 = new nodeData(g2,1,0.0,null,0);
		g1.addNode(t1);
		g1.addNode(t2);
		g1.connect(0, 1, 2);
     	g1.removeEdge(0, 1);
		if(!g1.getE(0).isEmpty())	
		fail("shoulbe be empty");
			}
	@Test
	void test4() {
		System.out.println("**********test MC*************");
		int mc = 0;
		DGraph g1 = new DGraph();           //test if was a change in a graph
		Point3D  g = new Point3D(10,10,0);
		Point3D  g2 = new Point3D(20,10,0);
		nodeData t1 = new nodeData(g,0,0.0,null,0);
		nodeData t2 = new nodeData(g2,1,0.0,null,0);
		g1.addNode(t1);
		mc++;
		g1.addNode(t2);
		mc++;
		g1.connect(0, 1, 2);
		mc++;
     	g1.removeEdge(0, 1);
		mc++;
	    g1.removeNode(1);
		mc++;
		if(mc!=g1.getMC())
		fail("shoulbe be equals");
	}
	@Test
	void test5() {
		System.out.println("**********test size*************");
		int sizeEdge = 0;                 //checks change in a size vertices and edges
		int sizeNode = 0;
		DGraph g1 = new DGraph();
		Point3D  g = new Point3D(10,10,0);
		Point3D  g2 = new Point3D(20,10,0);
		nodeData t1 = new nodeData(g,0,0.0,null,0);
		nodeData t2 = new nodeData(g2,1,0.0,null,0);
		g1.addNode(t1);
		sizeNode++;
		g1.addNode(t2);
		sizeNode++;
		g1.connect(0, 1, 2);
		sizeEdge++;
		g1.connect(1, 0, 2);
		sizeEdge++;
		if(g1.edgeSize()!=sizeEdge||g1.nodeSize()!=sizeNode)
		fail("shoulbe be equals");
		
		g1.removeEdge(0, 1);
		sizeEdge--;
		g1.removeEdge(1, 0);
		sizeEdge--;
		if(g1.edgeSize()!=sizeEdge||g1.nodeSize()!=sizeNode)
			fail("shoulbe be equals");
		
		g1.removeNode(1);
		sizeNode--;
		if(g1.edgeSize()!=sizeEdge||g1.nodeSize()!=sizeNode)
			fail("shoulbe be equals");
	}
}
