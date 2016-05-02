package org.pennyledger.entitydb;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.pennyledger.object.type.BuiltinTypeRegistry;
import org.pennyledger.object.type.IType;

public class ResultSetSupplier<T> implements Supplier<T>, AutoCloseable {
  
  private final Class<?> klass;
  private final ResultSet rs;
  
  private Map<String, IType<?>> columnTypes;
  private Map<String, Field> columnFields;
  

  public ResultSetSupplier(Class<T> klass, ResultSet rs) {
    this.klass = klass;
    this.rs = rs;
  }

  
//  private String[] getAliases (Class<?>... klasses) {
//    if (klasses.length == 1) {
//      return new String[] {klasses[0].getName().substring(0, 1).toLowerCase()};
//    }
//    String[] aliases = new String[klasses.length];
//    Map<String, Integer> letterCount = new LinkedHashMap<>(10);
//    for (int i = 0; i < klasses.length; i++) {
//      Class<?> klass = klasses[i];
//      String alias = klass.getName().substring(0, 1).toLowerCase();
//      Integer count = letterCount.get(alias);
//      if (count == null) {
//        aliases[i] = alias;
//        letterCount.put(alias, 1);
//      } else {
//        aliases[i] = alias + count;
//        letterCount.put(alias, count + 1);
//      }
//    }
//    return aliases;
//  }
  
  
  private void getFields (Class<?> klass) {
    Class<?> superClass = klass.getSuperclass();
    if (superClass != null && !superClass.equals(Object.class)) {
      getFields(superClass);
    }
    Field[] fields = klass.getDeclaredFields();
    
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
      String name = field.getName().toLowerCase();
      columnTypes.put(name, type);
      columnFields.put(name, field);
    }
  }
  
  
  @Override
  public T get() {
    try {
      if (columnTypes == null) {
        init();
      }
      if (rs.next()) {
        T instance = rsInstance();
        return instance;
      }  else {            // cannot advance
        return null;
      }
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }

  
  private void init() {
    this.columnTypes = new HashMap<>();
    this.columnFields = new HashMap<>();
    getFields(klass);
  }

  
  @SuppressWarnings("unchecked")
  private T rsInstance () {
    T instance;
    try {
      instance = (T)klass.newInstance();

      
      ResultSetMetaData md = rs.getMetaData();
      int n = md.getColumnCount();
      for (int i = 0; i < n; i++) {
        String colName = md.getColumnName(i + 1).toLowerCase();
        IType<?> colType = columnTypes.get(colName);
        if (colType == null) {
          throw new RuntimeException("SQL select column '" + colName + "' does not match a field in " + klass);
        }
        Object value = colType.getSQLValue(rs, i + 1);
        Field field = columnFields.get(colName);
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
    } catch (SQLException e) {
      //nothing we can do here
    }
  }
  
}
