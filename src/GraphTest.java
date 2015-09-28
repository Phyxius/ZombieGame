import junit.framework.TestCase;

import java.util.*;

/**
 * Shea Polansky
 * GraphTest: Graph unit tests.
 */
public class GraphTest extends TestCase
{
  Graph<String> graph = new Graph<>();
  public void setUp() throws Exception
  {
    super.setUp();
    graph.add(Arrays.asList(new String[]{"A", "B", "C", "D", "E", "F", "G"}));
    graph.add("A", "B", 3);
    graph.add("A", "C", 7);
    graph.add("B", "C", 3);
    graph.add("C", "D", 8);
    graph.add("C", "E", 9);
    graph.add("D", "E", 1);
    graph.add("E", "H", 1);
    graph.add("E", "F", 2);
    graph.add("F", "G", 2);
  }

  public void testFindPath() throws Exception
  {
    Optional<Path<String>> result = graph.findPath("A", "H", (a, b) -> 0f);
    assertTrue(result.isPresent());
    ArrayList<String> path = result.get().getNodes();
    float purportedTotalDistance = result.get().getTotalPathCost();
    assertTrue(result.get().getNodes().size() > 0);
    assertEquals(path.get(path.size() - 1), "H");
    assertEquals(path.get(0), "A");
    float realDistance = 0;
    for(int i = 0; i < path.size() - 1; i++)
    {
      assertTrue(graph.getEdgeWeight(path.get(i), path.get(i + 1)).isPresent());
      realDistance += graph.getEdgeWeight(path.get(i), path.get(i + 1)).get();
    }
    assertEquals(purportedTotalDistance, realDistance);
  }

  public void testAreNeighbors() throws Exception
  {
    assertTrue(graph.areNeighbors("B", "A"));
    assertTrue(graph.areNeighbors("E", "H"));
    assertFalse(graph.areNeighbors("A", "H"));
  }
}