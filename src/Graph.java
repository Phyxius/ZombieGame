import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * Created by Shea on 2015-09-18.
 * Graph: a generic weight, undirected graph object, with A* pathfinding.
 */
public class Graph<T>
{
  private Map<UnorderedTuple, Optional<Float>> edges = new HashMap<>();
  private Set<T> nodes = new HashSet<>();

  /**
   * Creates a new empty Graph
   */
  public Graph()
  {

  }

  /**
   * Creates a copy of a given graph
   *
   * @param copy the graph to copy
   */
  public Graph(Graph<T> copy)
  {
    nodes = new HashSet<>(copy.getNodes());
    edges = new HashMap<>(copy.getEdges());
  }

  /**
   * Concatenates the given graphs.
   * This method has no side effects.
   *
   * @param graphs the graphs to combine
   * @param <T>    the type of nodes in the graphs
   * @return a new combined graph
   */
  @SafeVarargs
  public static <T> Graph<T> concatenate(Graph<T>... graphs)
  {
    Graph<T> ret = new Graph<>();
    for (Graph<T> graph : graphs)
    {
      ret.add(graph);
    }
    return ret;
  }

  /**
   * Adds the given node to the graph with no edges.
   *
   * @param node the node to add
   */
  public void add(T node)
  {
    nodes.add(node);
  }

  /**
   * Adds the given nodes to the graph, with no edges.
   *
   * @param nodes the Collection of nodes to add
   */
  public void add(Collection<T> nodes)
  {
    this.nodes.addAll(nodes);
  }

  /**
   * Adds a given node to the graph, connected to the given node with the given weight
   *
   * @param node          the node to add
   * @param connectedNode the node it is connected to
   * @param edgeWeight    the weight of the connection
   */
  public void add(T node, T connectedNode, float edgeWeight)
  {
    add(node);
    setEdge(node, connectedNode, edgeWeight);
  }

  /**
   * Adds the given node to the graph with the given edges
   *
   * @param node  the node to add
   * @param edges a map of nodes to edge weights. Each key in the Map will be
   *              added as a connection to <code>node</code> with the weight
   *              being the corresponding value.
   */
  public void add(T node, Map<T, Optional<Float>> edges)
  {
    add(node);
    setEdges(node, edges);
  }

  /**
   * Adds all nodes and their edges to the graph, overwriting the weights of any existing connections
   *
   * @param graph the graph to add
   */
  void add(Graph<T> graph)
  {
    nodes.addAll(graph.getNodes());
    edges.putAll(graph.getEdges());
  }

  /**
   * @param node1 the first node to check
   * @param node2 the second node to check
   * @return true if the two nodes are neighbors (i.e. there exists a direct connection
   * between the two), false otherwise
   */
  public boolean areNeighbors(T node1, T node2)
  {
    return edges.getOrDefault(new UnorderedTuple(node1, node2), Optional.empty()).isPresent();
  }

  /**
   * Finds a path using A* between the two nodes using the given Heuristic.
   * The Heuristic will be called with the 'current' node as its first argument
   * and with the end node as its second. It should return a number that is smaller the
   * 'closer' the object is to the goal nodes. For grid-based pathfinding, Manhattan or Euclidean distance
   * is recommended as a heuristic.
   * If Heuristic is the Identity function, the algorithm will reduce to naive breadth-first search
   *
   * @param startNode the start point
   * @param endNode   the end point
   * @param heuristic the heuristic function to use
   * @return An Optional containing a Path from startNode to endNode, or Optional.empty() if no such path exists.
   * The path will have no elements and a total length of zero if startNode.equals(endNode)
   */
  public Optional<Path<T>> findPath(T startNode, T endNode, BiFunction<T, T, Float> heuristic)
  {
    if (!nodes.contains(startNode) || !nodes.contains(endNode)) return Optional.empty();
    if (startNode.equals(endNode)) return Optional.of(new Path<>());
    Map<T, Float> costsSoFar = new HashMap<>();
    Map<T, T> previousNodes = new HashMap<>();
    Queue<T> frontier =
        new PriorityQueue<>(Comparator.comparing(node -> costsSoFar.get(node) + heuristic.apply(node, endNode)));
    frontier.add(startNode);
    costsSoFar.put(startNode, 0f);
    while (!frontier.isEmpty())
    {
      T currentNode = frontier.remove();
      if (currentNode.equals(endNode)) break;
      Collection<T> neighbors = getNeighbors(currentNode);
      for (T neighbor : neighbors)
      {
        float newCost =
            costsSoFar.getOrDefault(currentNode, Float.MAX_VALUE) + getEdgeWeight(currentNode, neighbor).get();
        if (newCost >= costsSoFar.getOrDefault(neighbor, Float.MAX_VALUE)) continue;
        costsSoFar.put(neighbor, newCost);
        previousNodes.put(neighbor, currentNode);
        frontier.add(neighbor);
      }
    }
    ArrayList<T> path = new ArrayList<>();
    T currentNode = endNode, previousNode;
    while ((previousNode = previousNodes.get(currentNode)) != null)
    {
      path.add(currentNode);
      currentNode = previousNode;
    }
    if (path.isEmpty()) return Optional.empty();
    //path.add(startNode);
    Collections.reverse(path);
    return Optional.of(new Path<>(new LinkedHashSet<>(path), costsSoFar.get(endNode)));
  }

