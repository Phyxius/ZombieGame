/**
 * Created by rashid on 9/23/2015.
 * This interface marks any objects as a Trap detonator.
 * @see Trap
 */
public interface Detonator
{
  /**
   * Determines whether the trigger is activated.
   * @return True if the trigger is activated otherwise false.
   */
  public boolean trigger();
}
