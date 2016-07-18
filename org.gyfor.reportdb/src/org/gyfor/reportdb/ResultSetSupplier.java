package org.gyfor.reportdb;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.gyfor.report.IReportBlock;
import org.gyfor.report.IReportGrouping;
import org.gyfor.report.ReportDetail;
import org.pennyledger.object.plan.IEntityPlan;
import org.pennyledger.object.plan.IFieldPlan;
import org.pennyledger.object.type.IType;
import org.pennyledger.util.ResettableSupplier;


public class ResultSetSupplier<P> implements ResettableSupplier<P>, AutoCloseable {

  private final Connection conn;
  private final Class<P> klass;
  private final IEntityPlan<?> entityPlan;
  private final String fromSQL;
  private final List<IType<?>> columnTypes;
  private final List<Field> columnFields;

  private ResultSet rs;
  private PreparedStatement ps;
  private StringBuilder selectSQL;
  
  private ColumnSet colSet = new ColumnSet();
  private ColumnHeadingsLevel<P> colHeadingLevel = null;
  

  public ResultSetSupplier(Connection conn, Class<P> klass, IEntityPlan<?> entityPlan, String fromSQL) {
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


  public IReportGrouping<P> simpleGroup (Function<P,Object> grouper, String... names) {
    return new SimpleGroupLevel<P>(klass, entityPlan, grouper, colSet, names);
  }
  
    
  public IReportGrouping<P> simpleColumnHeadings() {
    colHeadingLevel = new ColumnHeadingsLevel<P>();
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
    IReportBlock block = new SimpleDetailBlock(types, names, fields, colSet);
    if (colHeadingLevel != null) {
      colHeadingLevel.setHeadingLabels(labels, types, colSet);
    }
    return new ReportDetail(block);
  }
  
  
  @SuppressWarnings("null")
  @Override
  public P get() {
    try {
      if (rs == null) {
        rs = ps.executeQuery();
      }
      if (rs.next()) {
        P instance = rsInstance();
        return instance;
      } else { // cannot advance
        return (P)null;
      }
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }


  @Override
  public ResultSetSupplier<P> reset() {
    // The next get will execute the query again.
    rs = null;
    return this;
  }
  
  
  private P rsInstance() {
    P instance;
    try {
      instance = (P)klass.newInstance();
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
