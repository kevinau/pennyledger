package org.pennyledger.form;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.Column;

import org.pennyledger.form.type.ICaseSettable;
import org.pennyledger.form.type.ILengthSettable;
import org.pennyledger.form.type.IMaxSettable;
import org.pennyledger.form.type.IMinSettable;
import org.pennyledger.form.type.IPatternSettable;
import org.pennyledger.form.type.IPrecisionSettable;
import org.pennyledger.form.type.IScaleSettable;
import org.pennyledger.form.type.ISignSettable;
import org.pennyledger.form.type.IType;
import org.pennyledger.form.type.builtin.BigDecimalType;
import org.pennyledger.form.type.builtin.BigIntegerType;
import org.pennyledger.form.type.builtin.BooleanType;
import org.pennyledger.form.type.builtin.ByteType;
import org.pennyledger.form.type.builtin.CharacterType;
import org.pennyledger.form.type.builtin.DateType;
import org.pennyledger.form.type.builtin.DecimalType;
import org.pennyledger.form.type.builtin.DoubleType;
import org.pennyledger.form.type.builtin.EntityLifeType;
import org.pennyledger.form.type.builtin.EnumType;
import org.pennyledger.form.type.builtin.FileContentType;
import org.pennyledger.form.type.builtin.FileType;
import org.pennyledger.form.type.builtin.FloatType;
import org.pennyledger.form.type.builtin.IntegerType;
import org.pennyledger.form.type.builtin.LocalDateType;
import org.pennyledger.form.type.builtin.LongType;
import org.pennyledger.form.type.builtin.ShortType;
import org.pennyledger.form.type.builtin.SqlDateType;
import org.pennyledger.form.type.builtin.StringType;
import org.pennyledger.form.type.builtin.TimestampType;
import org.pennyledger.form.type.builtin.URLType;
import org.pennyledger.math.Decimal;
import org.pennyledger.value.EntityLife;
import org.pennyledger.value.FileContent;

public class BuiltinRegistry {

  // private static class Entry<T> {
  // private Class<T> fieldClass;
  // private Class<? extends IType<T>> fieldTypeClass;
  //
  // private Entry(Class<T> fieldClass, Class<? extends IType<T>>
  // fieldTypeClass) {
  // this.fieldClass = fieldClass;
  // this.fieldTypeClass = fieldTypeClass;
  // }
  // }

  private static Map<Class<?>, Class<? extends IType<?>>> typeMap = new LinkedHashMap<>();

