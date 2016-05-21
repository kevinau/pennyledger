package org.gyfor.reportdb;

import java.lang.reflect.Field;
import java.util.function.Function;

import org.gyfor.report.IReportBlock;
import org.gyfor.report.IReportGrouping;
import org.pennyledger.object.plan.IEntityPlan;
import org.pennyledger.object.plan.IFieldPlan;
import org.pennyledger.object.type.IType;

public class SimpleGroupLevel<T> implements IReportGrouping<T> {
  
  private final Function<T,Object> grouper;
  private final IReportBlock logicalHeader;
  private final IReportBlock logicalFooter;
  private final IAccumulationColumn[] accumCols;
  
  @SuppressWarnings("unchecked")
  public SimpleGroupLevel (Class<T> klass, IEntityPlan<?> entityPlan, Function<T,Object> grouper, ColumnSet detailCols, String... names) {
    this.grouper = grouper;
    
    int n = 0;
    while (n < names.length && names[n] != null) {
      n++;
    }
    String[] headerLabels = new String[n];
    IType<Object>[] headerTypes = new IType[n];
    Field[] headerFields = new Field[n];
  
    // Get the header fields
    for (int i = 0; i < n; i++) {
      String name = names[i];
      IFieldPlan<?> fieldPlan = entityPlan.getFieldPlan(name);
      if (fieldPlan == null) {
        throw new IllegalArgumentException("'" + name + "' does not name a field of " + klass);
      }
      headerLabels[i] = fieldPlan.getDeclaredLabel();
      headerTypes[i] = (IType<Object>)fieldPlan.getType();
      try {
        headerFields[i] = klass.getDeclaredField(name);
      } catch (NoSuchFieldException | SecurityException ex) {
        throw new RuntimeException(ex);
      }
    }
    logicalHeader = new SimpleHeadingBlock(headerLabels, headerTypes, headerFields);

    if (n < names.length) {
      // Step over the null name
      n++;
      int m = names.length - n;
      IType<Object>[] totalTypes = new IType[m];
      String[] totalNames = new String[m];
      IAccumulationColumn[] accumCols = new IAccumulationColumn[m];

      // Get the footer fields
      int i = 0;
      while (n < names.length) {
        String name = names[n];
        String[] cleanName = new String[1];
        accumCols[i] = AccumulationColumnFactory.get(name, klass, cleanName);
        totalNames[i] = cleanName[0];
        IFieldPlan<?> fieldPlan = entityPlan.getFieldPlan(cleanName[0]);
        if (fieldPlan == null) {
          throw new IllegalArgumentException("'" + name + "' does not name a field of " + klass);
        }
        totalTypes[i] = (IType<Object>)fieldPlan.getType();
        i++;
        n++;
      }
      logicalFooter = new SimpleTotalsBlock(totalTypes, totalNames, accumCols, detailCols);
      this.accumCols = accumCols;
    } else {
      logicalFooter = null;
      this.accumCols = new IAccumulationColumn[0];
    }
  }

  @Override
  public Object getGroup(T source) {
    return grouper.apply(source);
  }

  @Override
  public IReportBlock getLogicalHeader() {
    return logicalHeader;
  }

  @Override
  public IReportBlock getLogicalFooter() {
    return logicalFooter;
  }

  @Override
  public void setData (T data) {
    logicalHeader.setData(data);
    if (logicalFooter != null) {
      logicalFooter.setData(data);
    }
  }

  
  @Override
  public void accumulate (T data) {
    for (IAccumulationColumn accumCol : accumCols) {
      accumCol.accumulate(data);
    }
  }
  
  
  @Override
  public void reset () {
    for (IAccumulationColumn accumCol : accumCols) {
      accumCol.reset();
    }
  }
  
  
  @Override
  public void finalizeWidths() {
  }

}
