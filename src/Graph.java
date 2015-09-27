import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * Created by Shea on 2015-09-18.
 * Class name and description go here.
 */
public class Graph<T>
{
  private Map<UnorderedTuple, Optional<Float>> edges = new HashMap<>();
  private Set<T> nodes = new HashSet<>();
  public final class UnorderedTuple
  {
    public final T a, b;

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

  public Graph()
  {

  }

  public Graph(Graph<T> copy)
  {
    nodes = new HashSet<>(copy.getNodes());
    edges = new HashMap<>(copy.getEdges());
  }

  public Collection<T> getNodes()
  {
    return new ArrayList<>(nodes);
  }

  public Map<UnorderedTuple, Optional<Float>> getEdges()
  {
    return new HashMap<>(edges);
  }

  public void add(T node)
  {
    nodes.add(node);
  }

  public void add(Collection<T> nodes)
  {
    this.nodes.addAll(nodes);
  }

  public void add(T node, T connectedNode, float edgeWeight)
  {
    add(node);
    setEdge(node, connectedNode, edgeWeight);
  }

  public void add(T node, Map<T, Optional<Float>> edges)
  {
    add(node);
    setEdges(node, edges);
  }

  public void add(Graph<T> graph)
  {
    nodes.addAll(graph.getNodes());
    edges.putAll(graph.getEdges());
  }

  public void remove(T node)
  {
    nodes.remove(node);
    edges.keySet().stream().filter(t -> t.a.equals(node) || t.b.equals(node)).forEach(edges::remove);
  }

  public void setEdge(T node1, T node2, Optional<Float> weight)
  {
    if (weight == null) throw new IllegalArgumentException("Weights cannot be null!");
    nodes.add(node1);
    nodes.add(node2);
    edges.put(new UnorderedTuple(node1, node2), weight);
  }

  public void setEdge(T node1, T node2, float weight)
  {
    setEdge(node1, node2, Optional.of(weight));
  }

  public void setEdges(T node, Map<T, Optional<Float>> edges)
  {
    edges.entrySet().forEach(e -> setEdge(node, e.getKey(), e.getValue()));
  }

  public void setEdges(Map<UnorderedTuple, Optional<Float>> edges)
  {
    edges.entrySet().forEach(e -> setEdge(e.getKey().a, e.getKey().b, e.getValue()));
  }

  public void removeEdge(T node1, T node2)
  {
    edges.remove(new UnorderedTuple(node1, node2));
  }

  public void removeEdges(Collection<UnorderedTuple> edges)
  {
    edges.forEach(this.edges::remove);
  }

  public Collection<T> getNeighbors(T node)
  {
    return nodes.stream().filter(
        n -> edges.getOrDefault(new UnorderedTuple(node, n), Optional.empty()).isPresent())
        .collect(Collectors.toList());
  }

  public boolean areNeighbors(T node1, T node2)
  {
    return edges.getOrDefault(new UnorderedTuple(node1, node2), Optional.empty()).isPresent();
  }

  public Optional<Float> getEdgeWeight(T node1, T node2)
  {
    return edges.getOrDefault(new UnorderedTuple(node1, node2), Optional.empty());
  }

  public Optional<Path<T>> findPath(T startNode, T endNode, BiFunction<T, T, Float> heuristic)
  {
    if (!nodes.contains(startNode) || !nodes.contains(endNode)) return Optional.empty();
    if (startNode.equals(endNode)) return Optional.of(new Path<>());
    Map<T, Float> costsSoFar = new HashMap<>();
    Map<T, T> previousNodes = new HashMap<>();
    Queue<T> frontier = new PriorityQueue<>(
        Comparator.comparing(node -> costsSoFar.get(node) + heuristic.apply(node, endNode)));
    frontier.add(startNode);
    costsSoFar.put(startNode, 0f);
    while(!frontier.isEmpty())
    {
      T currentNode = frontier.remove();
      if (currentNode.equals(endNode)) break;
      Collection<T> neighbors = getNeighbors(currentNode);
      for (T neighbor : neighbors)
      {
        float newCost = costsSoFar.getOrDefault(currentNode, Float.MAX_VALUE) + getEdgeWeight(currentNode, neighbor).get();
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
}
