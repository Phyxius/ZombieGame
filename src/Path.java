import java.util.LinkedHashSet;

public class Path <T>
{
  private final LinkedHashSet<T> nodes;
  private float totalPathCost;

  public Path(LinkedHashSet<T> nodes, float totalPathCost)
  {
    this.nodes = nodes;
    this.totalPathCost = totalPathCost;
  }

  public Path()
  {
    this(new LinkedHashSet<>(), 0);
  }

  public LinkedHashSet<T> getNodes()
  {
    return new LinkedHashSet<>(nodes);
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