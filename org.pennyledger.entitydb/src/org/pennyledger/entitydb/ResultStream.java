package org.pennyledger.entitydb;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.pennyledger.object.type.BuiltinTypeRegistry;
import org.pennyledger.object.type.IType;

public class ResultStream<T> {
  private final Connection conn;
  private final Class<T> klass;
  private final String fromSQL;
  private final List<IType<?>> columnTypes;
  private final List<Field> columnFields;
  
  private ResultSet rs;
  private PreparedStatement ps;
  private StringBuilder selectSQL;
  
  
  public ResultStream(Connection conn, Class<T> klass, String fromSQL) {
    this.conn = conn;
    this.klass = klass;
    this.fromSQL = fromSQL;
    
    this.columnTypes = new ArrayList<>();
    this.columnFields = new ArrayList<>();
    buildSelect(klass);
  }

  private void buildSelect (Class<?> klass) {
    selectSQL = new StringBuilder("SELECT ");
    Class<?> superClass = klass.getSuperclass();
    if (superClass != null && !superClass.equals(Object.class)) {
      buildSelect(superClass);
    }
    Field[] fields = klass.getDeclaredFields();
    boolean first = true;
    
    for (Field field : fields) {
      if (field.isSynthetic()) {
        continue;
      }
      if ((field.getModifiers() & (Modifier.STATIC | Modifier.FINAL | Modifier.TRANSIENT | Modifier.VOLATILE)) != 0) {
        continue;
      }
      // TODO allow for embedded classes, referenced classes, and repeating elements
      IType<?> type = BuiltinTypeRegistry.lookupType(field.getType());
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
      selectSQL.append(field.getName());
    }
  }
  
  
  public boolean tryAdvance(Consumer<? super T> action) {
    try {
      if (rs == null) {
        init();
      }
      if (rs.next()) {
        T instance = rsInstance();
        action.accept(instance);
        return true;
      }  else {            // cannot advance
        return false;
      }
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }

  
  private void init() {
    String sql = selectSQL + " " + fromSQL;
    try {
        ps = conn.prepareStatement(sql);
        rs = ps.executeQuery();

    } catch (SQLException ex) {
        System.out.println(sql);
        close();
        throw new RuntimeException(ex);
    }
  }

  
  private T rsInstance () {
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
  
  
  private void close() {
    try {
        rs.close();
        try {
            ps.close();
        } catch (SQLException e) {
            //nothing we can do here
        }
    } catch (SQLException e) {
        //nothing we can do here
    }
  }

}
