import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Path: A Path between two graph nodes
 * @param <T> the type of the graph nodes
 */
public class Path <T>
{
  private final ArrayList<T> nodes;
  private float totalPathCost;

  /**
   * Creates a new Path with the given set of nodes visited, and the given total cost
   * @param nodes the Set of nodes to visit
   * @param totalPathCost the total cost of the path
   */
  public Path(Set<T> nodes, float totalPathCost)
  {
    this.nodes = new ArrayList<>(nodes);
    this.totalPathCost = totalPathCost;
  }

  /**
   * Creates an empty path with zero cost
   */
  public Path()
  {
    this(new LinkedHashSet<>(), 0);
  }

  /**
   * @return the nodes in the path
   */
  public ArrayList<T> getNodes()
  {
    return new ArrayList<>(nodes);
  }

  /**
   * @return The total cost in the path
   */
  public float getTotalPathCost()
  {
    return totalPathCost;
  }

  /**
   * Appends the given path to this one, adding its nodes after
   * this path's nodes and its cost to this path's cost
   * @param path the path to add
   */
  public void append(Path<T> path)
  {
    nodes.addAll(path.getNodes());
    totalPathCost += path.getTotalPathCost();
  }

  /**
   * Concatenates the given paths, returning a new one
   * containing the result.
   * This operation is stateless.
   * @param paths the paths to concatenate.
   * @param <T> the type of nodes the paths contain.
   * @return the resulting path.
   */
  @SafeVarargs
  public static <T> Path concatenate(Path<T>... paths)
  {
    Path<T> ret = new Path<>();
    for (Path<T> path : paths)
    {
      ret.append(path);
    }

    return ret;
  }
}