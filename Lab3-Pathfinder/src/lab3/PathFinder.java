package lab3;

import java.util.List;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.PriorityQueue;
import java.util.Random;

import java.util.stream.Collectors;

public class PathFinder<V> {

	private DirectedGraph<V> graph;
	private long startTimeMillis;

	public PathFinder(DirectedGraph<V> graph) {
		this.graph = graph;
	}

	public class Result<V> {
		public final boolean success;
		public final V start;
		public final V goal;
		public final double cost;
		public final List<V> path;
		public final int visitedNodes;
		public final double elapsedTime;

		public Result(boolean success, V start, V goal, double cost, List<V> path, int visitedNodes) {
			this.success = success;
			this.start = start;
			this.goal = goal;
			this.cost = cost;
			this.path = path;
			this.visitedNodes = visitedNodes;
			this.elapsedTime = (System.currentTimeMillis() - startTimeMillis) / 1000.0;
		}

		public String toString() {
			String s = "";
			s += String.format("Visited nodes: %d\n", visitedNodes);
			s += String.format("Elapsed time: %.1f seconds\n", elapsedTime);
			if (success) {
				s += String.format("Total cost from %s -> %s: %s\n", start, goal, cost);
				s += "Path: " + path.stream().map(x -> x.toString()).collect(Collectors.joining(" -> "));
			} else {
				s += String.format("No path found from %s", start);
			}
			return s;
		}
	}

	public Result<V> search(String algorithm, V start, V goal) {
		startTimeMillis = System.currentTimeMillis();
		switch (algorithm) {
		case "random":
			return searchRandom(start, goal);
		case "dijkstra":
			return searchDijkstra(start, goal);
		case "astar":
			return searchAstar(start, goal);
		}
		throw new IllegalArgumentException("Unknown search algorithm: " + algorithm);
	}

	public Result<V> searchRandom(V start, V goal) {
		int visitedNodes = 0;
		LinkedList<V> path = new LinkedList<>();
		double cost = 0.0;
		Random random = new Random();

		V current = start;
		path.add(current);
		while (current != null) {
			visitedNodes++;
			if (current.equals(goal)) {
				return new Result<>(true, start, current, cost, path, visitedNodes);
			}

			List<DirectedEdge<V>> neighbours = graph.outgoingEdges(start);
			if (neighbours == null || neighbours.size() == 0) {
				break;
			} else {
				DirectedEdge<V> edge = neighbours.get(random.nextInt(neighbours.size()));
				cost += edge.weight();
				current = edge.to();
				path.add(current);
			}
		}
		return new Result<>(false, start, null, -1, null, visitedNodes);
	}

	public Result<V> searchDijkstra(V start, V goal) {
		int visitedNodes = 0;
		/********************
		 * TODO: Task 1
		 ********************/
		HashMap<V, DirectedEdge<V>> edgeTo = new HashMap<V, DirectedEdge<V>>();
		HashMap<V, Double> distTo = new HashMap<V, Double>();
		double cost = 0.0;
		LinkedList<V> visited = new LinkedList<>();
//		Comparator<V> comparator = new Comparator<V>() {
//			public int compare(V term1, V term2) {
//				if (distTo.get(term1).doubleValue() > distTo.get(term2).doubleValue()) {
//					return 1;
//				} else if (distTo.get(term1).doubleValue() < distTo.get(term2).doubleValue()) {
//					return -1;
//				} else {
//					return 0;
//				}
//			}
//		};
//		PriorityQueue<V> queue = new PriorityQueue<>(comparator);
		PriorityQueue<V> queue = new PriorityQueue<>((v1, v2) -> Double.compare(distTo.get(v1), distTo.get(v2)));

		queue.add(start);
		distTo.put(start, 0.0);

		while (!queue.isEmpty()) {
			V v = queue.poll();
			visitedNodes++;
			if (!visited.contains(v)) {
				visited.add(v);
				if (v.equals(goal)) {
					return new Result<>(true, start, goal, getDistance(edgeTo, goal), getPath(edgeTo, goal), visitedNodes);
				}

				for (DirectedEdge<V> e : graph.outgoingEdges(v)) {
					V w = (V) e.to();
					double newdist = distTo.get(v).doubleValue() + e.weight();
					if (!distTo.containsKey(w) || distTo.get(w).doubleValue() > newdist) {
						distTo.put(w, Double.valueOf(newdist));
						edgeTo.put(w, e);
						queue.add(w);
					}
				}
			}
		}
		return new Result<>(false, start, null, -1, null, visitedNodes);
	}

	public Result<V> searchAstar(V start, V goal) {
		int visitedNodes = 0;/********************
								 * TODO: Task 3
								 ********************/
		return new Result<>(false, start, null, -1, null, visitedNodes);
	}

//	private LinkedList<V> getPath(HashMap<V, DirectedEdge<V>> map, V goal) {
//		LinkedList<V> path = new LinkedList<>();
//		V current = goal;
//		path.addFirst(goal);
//		int i = 0;
//		while() {
//			V old = current;
//			//current = map.get(old).from();
//			map.get(old).from();
//			path.addFirst(current);
//			i++;
//		}
//		return path;

	private LinkedList<V> getPath(HashMap<V, DirectedEdge<V>> map, V goal) {
		LinkedList<V> path = new LinkedList<>();
		boolean loop = true;
		while (loop) {
			path.addFirst(goal);
			try {
				goal = map.get(goal).from();
			} catch (Exception e) {
				loop = false;
			}
		}
		return path;
	}
	
	private double getDistance(HashMap<V,DirectedEdge<V>> map, V goal) {
		double cost = 0.0;
		boolean loop = true;
		while (loop) {
			try {
				cost += map.get(goal).weight();
				goal = map.get(goal).from();
			} catch (Exception e) {
				loop = false;
			}
		}
		return cost;
	}
}
