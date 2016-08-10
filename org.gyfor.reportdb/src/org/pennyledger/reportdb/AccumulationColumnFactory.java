package org.pennyledger.reportdb;

import java.lang.reflect.Field;

public class AccumulationColumnFactory {

  public static IAccumulationColumn get(String namex, Class<?> klass, String[] cleanName) {
    try {
      String name;
      Field field;
      IAccumulationColumn accumCol;
      
      switch (namex.charAt(0)) {
      case '#' :
        name = namex.substring(1);
        field = klass.getDeclaredField(name);
        accumCol = new DecimalCount();
      case '+' :
        name = namex.substring(1);
        field = klass.getDeclaredField(name);
        accumCol = new DecimalSum(field);
      case '~' :
        name = namex.substring(1);
        field = klass.getDeclaredField(name);
        accumCol = new DecimalAve(field);
      case '<' :
        name = namex.substring(1);
        field = klass.getDeclaredField(name);
        accumCol = new ComparableMin(field);
      case '>' :
        name = namex.substring(1);
        field = klass.getDeclaredField(name);
        accumCol = new ComparableMax(field);
      case '.' :
        name = namex.substring(1);
        field = klass.getDeclaredField(name);
        accumCol = new ObjectFirst(field);
      case ':' :
        name = namex.substring(1);
        field = klass.getDeclaredField(name);
        accumCol = new ObjectLast(field);
      default :
        int i = namex.indexOf('/');
        if (i == -1) {
          name = namex;
          field = klass.getDeclaredField(name);
          accumCol = new DecimalSum(field);
        } else {
          name = namex.substring(1, i);
          field = klass.getDeclaredField(name);
          String name2 = namex.substring(i + 1);
          Field field2 = klass.getDeclaredField(name2);
          accumCol = new DecimalWeightedAve(field, field2);
        }
      }
      cleanName[0] = name;
      return accumCol;
    } catch (NoSuchFieldException | SecurityException ex) {
      throw new RuntimeException(ex);
    }
  }

}
