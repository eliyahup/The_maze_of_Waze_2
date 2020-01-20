package algorithms;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;



import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.nodeData;
import dataStructure.node_data;
/**
 * This empty class represents the set of graph-theory algorithms
 * which should be implemented as part of Ex2 - Do edit this class.
 * @author 
 *
 */
public class Graph_Algo implements graph_algorithms, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private graph g1;


	public Graph_Algo() {             //Default constructor
		this.g1 = new DGraph();
	}

	public Graph_Algo(graph _graph) {
		this.g1 = new DGraph();
		this.g1 = _graph;
	}


	@Override
	public void init(graph g) {
		this.g1 =  g;
	}


	@Override
	public graph copy() {            //deep copy
		graph gcopy = new DGraph();
		for (node_data nd : this.g1.getV()) {
			gcopy.addNode(new nodeData(nd));
		}
		for (node_data nd : this.g1.getV()) {
			for (edge_data eg : this.g1.getE(nd.getKey())) {
				gcopy.connect(eg.getSrc(), eg.getDest(), eg.getWeight());
			}
		}
		return gcopy;
	}


	@Override
	public void init(String file_name) {     //Initializing object from save file.
		graph g = null; 
		try
		{    
			FileInputStream file = new FileInputStream(file_name); 
			ObjectInputStream in = new ObjectInputStream(file); 

			g = (graph)in.readObject(); 
			g1 = g;
			in.close(); 
			file.close(); 

			System.out.println("Object has been deserialized"); 
		} 

		catch(IOException ex) 
		{ 
			System.out.println("IOException is caught"); 
		} 

		catch(ClassNotFoundException ex) 
		{ 
			System.out.println("ClassNotFoundException is caught"); 
		} 

	}

	@Override
	public void save(String file_name) {    //save object 

		try
		{    
			FileOutputStream file = new FileOutputStream(file_name); 
			ObjectOutputStream out = new ObjectOutputStream(file); 

			out.writeObject(g1); 

			out.close(); 
			file.close(); 

			System.out.println("Object has been serialized"); 
		}   
		catch(IOException ex) 
		{ 
			System.out.println("IOException is caught"); 
		} 

	}
	
	
	private void DFSUtil(int v)        // DFS algorithm.
	{ 
		// Mark the current node as visited and print it 
		node_data t;
		t = g1.getNode(v);
		t.setTag(1);

		edge_data n; 

		// Recur for all the vertices adjacent to this vertex 
		Iterator<edge_data> i = g1.getE(t.getKey()).iterator();
		while (i.hasNext()) 
		{ 
			n = i.next(); 
			if (g1.getNode(n.getDest()).getTag()!=1) 
				DFSUtil(n.getDest()); 
		}
	}
	private DGraph getTranspose()    //reverse all edge.
	{
		DGraph g = new DGraph(); 
		Iterator<node_data> it1 = g1.getV().iterator();
		Iterator<node_data> it2 = g1.getV().iterator();
		while (it1.hasNext()) {
			node_data t = it1.next();
			g.addNode(t);
		}
		while (it2.hasNext()) {
			Iterator<edge_data> i = g1.getE(it2.next().getKey()).iterator();
			while(i.hasNext()) {
				edge_data e = i.next();
				g.connect(e.getDest(), e.getSrc(), e.getWeight());
			}
		}
		return g; 
	} 


	@Override
	public boolean isConnected() {  // isConnected use BFS algorithm.
		if(g1.getV().isEmpty()) {
			System.out.println("the graph is empty");
			return true;
		}
		// Step 1: Mark all the vertices as not visited 
		// (For first DFS)
		Iterator<node_data> it = g1.getV().iterator();
		while (it.hasNext()) {
			it.next().setTag(0);
		}
		// Step 2: Do DFS traversal starting from first vertex. 
		Iterator<node_data> it2 = g1.getV().iterator();
		if(it2.hasNext()) {
			DFSUtil( it2.next().getKey());
		}
		// If DFS traversal doesn't visit all vertices, then 
		// return false. 
		Iterator<node_data> it3 = g1.getV().iterator();
		while (it3.hasNext()) {
			if (it3.next().getTag()!=1)
				return false;
		}
		// Step 3: Create a reversed graph 
		DGraph gr = getTranspose();
		graph t = g1;
		this.g1 = gr;
		// Step 4: Mark all the vertices as not visited (For 
		// second DFS) 
		Iterator<node_data> it4 = gr.getV().iterator();
		while (it4.hasNext()) {
			it4.next().setTag(0);
		} 
		// Step 5: Do DFS for reversed graph starting from 
		// first vertex. Staring Vertex must be same starting 
		// point of first DFS 
		Iterator<node_data> it5 = gr.getV().iterator();
		if(it5.hasNext()) {
			DFSUtil(it5.next().getKey());
		}
		// If all vertices are not visited in second DFS, then 
		// return false 
		Iterator<node_data> it6 = gr.getV().iterator();
		while (it6.hasNext()) {
			if (it6.next().getTag()!=1) {
				//this.g1 = t;
				return false;
			}
		}
		this.g1 = t;
		return true;
	}


	@Override
	public double shortestPathDist(int src, int dest) {
		if(g1.getNode(src)==null||g1.getNode(dest)==null) { // if node is not exist
			return -1;
		}
		Iterator<node_data> it = g1.getV().iterator();
		double d = 0;
		while (it.hasNext()) {
			node_data t = it.next();
			t.setTag(0);                     // init unvisited
			t.setWeight(Double.MAX_VALUE);   //infinity is Double.MAX_VALUE
		}
		g1.getNode(src).setWeight(0);     //start vertex
		int s = src;
		while(isNotEmpty()){
			Iterator<node_data> it2 = g1.getV().iterator();
			node_data min = it2.next();
			while(min.getTag()!=0) {
				min = it2.next();
			}
			node_data v3 = null;
			if(it2.hasNext()) {
				v3 = it2.next();
			}
			while(it2.hasNext()) {   //chose the min Weight vertex
				if(min.getWeight()>v3.getWeight()&&v3.getTag()==0) {
					min = v3;
				}
				v3 = it2.next();
				if(min.getWeight()>v3.getWeight()&&v3.getTag()==0) {
					min = v3;
				}
			}
			
			s = min.getKey();
			Iterator<edge_data> i = g1.getE(s).iterator();	
			while(i.hasNext()) {                         
				edge_data v = i.next();
				if(g1.getNode(s).getWeight()+v.getWeight()<g1.getNode(v.getDest()).getWeight()) {
					g1.getNode(v.getDest()).setWeight(g1.getNode(s).getWeight()+v.getWeight());
					g1.getNode(v.getDest()).setInfo(""+g1.getNode(s).getKey());
				}
			}
			g1.getNode(s).setTag(1);
		}
		d = g1.getNode(dest).getWeight();
		return d; 
	}

	private boolean isNotEmpty() {    //if not yet visit all vertices   
		Iterator<node_data> it = g1.getV().iterator();
		while (it.hasNext()) {
			if (it.next().getTag()==0)
				return true;		
		}
		return false;
	}

	@Override
	public List<node_data> shortestPath(int src, int dest) {
		if(g1.getNode(src)==null||g1.getNode(dest)==null) {   // if node is not exist
			return null;
		}
		if(src==dest) {
			List<node_data> n = new ArrayList<node_data>();
			n.add(g1.getNode(src));
			return n;
		}
		shortestPathDist(src, dest);
		if(g1.getNode(dest).getWeight()==Double.MAX_VALUE) {
			return null;
		}
		List<node_data> n = new ArrayList<node_data>();
		List<node_data> n2 = new ArrayList<node_data>();
		n.add(g1.getNode(dest));
		int prev = dest;
		while(Integer.parseInt(g1.getNode(prev).getInfo())!=src ) { // give the path in reverse
			n.add(g1.getNode(Integer.parseInt(g1.getNode(prev).getInfo())));
			prev = Integer.parseInt(g1.getNode(prev).getInfo());	
		}
		n.add(g1.getNode(src));
		int i = 0;
		while(i < n.size()) {
			n2.add(n.get(n.size()-1-i));
			i++;
		}

		return n2;
	}

	@Override
	public List<node_data> TSP(List<Integer> targets) {

		List<node_data> v;
		List<node_data> n = new ArrayList<node_data>();
		if(targets.size()==1){
			n.add(g1.getNode(targets.get(0)));
		}
		for (int i = 0; i < targets.size()-1; ) {    // give the shortest path between target list
			v = shortestPath(targets.get(i), targets.get(i+1));
			if(v==null) {
				return null;
			}
			n.addAll(v); 
			i++; 
			if( i < targets.size()-1) {
				n.remove(n.size()-1);
			}
		} 
		return n;
	}



}
