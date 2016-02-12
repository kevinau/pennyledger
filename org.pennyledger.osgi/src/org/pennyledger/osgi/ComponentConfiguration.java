package org.pennyledger.osgi;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.regex.Pattern;

import javax.activation.UnsupportedDataTypeException;

import org.osgi.service.component.ComponentContext;

public class ComponentConfiguration {

  public static void load(Object target, ComponentContext context) {
    if (context != null) {
      Dictionary<String, Object> dict = context.getProperties();

      for (Enumeration<String> e = dict.keys(); e.hasMoreElements(); ) {
        String n = e.nextElement();
        Object v = dict.get(n);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>> "+ n + "=" + v);
      }
      try {
        Class<?> klass = target.getClass();
        Field[] fields = klass.getDeclaredFields();
        for (Field field : fields) {
          Configurable configAnn = field.getAnnotation(Configurable.class);
          if (configAnn != null) {
            String propertyName = configAnn.value();
            if (propertyName.length() == 0) {
              propertyName = field.getName();
            }
            String propertyValue = (String)dict.get(propertyName);
            if (propertyValue != null) {
              if (configAnn.required()) {
                throw new RuntimeException("Configuration value '" + propertyName + "' required for " + klass.getSimpleName());
              } else {
                Object fieldValue = getFieldValue(field.getType(), propertyValue);
                field.setAccessible(true);
                field.set(target, fieldValue);
              }
            }
          }
        }
      } catch (InstantiationException | IllegalAccessException | SecurityException | IllegalArgumentException
          | InvocationTargetException | UnsupportedDataTypeException ex) {
        throw new RuntimeException(ex);
      }
    }
  }

  private static Object getFieldValue(Class<?> type, String propertyValue) throws InstantiationException,
      IllegalAccessException, IllegalArgumentException, InvocationTargetException, UnsupportedDataTypeException {
    Object value;
    if (type.isEnum()) {
      value = getPropertyAsEnum(propertyValue, type);
    } else if (Path.class.isAssignableFrom(type)) {
      value = Paths.get(propertyValue);
    } else if (Pattern.class.isAssignableFrom(type)) {
      value = Pattern.compile(propertyValue);
    } else {
      try {
        Constructor<?> constructor = type.getDeclaredConstructor(String.class);
        constructor.setAccessible(true);
        value = constructor.newInstance(propertyValue);
      } catch (NoSuchMethodException ex) {
        if (String.class.isAssignableFrom(type)) {
          value = (String)propertyValue;
        } else {
          throw new UnsupportedDataTypeException(type.getCanonicalName());
        }
      }
    }
    return value;
  }

  private static <E> E getPropertyAsEnum(String property, Class<E> enumClass) {
    E[] values = enumClass.getEnumConstants();
    for (E value : values) {
      if (value.toString().equals(property)) {
        return value;
      }
    }
    try {
      int i = Integer.parseInt(property);
      if (i >= 0 && i < values.length) {
        return values[i];
      } else {
        throw new IllegalArgumentException(property);
      }
    } catch (NumberFormatException ex) {
      throw new IllegalArgumentException(property);
    }
  }

}
