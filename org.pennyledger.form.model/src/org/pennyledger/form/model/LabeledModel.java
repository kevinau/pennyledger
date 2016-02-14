package org.pennyledger.form.model;

public class LabeledModel implements ILabeledModel {

  private final String label;
  private final IObjectModel model;
  
  LabeledModel (String label, IObjectModel model) {
    this.label = label;
    this.model = model;
  }
  
  
  @Override
  public String getLabel () {
    return label;
  }
  
  @Override
  public IObjectModel getModel () {
    return model;
  }
  
}
