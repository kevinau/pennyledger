package org.pennyledger.form.plan.impl;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Date;

import org.pennyledger.form.FormField;
import org.pennyledger.form.NumberSign;
import org.pennyledger.form.TextCase;
import org.pennyledger.form.TypeFor;
import org.pennyledger.form.type.BigDecimalType;
import org.pennyledger.form.type.BigIntegerType;
import org.pennyledger.form.type.BooleanType;
import org.pennyledger.form.type.ByteType;
import org.pennyledger.form.type.CharacterType;
import org.pennyledger.form.type.DateType;
import org.pennyledger.form.type.DecimalType;
import org.pennyledger.form.type.DoubleType;
import org.pennyledger.form.type.FileContentType;
import org.pennyledger.form.type.FileType;
import org.pennyledger.form.type.FloatType;
import org.pennyledger.form.type.IPrecisionableType;
import org.pennyledger.form.type.IScalableType;
import org.pennyledger.form.type.IType;
import org.pennyledger.form.type.IntegerType;
import org.pennyledger.form.type.LocalDateType;
import org.pennyledger.form.type.LongType;
import org.pennyledger.form.type.RegexTextType;
import org.pennyledger.form.type.ShortType;
import org.pennyledger.form.type.TextType;
import org.pennyledger.math.Decimal;
import org.pennyledger.value.FileContent;

public class AtomicTypeList {

  private static final int LENGTH = 1;
  private static final int PATTERN = 2;
  private static final int PRECISION = 4;
  private static final int SCALE = 8;
  private static final int XCASE = 16;
  private static final int SIGN = 32;
  private static final int PRIMITIVE = 64;
  

  static class TypeMap {
    private final Class<?> fieldClass;
    private final Class<? extends IType<?>> typeClass;
    private final int override;
    
