package dataStructure;

import java.io.Serializable;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;

import utils.Point3D;

public class DGraph implements graph, Serializable {
	public HashMap<Integer, node_data> vertices;
	public HashMap<node_data,HashMap<Integer, edge_data> > edges;
	int MC=0;
	int edgesSize=0;

	public DGraph() {
		this.vertices = new HashMap<Integer, node_data>();
		this.edges = new HashMap<node_data, HashMap<Integer, edge_data>>();
	}

	public void init(String s) {
		try {
			JSONObject g = new JSONObject(s);
			JSONArray nodes = g.getJSONArray("Nodes");
			JSONArray edges = g.getJSONArray("Edges");
		
			for (int i = 0; i < nodes.length(); i++)
			{
				int id = nodes.getJSONObject(i).getInt("id");
				String pi = nodes.getJSONObject(i).getString("pos");
				Point3D  Pi = new Point3D(pi); 
				nodeData ni = new nodeData(Pi,id,0.0,null,0);
				this.addNode(ni);
				
			}
			for (int i = 0; i < edges.length(); i++)
			{
				int sr = edges.getJSONObject(i).getInt("src");
				int d = edges.getJSONObject(i).getInt("dest");
				double w = edges.getJSONObject(i).getDouble("w");
				connect(sr, d, w);
			}

		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public node_data getNode(int key) {
		return this.vertices.get(key);
	}

	@Override
	public edge_data getEdge(int src, int dest) {
		node_data vertex = this.vertices.get(src);
		edge_data edge =  this.edges.get(vertex).get(dest);

		return edge;
	}

	@Override
	public void addNode(node_data n) {

		if(this.vertices.containsKey(n)==false) {
			this.vertices.put(n.getKey(), n);
			this.edges.put(n, new HashMap<Integer, edge_data>());
			this.MC++;
		}
	}

	@Override
	public void connect(int src, int dest, double w) {
		node_data vertex = this.vertices.get(src);
		if(vertex==null||this.vertices.get(dest)==null)
			throw new IllegalArgumentException("source vertex not in graph");
		this.edges.get(vertex).put(dest,new edgeData(src,dest,w,"",0));
		this.MC++;
		this.edgesSize++;
	}

	@Override
	public Collection<node_data> getV() {
		Collection<node_data> ver;
		ver = this.vertices.values();
		return ver;
	}

	@Override
	public Collection<edge_data> getE(int node_id) {
		node_data vertex = this.vertices.get(node_id);
		Collection<edge_data> edg = this.edges.get(vertex).values();
		return edg;
	}
	@Override
	public node_data removeNode(int key) {
		node_data vertex = this.vertices.get(key);
		if (vertex == null) return null;
		Set<node_data> set = this.edges.keySet();
		for (node_data node_data : set) {
			edge_data edg = this.edges.get(node_data).remove(key);
			if (edg != null) {
				this.edgesSize--;
			}
		}
		this.edgesSize -= this.edges.get(vertex).size();
		this.edges.remove(vertex);
		this.MC++;
		return this.vertices.remove(key);

	}
	@Override
	public edge_data removeEdge(int src, int dest) {
		node_data vertex = this.vertices.get(src);
		edge_data edge = this.edges.get(vertex).remove(dest);
		this.MC++;
		this.edgesSize--;
		return edge;
	}

	@Override
	public int nodeSize() {
		return this.vertices.size();
	}

	@Override
	public int edgeSize() {
		return this.edgesSize;
	}

	@Override
	public int getMC() {
		return MC;
	}
	public String toString() {
		String ans = "";
		Iterator<node_data> it1 = this.getV().iterator();
		while(it1.hasNext()) {
			node_data t = it1.next();
			ans += t.getKey()+" d="+t.getWeight();
			Iterator<edge_data> it2 = this.getE(t.getKey()).iterator();
			while(it2.hasNext()) {
				edge_data e = it2.next();
				ans +="("+e.getSrc()+"-->"+e.getDest()+")"; 				
			}
			ans +=", ";
		}
		return ans;

	}

}
