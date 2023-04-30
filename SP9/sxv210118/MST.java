// Starter code for SP9

package sxv210118;

import sxv210118.Graph.Vertex;
import sxv210118.Graph.Edge;
import sxv210118.Graph.GraphAlgorithm;
import sxv210118.Graph.Factory;
import sxv210118.Graph.Timer;

import sxv210118.BinaryHeap.Index;
import sxv210118.BinaryHeap.IndexedHeap;

import java.util.*;
import java.io.FileNotFoundException;
import java.io.File;

public class MST extends GraphAlgorithm<MST.MSTVertex> {
	public static final int INF = Integer.MAX_VALUE;
	String algorithm;
	public long wmst;
	List<Edge> mst;

	MST(Graph g) {
		super(g, new MSTVertex((Vertex) null));
	}

	public static class MSTVertex implements Index, Comparable<MSTVertex>, Factory {
		int comp;
		Vertex parent;
		boolean seen;
		int distance;
		int index;
		Vertex self;

		MSTVertex(Vertex u) {
			comp = -1;
			parent = null;
			seen = false;
			distance = INF;
			self = u;
			index = 0;
		}

		MSTVertex(MSTVertex u) {  // for prim2
			seen = u.seen;
			parent = u.parent;
			distance = u.distance;
			index = u.index;
			self = u.self;
		}

		public MSTVertex make(Vertex u) { return new MSTVertex(u); }

		public void putIndex(int index) {
			this.index = index;
		}

		public int getIndex() {
			return index;
		}

		public int compareTo(MSTVertex other) {
			if (other.equals(null) || this.distance > other.distance)
				return 1;
			else if (this.distance == other.distance)
				return 0;
			else
				return -1;
		}
	}

	public void initialize(Graph g) {
		for (Vertex u : g) {
			setSeen(u, false);
			setParent(u, null);
			setDistance(u, INF);
			setSelf(u);
			setIndex(u);
		}
	}

	private void setIndex(Vertex u) {
		get(u).putIndex(u.getIndex());
	}

	private void setSelf(Vertex u) {
		get(u).self = u;
	}

	public Vertex getParent(Vertex u) {
		return get(u).parent;
	}

	public void setParent(Vertex u, Vertex p) {
		get(u).parent = p;
	}

	public boolean isSeen(Vertex u) {
		return get(u).seen;
	}

	public void setSeen(Vertex u, boolean b) {
		get(u).seen = b;
	}

	public int getDistance(Vertex u) {
		return get(u).distance;
	}

	public void setDistance(Vertex u, int d) {
		get(u).distance = d;
	}

	public long kruskal() {
		algorithm = "Kruskal";
		Edge[] edgeArray = g.getEdgeArray();
		mst = new LinkedList<>();
		wmst = 0;
		return wmst;
	}

	public long boruvka() {
		algorithm = "Boruvka";
		mst = new LinkedList<>();
		Graph f = new Graph(g.n);
		wmst = 0;

		int count = countAndLabel(f);
		while(count > 1) {
			AddSafeEdges(g.getEdgeArray(), f, count);
			count = countAndLabel(f);
		}
		for (Edge e: mst) {
			wmst += e.weight;
		}

		return wmst;
	}

	private void AddSafeEdges(Edge[] edges, Graph f, int count) {
		Edge[] safe = new Edge[count];
		for (Edge e : edges) {
			MSTVertex u = get(e.fromVertex());
			MSTVertex v = get(e.toVertex());
			if (u.comp != v.comp) {
				if (safe[u.comp] == null || e.compareTo(safe[u.comp]) < 0)
					safe[u.comp] = e;
				if (safe[v.comp] == null || e.compareTo(safe[v.comp]) < 0)
					safe[v.comp] = e;
			}
		}
		for (int i = 0; i < count; i++) {
			if (safe[i] != null && !mst.contains(safe[i])) {
				Edge e = safe[i];
				mst.add(e);
				f.addEdge(e.from, e.to, e.weight, e.name);
			}
		}
	}

	private int countAndLabel(Graph f) {
		initialize(f);
		int component = 0;

		for (Vertex s : f) {
			if (!isSeen(s)) {
				Stack<Vertex> st = new Stack<>();
				st.push(s);
				while (!st.isEmpty()) {
					Vertex u = st.pop();
					//pre
					setSeen(u, true);
					for (Edge e : f.incident(u)) {
						Vertex v = e.otherEnd(u);
						if (!isSeen(v)) {
							setParent(v, u);
							//comp
							get(v).comp = component;
							st.push(v);
						}
					}
					// post
					get(u).comp = component;
				}
				component++;
			}
		}

		return component;
	}

	public long prim2(Vertex s) {
		algorithm = "indexed heaps";
		mst = new LinkedList<>();
		wmst = 0;
		initialize(g);
		get(s).seen = true;
		get(s).distance = 0;
		IndexedHeap<MSTVertex> q = new IndexedHeap<>(g.size());
		for (Vertex u : g)
			q.add(get(u));


		while (!q.isEmpty()) {
			Vertex u = q.remove().self;
			get(u).seen = true;
			wmst += get(u).distance;
			for (Edge e : g.incident(u)) {
				Vertex v = e.otherEnd(u);
				if (!isSeen(v) && e.weight < get(v).distance) {
					setDistance(v, e.weight);
					setParent(v, u);
					q.decreaseKey(get(v));
				}
			}
		}

		return wmst;
	}

	public long prim1(Vertex s) {
		algorithm = "PriorityQueue<Edge>";
		mst = new LinkedList<>();
		wmst = 0;
		PriorityQueue<Edge> q = new PriorityQueue<>();
		return wmst;
	}

	public static MST mst(Graph g, Vertex s, int choice) {
		MST m = new MST(g);
		switch(choice) {
			case 0:
				m.boruvka();
				break;
			case 1:
				m.prim1(s);
				break;
			case 2:
				m.prim2(s);
				break;
			case 3:
				m.kruskal();
				break;
			default:

				break;
		}
		return m;
	}

	public static void main(String[] args) throws Exception {
		Scanner in;
		int choice = 2;  // prim2
		if (args.length == 0 || args[0].equals("-")) {
			in = new Scanner(System.in);
		} else {
			File inputFile = new File(args[0]);
			in = new Scanner(inputFile);
		}

		if (args.length > 1) { choice = Integer.parseInt(args[1]); }

		Graph g = Graph.readGraph(in);
		Vertex s = g.getVertex(1);

		Timer timer = new Timer();
		MST m = mst(g, s, choice);
		System.out.println(m.algorithm + "\n" + m.wmst);
		System.out.println(timer.end());
	}
}
