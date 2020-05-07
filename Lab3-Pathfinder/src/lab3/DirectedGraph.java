package lab3;


import java.util.List;


public interface DirectedGraph<V> {

    public List<DirectedEdge<V>> outgoingEdges(V v);

    public double guessCost(V v, V w);

}
