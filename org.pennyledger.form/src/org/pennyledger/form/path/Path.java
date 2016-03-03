package org.pennyledger.form.path;

public class Path extends StepPath implements IPathExpression {

  private final String source;
  
  public Path (String source) {
    this.source = source;
  }
  
  public String getSource() {
    return source;
  }
  
}
