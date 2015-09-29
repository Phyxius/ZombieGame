import java.util.Objects;

/**
 * Shea Polansky
 * Class name and description goes here
 */
class Tuple<T1, T2>
{
  public final T1 value1;
  public final T2 value2;

  public Tuple(T1 value1, T2 value2)
  {
    this.value1 = value1;
    this.value2 = value2;
  }

  @Override
  public boolean equals(Object obj)
  {
    return obj != null &&
        obj instanceof Tuple<?, ?> &&
        Objects.equals(value1, ((Tuple) obj).value1) &&
        Objects.equals(value2, ((Tuple) obj).value2);
  }
}
