package org.pennyledger.form.test.model2;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class Test2 {

  private static class Y {
    private String fieldx;
    
    private String fieldy;
  }
  
  private static class X {
    private int field0;
    
    private Integer field1;
    
    private int[] field2;
    
    private Integer[] field3;
    
    private List<Integer> field4;
    
    private List<Integer[]> field5;
    
    private List<List<Integer>> field6;
    
    private List<Integer>[] field7;
    
    private Y field8;
  }
  
  public void buildObjectPlan (Type type) {
    if (type instanceof GenericArrayType) {
      System.out.print("[]");
      Type type1 = ((GenericArrayType)type).getGenericComponentType();
      buildObjectPlan (type1);
    } else if (type instanceof ParameterizedType) {
      ParameterizedType ptype = (ParameterizedType)type;
      Type type1 = ptype.getRawType();
      if (type1.equals(List.class)) {
        Type[] typeArgs = ptype.getActualTypeArguments();
        if (typeArgs.length == 0) {
          throw new IllegalArgumentException("List must have a type parameter");
        }
        System.out.print("List<>");
        Type type2 = typeArgs[0];
        buildObjectPlan (type2);
      } else {
        throw new IllegalArgumentException("Parameterized type that is not a List");
      }
    } else if (type instanceof Class) {
      Class<?> klass = (Class<?>)type;
      if (klass.isArray()) {
        System.out.print("[]");
        Type type1 = klass.getComponentType();
        buildObjectPlan (type1);
      } else if (klass.isPrimitive()) {
        System.out.print(klass.getName().toLowerCase());
      } else if (klass.isAssignableFrom(Integer.class) || klass.isAssignableFrom(String.class)) {
        System.out.print(klass.getName());
      } else {
        System.out.println("{");
        for (Field f : klass.getDeclaredFields()) {
          System.out.print(f.getName() + ": ");
          Type type1 = f.getGenericType();
          buildObjectPlan (type1);
          System.out.println();
        }
        System.out.println("}");
      }
    }
  }
  
  
  public void buildGroupPlan (Class<?> groupClass) {
    System.out.println("{");
    for (Field f : groupClass.getDeclaredFields()) {
      System.out.print(f.getName() + ": ");
      Type type1 = f.getGenericType();
      buildObjectPlan (type1);
      System.out.println();
    }
    System.out.println("}");
  }
  
  
  public void buildArrayPlan (Type elemType, int size) {
    System.out.print("[]");
    buildObjectPlan (elemType);
  }
  
  
  public void buildListPlan (Type elemType) {
    System.out.print("List<>");
    buildObjectPlan (elemType);
  }
  
  
  public void main (String[] args) {
    Test2 test2 = new Test2();
    X[] test3 = new X[1];
    test2.buildObjectPlan(X.class);
    test2.buildObjectPlan(test3.getClass());
    List<X> test4 = new ArrayList<X>();
  }
}
