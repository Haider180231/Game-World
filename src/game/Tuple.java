package game;

/**
 * A generic tuple class to hold a pair of values.
 *
 * @param <X> the type of the first value
 * @param <Y> the type of the second value
 */
public class Tuple<X, Y> {
  public final X first;
  public final Y second;

  /**
   * Constructs a tuple with the specified values.
   *
   * @param first  the first value
   * @param second the second value
   */
  public Tuple(X first, Y second) {
    this.first = first;
    this.second = second;
  }
  
  /**
   * Retrieves the first value of the tuple.
   *
   * @return the first value
   */
  public X getFirst() {
    return first;
  }
  
  /**
   * Retrieves the second value of the tuple.
   *
   * @return the second value
   */
  public Y getSecond() {
    return second;
  }

  @Override
  public String toString() {
    return "(" + first + ", " + second + ")";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Tuple<?, ?> tuple = (Tuple<?, ?>) o;

    if (!first.equals(tuple.first)) {
      return false;
    }
    return second.equals(tuple.second);
  }

  @Override
  public int hashCode() {
    int result = first.hashCode();
    result = 31 * result + second.hashCode();
    return result;
  }
}
