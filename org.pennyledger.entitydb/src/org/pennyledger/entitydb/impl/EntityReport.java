package org.pennyledger.entitydb.impl;

import org.gyfor.report.IReportGrouping;
import org.pennyledger.object.plan.IEntityPlan;
import org.pennyledger.object.plan.IFieldPlan;

public class EntityReport {

  private final IEntityPlan<?> entityPlan;
  
  private double maxPageLabelWidth = 0;
  
  
  public EntityReport (Class<?> selectionClass, IEntityPlan<?> entityPlan) {
    this.entityPlan = entityPlan;
  }
  
  
  public IReportGrouping getPageHeading (String columnName) {
    IFieldPlan<?> field = entityPlan.getFieldPlan(columnName);
    String label = field.getDeclaredLabel();
    double width = pageHeadingFont.getAdvance(label);
    maxPageLabelWidth = Math.max(maxPageLabelWidth, width);
  }
  
  

}