    private TypeMap (Class<?> fieldClass, Class<? extends IType<?>> typeClass, int override) {
      this.fieldClass = fieldClass;
      this.typeClass = typeClass;
      this.override = override;
    }
    
  }
  
  
  private static final TypeMap[] typeMappings = {
    new TypeMap (String.class, TextType.class, LENGTH | PATTERN | XCASE),
    new TypeMap (LocalDate.class, LocalDateType.class, 0),
    new TypeMap (Integer.class, IntegerType.class, PRECISION | SIGN),
    new TypeMap (Short.class, ShortType.class, PRECISION | SIGN),
    new TypeMap (Long.class, LongType.class, PRECISION | SIGN),
    new TypeMap (Byte.class, ByteType.class, PRECISION | SIGN),
    new TypeMap (Decimal.class, DecimalType.class, PRECISION | SCALE | SIGN),
    new TypeMap (Double.class, DoubleType.class, PRECISION | SCALE | SIGN),
    new TypeMap (Float.class, FloatType.class, PRECISION | SCALE | SIGN),
    new TypeMap (File.class, FileType.class, 0),
    new TypeMap (Character.class, CharacterType.class, PATTERN | XCASE),
    new TypeMap (Boolean.class, BooleanType.class, 0),
    new TypeMap (Integer.TYPE, IntegerType.class, PRECISION | SIGN | PRIMITIVE),
    new TypeMap (Short.TYPE, ShortType.class, PRECISION | SIGN | PRIMITIVE),
    new TypeMap (Long.TYPE, LongType.class, PRECISION | SIGN | PRIMITIVE),
    new TypeMap (Byte.TYPE, ByteType.class, PRECISION | SIGN | PRIMITIVE),
    new TypeMap (Double.TYPE, DoubleType.class, PRECISION | SCALE | SIGN | PRIMITIVE),
    new TypeMap (Float.TYPE, FloatType.class, PRECISION | SCALE | SIGN | PRIMITIVE),
    new TypeMap (Character.TYPE, CharacterType.class, PATTERN | XCASE | PRIMITIVE),
    new TypeMap (Boolean.TYPE, BooleanType.class, PRIMITIVE),
    new TypeMap (BigDecimal.class, BigDecimalType.class, PRECISION | SCALE | SIGN),
    new TypeMap (BigInteger.class, BigIntegerType.class, PRECISION | SCALE | SIGN),
    new TypeMap (Date.class, DateType.class, 0),
    new TypeMap (FileContent.class, FileContentType.class, 0),
  };
  
  
  static IType<?> getDefaultType (FormField fieldAnn, javax.persistence.Column columnAnn, TypeMap tm) {
    IType<?> type;
    try {
      type = tm.typeClass.newInstance();
    } catch (InstantiationException e) {
      throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
    if (tm.override != 0) {          
      // Does this type allow length override
      if ((tm.override & LENGTH) != 0) {
        // Yes, so get a field length, if present.
        int length = -1;
       
        if (fieldAnn != null) {
          // Length is being overridden.  View size is assumed to be overridden as well.
          length = fieldAnn.length();
        }
        // Check javax.persistance.Column for overrides.
        if (columnAnn != null) {
          int cn = columnAnn.length();
          if (cn != 255) {
            if (length == -1) {
              length = cn;
            } else if (cn != length) {
              throw new RuntimeException("column attribute specified on both FormField and Column");
            }
          }
        }
        if (length == -1) {
          length = 255;
        }
        ((TextType)type).setMaxSize(length);
      }
      // Does this type allow a precision override
      if ((tm.override & PRECISION) != 0) {
        // Yes, so get the precision, if present.
        int precision = -1;
        int decimals = -1;
        NumberSign sign = NumberSign.SIGNED;
        
        if (fieldAnn != null) {
          precision = fieldAnn.precision();
          decimals = fieldAnn.scale();
          sign = fieldAnn.sign();
        }
        // Check javax.persistance.Column for overrides.
        if (columnAnn != null) {
          int cp = columnAnn.precision();
          int cs = columnAnn.scale();
          if (cp != -1) {
            if (precision == -1) {
              precision = cp;
              sign = NumberSign.SIGNED;
            } else if (cp != precision) {
              throw new RuntimeException("precision attribute specified on both FormField and Column");
            }
          }
          if (cs != -1) {
            if (decimals == -1) {
              decimals = cs;
            } else if (cs != decimals) {
              throw new RuntimeException("scale attribute specified on both FormField and Column");
            }
          }
        }
        // The number of decimals must be set first, as this will affect the number of digits to the left of
        // the decimal point.
        if (decimals != -1) {
          if (type instanceof IScalableType) {
            ((IScalableType)type).setDecimals(decimals);
          }
        }
        if (precision != -1) {
          if (type instanceof IPrecisionableType) {
            ((IPrecisionableType)type).setPrecision(sign, precision);
          }
        }
      }
      if ((tm.override & PATTERN) != 0) {
        // Get a pattern, if present.
        String pattern = "";
        if (fieldAnn != null) {
          pattern = fieldAnn.pattern();
        }
        if (pattern.length() != 0) {
          if (!(type instanceof RegexTextType)) {
            type = new RegexTextType(((TextType)type).getMaxSize(), pattern, pattern);
          }
          ((RegexTextType)type).setPattern(pattern);
        }
        // Ditto for any associated message.
        String message = "";
        if (fieldAnn != null) {
          message = fieldAnn.message();
        }
        if (message.length() != 0) {
          if (!(type instanceof RegexTextType)) {
            throw new RuntimeException("Cannot set message on a non-regex type");
          }
          ((RegexTextType)type).setMessage(message);
        }
      }
      if ((tm.override & XCASE) != 0) {
        // Get the allowed case, if present.
        TextCase allowedCase = TextCase.UNSPECIFIED;
        if (fieldAnn != null) {
          allowedCase = fieldAnn.xcase();
        }
        if (allowedCase != TextCase.UNSPECIFIED) {
          ((TextType)type).setAllowedCase(allowedCase);
        }
      }
    }
    if ((tm.override & PRIMITIVE) != 0) {
      type.setPrimitive(true);
    } else {
      type.setPrimitive(false);
    }
    return type;
  }
  
  
  static TypeMap getTypeMap (Class<?> fieldType) {
    for (TypeMap tm : typeMappings) {
      if (tm.fieldClass.isAssignableFrom(fieldType)) {
        return tm;
      }
    }
    return null;
  }
  
  
  static IType<?> lookupType (Class<?> parentClass, String fieldName, FormField annField) {
    String memberName = fieldName + "Type";
    
    if (parentClass != null) {
      if (annField != null) {
        // Does the FormField annotation include the name of a type member.
        Class<?> tc = annField.type();
        if (!tc.equals(Void.class)) {
          if (IType.class.isAssignableFrom(tc)) {
            IType<?> type;
            try {
              type = (IType<?>)tc.newInstance();
            } catch (InstantiationException ex) {
              throw new IllegalArgumentException("cannot instantiate IType named by 'type' attribute on @FormField", ex);
            } catch (IllegalAccessException ex) {
              throw new IllegalArgumentException("cannot instantiate IType named by 'type' attribute on @FormField", ex);
            }
            return type;
          } else {
            throw new IllegalArgumentException("'type' attribute on @FormField is not an instance of IType");
          }
        }
      }

      try {
        // Try for a method, named by default, that provides a type.
        Method method = parentClass.getDeclaredMethod(memberName);
        // The field must be the type of the field
        if (IType.class.isAssignableFrom(method.getReturnType()) && method.getParameterTypes().length == 0) {
          int modifiers = method.getModifiers();
          if (Modifier.isStatic(modifiers)) {
            // The method must not be annotated with an TypeFor annotation. If it is so annotated,
            // it is not a type providing method by convention.
            if (method.getAnnotation(TypeFor.class) == null) {
              method.setAccessible(true);
              return (IType<?>)method.invoke(null);
            }
          }
        }
      } catch (NoSuchMethodException ex) {
        // Continue.
      } catch (InvocationTargetException ex) {
        throw new RuntimeException(ex);
      } catch (IllegalAccessException ex) {
        throw new RuntimeException(ex);
      } catch (IllegalArgumentException ex) {
        throw new RuntimeException(ex);
      }

      try {
        // Otherwise, try for a field
        Field field = parentClass.getDeclaredField(memberName);
        // The field must be an IType field
        if (IType.class.isAssignableFrom(field.getType())) {
          int modifiers = field.getModifiers();
          if (Modifier.isStatic(modifiers)) {
            // The field must not be annotated with an TypeFor annotation. If it is so annotated,
            // it is not a type providing field by convention.
            if (field.getAnnotation(TypeFor.class) == null) {
              field.setAccessible(true);
              return (IType<?>)field.get(null);
            }
          }
        }
      } catch (NoSuchFieldException ex) {
        // Continue.
      } catch (IllegalAccessException ex) {
        throw new RuntimeException(ex);
      } catch (IllegalArgumentException ex) {
        throw new RuntimeException(ex);
      }
    }

    return null;
  }
  
  

}
