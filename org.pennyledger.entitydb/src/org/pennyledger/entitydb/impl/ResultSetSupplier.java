package org.pennyledger.entitydb.impl;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.gyfor.report.IReportBlock;
import org.gyfor.report.IReportDetail;
import org.gyfor.report.IReportGrouping;
import org.gyfor.report.ReportDetail;
import org.gyfor.report.ReportGrouping;
import org.gyfor.report.ResettableSupplier;
import org.pennyledger.object.plan.IEntityPlan;
import org.pennyledger.object.plan.IFieldPlan;
import org.pennyledger.object.type.IType;


public class ResultSetSupplier<T> implements ResettableSupplier<T>, AutoCloseable {

  private final Connection conn;
  private final Class<T> klass;
  private final IEntityPlan<?> entityPlan;
  private final String fromSQL;
  private final List<IType<?>> columnTypes;
  private final List<Field> columnFields;

  private ResultSet rs;
  private PreparedStatement ps;
  private StringBuilder selectSQL;
  
  private ColumnSet colSet = new ColumnSet();
  private ColumnHeadingsLevel<T> colHeadingLevel = null;
  

  public ResultSetSupplier(Connection conn, Class<T> klass, IEntityPlan<?> entityPlan, String fromSQL) {
    this.conn = conn;
    this.klass = klass;
    this.entityPlan = entityPlan;
    this.fromSQL = fromSQL;

    this.columnTypes = new ArrayList<>();
    this.columnFields = new ArrayList<>();
    buildSelect(klass, entityPlan);
  }


  private void buildSelect(Class<?> klass, IEntityPlan<?> template) {
    selectSQL = new StringBuilder("SELECT ");
    Class<?> superClass = klass.getSuperclass();
    if (superClass != null && !superClass.equals(Object.class)) {
      buildSelect(superClass, template);
    }
    Field[] fields = klass.getDeclaredFields();
    boolean first = true;

    for (Field field : fields) {
      String fieldName = field.getName();
      IFieldPlan<? super Object> fieldPlan = template.getFieldPlan(fieldName);
      if (fieldPlan == null) {
        throw new RuntimeException("Unknown field '" + fieldName + "' in " + template.getEntityClass());        
      }

      IType<?> type = fieldPlan.getType();
      if (type == null) {
        throw new RuntimeException("Unknown/unsupported type " + field.getType() + " in " + klass);
      }
      columnTypes.add(type);
      columnFields.add(field);

      if (first) {
        first = false;
      } else {
        selectSQL.append(',');
      }
      selectSQL.append(fieldName);
    }
    
    String sql = selectSQL + " " + fromSQL;
    try {
      ps = conn.prepareStatement(sql);
    } catch (SQLException ex) {
      System.out.println(sql);
      throw new RuntimeException(ex);
    }
  }


  @SuppressWarnings("unchecked")
  public IReportBlock simpleHeading (String... names) {
    String[] labels = new String[names.length];
    IType<Object>[] types = new IType[names.length];
    Field[] fields = new Field[names.length];
    
    int i = 0;
    for (String name : names) {
      IFieldPlan<?> fieldPlan = entityPlan.getFieldPlan(name);
      if (fieldPlan == null) {
        throw new IllegalArgumentException("'" + name + "' does not name a field of " + klass);
      }
      labels[i] = fieldPlan.getDeclaredLabel();
      types[i] = (IType<Object>)fieldPlan.getType();
      try {
        fields[i] = klass.getDeclaredField(name);
      } catch (NoSuchFieldException | SecurityException ex) {
        throw new RuntimeException(ex);
      }
      i++;
    }
    return new SimpleHeadingBlock(labels, types, fields);
  }
  
  
  public IReportGrouping<T> simpleColumnHeadings() {
    colHeadingLevel = new ColumnHeadingsLevel<T>();
    return colHeadingLevel;
  }
  
  
  @SuppressWarnings("unchecked")
  public ReportDetail simpleDetail(String... names) {
    String[] labels = new String[names.length];
    IType<Object>[] types = new IType[names.length];
    Field[] fields = new Field[names.length];
    
    int i = 0;
    for (String name : names) {
      IFieldPlan<?> fieldPlan = entityPlan.getFieldPlan(name);
      if (fieldPlan == null) {
        throw new IllegalArgumentException("'" + name + "' does not name a field of " + klass);
      }
      labels[i] = fieldPlan.getDeclaredLabel();
      types[i] = (IType<Object>)fieldPlan.getType();
      try {
        fields[i] = klass.getDeclaredField(name);
      } catch (NoSuchFieldException | SecurityException ex) {
        throw new RuntimeException(ex);
      }
      i++;
    }
    IReportBlock block = new SimpleDetailBlock(labels, types, fields, colSet);
    if (colHeadingLevel != null) {
      colHeadingLevel.setHeadingLabels(labels, types, colSet);
    }
    return new ReportDetail(block);
  }
  
  
  @SuppressWarnings("null")
  @Override
  public T get() {
    try {
      if (rs == null) {
        rs = ps.executeQuery();
      }
      if (rs.next()) {
        T instance = rsInstance();
        return instance;
      } else { // cannot advance
        return (T)null;
      }
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }


  @Override
  public ResultSetSupplier<T> reset() {
    // The next get will execute the query again.
    rs = null;
    return this;
  }
  
  
  private T rsInstance() {
    T instance;
    try {
      instance = (T)klass.newInstance();
      for (int i = 0; i < columnFields.size(); i++) {
        Object value = columnTypes.get(i).getSQLValue(rs, i + 1);
        Field field = columnFields.get(i);
        field.setAccessible(true);
        field.set(instance, value);
      }
    } catch (InstantiationException | IllegalAccessException | SQLException ex) {
      throw new RuntimeException(ex);
    }
    return instance;
  }


  @Override
  public void close() {
    try {
      rs.close();
      try {
        ps.close();
      } catch (SQLException e) {
        // nothing we can do here
      }
    } catch (SQLException e) {
      // nothing we can do here
    }
  }

}
