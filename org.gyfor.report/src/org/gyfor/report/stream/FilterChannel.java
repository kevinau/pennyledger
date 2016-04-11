package org.gyfor.report.stream;

import java.util.function.Predicate;

public class FilterChannel<T> implements IChannel {

  private final Predicate<T> p;
  
  public FilterChannel (Predicate<T> p) {
    this.p = p;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Object process(Object arg) {
    if (p.test((T)arg)) {
      return arg;
    }
    return null;
  }

}
