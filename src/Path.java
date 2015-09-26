import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class Path <T>
{
  private final ArrayList<T> nodes;
  private float totalPathCost;

  public Path(Set<T> nodes, float totalPathCost)
  {
    this.nodes = new ArrayList<>(nodes);
    this.totalPathCost = totalPathCost;
  }

  public Path()
  {
    this(new LinkedHashSet<>(), 0);
  }

  public ArrayList<T> getNodes()
  {
    return new ArrayList<>(nodes);
  }

  public float getTotalPathCost()
  {
    return totalPathCost;
  }

  public void append(Path<T> path)
  {
    nodes.addAll(path.getNodes());
    totalPathCost += path.getTotalPathCost();
  }

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