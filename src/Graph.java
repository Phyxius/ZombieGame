import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Shea on 2015-09-18.
 * Class name and description go here.
 */
public class Graph<T>
{
  private HashMap<UnorderedTuple, Optional<Integer>> edges = new HashMap<>();
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
  }

  public Collection<T> getNodes()
  {
    return new ArrayList<>(nodes);
  }

  public Map<UnorderedTuple, Optional<Integer>> getEdges()
  {
    return new HashMap<>(edges);
  }

  public void add(T node)
  {
    nodes.add(node);
  }

  public void add(T node, T connectedNode, int edgeWeight)
  {
    add(node);
    setEdge(node, connectedNode, edgeWeight);
  }

  public void add(T node, Map<T, Optional<Integer>> edges)
  {
    add(node);
    setEdges(node, edges);
  }

  public void remove(T node)
  {
    nodes.remove(node);
    edges.keySet().stream().filter(t -> t.a.equals(node) || t.b.equals(node)).forEach(edges::remove);
  }

  public void setEdge(T node1, T node2, Optional<Integer> weight)
  {
    if (weight == null) throw new IllegalArgumentException("Weights cannot be null!");
    nodes.add(node1);
    nodes.add(node2);
    edges.put(new UnorderedTuple(node1, node2), weight);
  }

  public void setEdge(T node1, T node2, int weight)
  {
    setEdge(node1, node2, Optional.of(weight));
  }

  public void setEdges(T node, Map<T, Optional<Integer>> edges)
  {
    edges.entrySet().forEach(e -> setEdge(node, e.getKey(), e.getValue()));
  }

  public void setEdges(Map<UnorderedTuple, Optional<Integer>> edges)
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

  public Optional<Integer> getEdgeWeight(T node1, T node2)
  {
    return edges.getOrDefault(new UnorderedTuple(node1, node2), Optional.empty());
  }
}