  static {
    typeMap.put(BigDecimal.class, BigDecimalType.class);
    typeMap.put(BigInteger.class, BigIntegerType.class);
    typeMap.put(Boolean.class, BooleanType.class);
    typeMap.put(Boolean.TYPE, BooleanType.class);
    typeMap.put(Byte.class, ByteType.class);
    typeMap.put(Byte.TYPE, ByteType.class);
    typeMap.put(Character.class, CharacterType.class);
    typeMap.put(Character.TYPE, CharacterType.class);
    typeMap.put(Date.class, DateType.class);
    typeMap.put(Decimal.class, DecimalType.class);
    // entries.put(Directory.class, DirectoryType.class);
    typeMap.put(Double.class, DoubleType.class);
    typeMap.put(Double.TYPE, DoubleType.class);
    // entries.put(EmailAddress.class, EmailAddressType.class);
    typeMap.put(EntityLife.class, EntityLifeType.class);
    typeMap.put(FileContent.class, FileContentType.class);
    typeMap.put(File.class, FileType.class);
    typeMap.put(Float.class, FloatType.class);
    typeMap.put(Float.TYPE, FloatType.class);
    // entries.put(ImageCode.class, ImageCodeType.class);
    typeMap.put(Integer.class, IntegerType.class);
    typeMap.put(Integer.TYPE, IntegerType.class);
    typeMap.put(LocalDate.class, LocalDateType.class);
    typeMap.put(Long.class, LongType.class);
    typeMap.put(Long.TYPE, LongType.class);
    // entries.put(Password.class, PasswordType.class);
    // entries.put(PhoneNumber.class, PhoneNumberType.class);
    typeMap.put(Short.class, ShortType.class);
    typeMap.put(Short.TYPE, ShortType.class);
    typeMap.put(java.sql.Date.class, SqlDateType.class);
    typeMap.put(String.class, StringType.class);
    typeMap.put(Timestamp.class, TimestampType.class);
    typeMap.put(URL.class, URLType.class);
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  public static IType<?> lookupType(Class<?> fieldClass, FormField fieldAnn, Column colAnn) {
    IType<?> type = null;

    // Find a type for this field.
    try {
      if (fieldAnn != null) {
        Class<?> typeClass = fieldAnn.type();
        
        // The following test is required because the simpler typeClass.equals(Void.TYPE) does not work.
        // It always returns false.
        if (!typeClass.getName().equals("java.lang.Void")) {
          type = (IType<?>)typeClass.newInstance();
        }
      }
      
      if (fieldClass.isEnum()) {
         type = new EnumType(fieldClass);
      } else {
        // TODO code value types are not supported
        Class<?> typeClass = typeMap.get(fieldClass);
        if (typeClass == null) {
          for (Map.Entry<Class<?>, Class<? extends IType<?>>> entry : typeMap.entrySet()) {
            Class<?> mapClass = entry.getKey();
            if (mapClass.isAssignableFrom(fieldClass)) {
              typeClass = entry.getValue();
              break;
            }
          }
          if (typeClass == null) {
            return null;
          }
        }
        type = (IType<?>)typeClass.newInstance();
      }
    } catch (InstantiationException | IllegalAccessException ex) {
      throw new RuntimeException(ex);
    }
    
    // and then set parameters from the FormField and Column annotations.
    // A form field annotation is tried first
    if (fieldAnn != null) {
      if (type instanceof ILengthSettable) {
        int n = fieldAnn.length();
        if (n != -1) {
          ((ILengthSettable)type).setMaxLength(n);
        }
      }
      // The order of the following is important for integers.  The following 
      // combinations are allowed:
      // - min and max
      // - sign and max
      // - sign and precision
      // - precision (assumed to be signed)
      // To get this to work, precision and max come first.  After that, min
      // and sign.
      if (type instanceof IPrecisionSettable) {
        int n = fieldAnn.precision();
        if (n != -1) {
          ((IPrecisionSettable)type).setPrecision(n);
        }
      }
      if (type instanceof IMaxSettable) {
        long max = fieldAnn.max();
        if (max != Long.MAX_VALUE) {
          ((IMaxSettable)type).setMax(max);
        }
      }
      if (type instanceof IMinSettable) {
        long min = fieldAnn.min();
        if (min != Long.MIN_VALUE) {
          ((IMinSettable)type).setMin(min);
        }
      }
      if (type instanceof ISignSettable) {
        NumberSign ns = fieldAnn.sign();
        if (ns != NumberSign.UNSPECIFIED) {
          ((ISignSettable)type).setSign(ns);
        }
      }
      if (type instanceof IScaleSettable) {
        int n = fieldAnn.scale();
        if (n != -1) {
          ((IScaleSettable)type).setScale(n);
        }
      }
      if (type instanceof IPatternSettable) {
        String pattern = fieldAnn.pattern();
        if (pattern.length() > 0) {
          // If pattern is specified, a required message and an error message
          // must also be specified
          String targetName = fieldAnn.targetName();
          ((IPatternSettable)type).setPattern(pattern, targetName);
        }
      }
      if (type instanceof ICaseSettable) {
        TextCase xcase = fieldAnn.xcase();
        if (xcase != TextCase.UNSPECIFIED) {
          ((ICaseSettable)type).setAllowedCase(xcase);
        }
      }
    } else if (colAnn != null) {
      if (type instanceof ILengthSettable) {
        int n = colAnn.length();
        if (n != 255) {
          ((ILengthSettable)type).setMaxLength(n);
        }
      }
      if (type instanceof IPrecisionSettable) {
        int n = colAnn.precision();
        if (n != 0) {
          ((IPrecisionSettable)type).setPrecision(n);
        }
      }
      if (type instanceof IScaleSettable) {
        int n = colAnn.scale();
        ((IScaleSettable)type).setScale(n);
      }
    }
    // try {
    // // Try for a method, named by default, that provides a type.
    // Method method = parentClass.getDeclaredMethod(memberName);
    // // The field must be the type of the field
    // if (IType.class.isAssignableFrom(method.getReturnType()) &&
    // method.getParameterTypes().length == 0) {
    // int modifiers = method.getModifiers();
    // if (Modifier.isStatic(modifiers)) {
    // // The method must not be annotated with an TypeFor annotation. If it
    // is
    // so
    // annotated,
    // // it is not a type providing method by convention.
    // if (method.getAnnotation(TypeFor.class) == null) {
    // method.setAccessible(true);
    // return (IType<?>)method.invoke(null);
    // }
    // }
    // }
    // } catch (NoSuchMethodException ex) {
    // // Continue.
    // } catch (InvocationTargetException ex) {
    // throw new RuntimeException(ex);
    // } catch (IllegalAccessException ex) {
    // throw new RuntimeException(ex);
    // } catch (IllegalArgumentException ex) {
    // throw new RuntimeException(ex);
    // }
    //
    // try {
    // // Otherwise, try for a field
    // Field field = parentClass.getDeclaredField(memberName);
    // // The field must be an IType field
    // if (IType.class.isAssignableFrom(field.getType())) {
    // int modifiers = field.getModifiers();
    // if (Modifier.isStatic(modifiers)) {
    // // The field must not be annotated with an TypeFor annotation. If it is
    // so
    // annotated,
    // // it is not a type providing field by convention.
    // if (field.getAnnotation(TypeFor.class) == null) {
    // field.setAccessible(true);
    // return (IType<?>)field.get(null);
    // }
    // }
    // }
    // } catch (NoSuchFieldException ex) {
    // // Continue.
    // } catch (IllegalAccessException ex) {
    // throw new RuntimeException(ex);
    // } catch (IllegalArgumentException ex) {
    // throw new RuntimeException(ex);
    // }
    // }
    return type;
  }
}
