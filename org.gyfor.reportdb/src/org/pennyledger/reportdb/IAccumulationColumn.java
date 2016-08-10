package org.pennyledger.reportdb;

public interface IAccumulationColumn {

  public void accumulate(Object source);

  public void reset();

  public Object getValue();

}