  /**
   * Returns an Optional containing (if applicable) the weight of the edge between the two nodes
   *
   * @param node1 the first node
   * @param node2 the second node
   * @return an Optional containing the edge weight between the two nodes, or Optional.empty() if no
   * connection exists.
   */
  public Optional<Float> getEdgeWeight(T node1, T node2)
  {
    return edges.getOrDefault(new UnorderedTuple(node1, node2), Optional.empty());
  }

  /**
   * @return a mapping of node tuples to edge weights. An empty optional
   * means no connection.
   * Not all combinations of nodes will necessarily be represented.
   */
  Map<UnorderedTuple, Optional<Float>> getEdges()
  {
    return new HashMap<>(edges);
  }

  /**
   * Sets the edges and associated weights
   *
   * @param edges A map of UnorderedTuples to edge weights. Each UnorderedTuple will
   *              be added to the edge list with the given weight.
   */
  public void setEdges(Map<UnorderedTuple, Optional<Float>> edges)
  {
    edges.entrySet().forEach(e -> setEdge(e.getKey().a, e.getKey().b, e.getValue()));
  }

  /**
   * Returns all nodes connected to the given node
   *
   * @param node the node whose neighbors to get
   * @return the Collection of neighbors
   */
  private Collection<T> getNeighbors(T node)
  {
    return nodes.stream().filter(n -> edges.getOrDefault(new UnorderedTuple(node, n), Optional.empty()).isPresent())
        .collect(Collectors.toList());
  }

  /**
   * @return every Node in the graph
   */
  Collection<T> getNodes()
  {
    return new ArrayList<>(nodes);
  }

  /**
   * Removes the node from the graph, along with any assocaited connections
   *
   * @param node the node to remove
   */
  public void remove(T node)
  {
    nodes.remove(node);
    edges.keySet().stream().filter(t -> t.a.equals(node) || t.b.equals(node)).forEach(edges::remove);
  }

  /**
   * Removes the edge between the two nodes
   *
   * @param node1 the first node
   * @param node2 the second node
   */
  public void removeEdge(T node1, T node2)
  {
    edges.remove(new UnorderedTuple(node1, node2));
  }

  /**
   * Removes every edge in the collection
   *
   * @param edges the Collection of edges to remove
   */
  public void removeEdges(Collection<UnorderedTuple> edges)
  {
    edges.forEach(this.edges::remove);
  }

  /**
   * Sets the edge weight (or removes the edge) between two nodes
   *
   * @param node1  the first node
   * @param node2  the second node
   * @param weight the new weight (or Optional.empty() for no connection)
   */
  private void setEdge(T node1, T node2, Optional<Float> weight)
  {
    if (weight == null) throw new IllegalArgumentException("Weights cannot be null!");
    nodes.add(node1);
    nodes.add(node2);
    edges.put(new UnorderedTuple(node1, node2), weight);
  }

  /**
   * Sets the edge weight between two nodes
   *
   * @param node1  the first node
   * @param node2  the second node
   * @param weight the weight of the edge between them
   */
  public void setEdge(T node1, T node2, float weight)
  {
    setEdge(node1, node2, Optional.of(weight));
  }

  /**
   * Sets the connections and associated weights to the given node
   *
   * @param node  the node whose connections to set
   * @param edges A map of other nodes to edge weights. Each key in the Map
   *              will be added as a connection to <code>node</code> with the weight given
   *              by the associated value.
   */
  private void setEdges(T node, Map<T, Optional<Float>> edges)
  {
    edges.entrySet().forEach(e -> setEdge(node, e.getKey(), e.getValue()));
  }

  /**
   * An unordered tuple, i.e. Tuple(a,b)=Tuple(b,a)
   */
  public final class UnorderedTuple
  {
    public final T a, b;

    /**
     * Creates a new Tuple with the given contents
     *
     * @param a the first element
     * @param b the second element
     */
    public UnorderedTuple(T a, T b)
    {
      this.a = a;
      this.b = b;
    }

    @Override
    public boolean equals(Object o)
    {
      if (!(o instanceof Graph<?>.UnorderedTuple)) return false;
      Graph<?>.UnorderedTuple other = (Graph<?>.UnorderedTuple) o;
      return (Objects.equals(a, other.a) && Objects.equals(b, other.b)) ||
          (Objects.equals(a, other.b) && Objects.equals(b, other.a));
    }

    @Override
    public int hashCode()
    {
      return a.hashCode() + b.hashCode();
    }
  }
}
