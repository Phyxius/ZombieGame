/**
 * Created by Mohammad R. Yousefi on 9/23/2015.
 * Provides the functionality of a simple detonator.
 *
 * @see Trap
 */
interface Detonator
{
  /**
   * Simulates the press of a trigger.
   *
   * @return Returns true upon a successful activation of the detonator.
   */
  default boolean trigger()
  {
    return true;
  }
}
