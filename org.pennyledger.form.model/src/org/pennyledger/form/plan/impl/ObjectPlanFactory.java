package org.pennyledger.form.plan.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import org.pennyledger.form.EntryMode;
import org.pennyledger.form.FormField;
import org.pennyledger.form.LabelFor;
import org.pennyledger.form.LastEntryFor;
import org.pennyledger.form.Mode;
import org.pennyledger.form.ModeFor;
import org.pennyledger.form.NotFormField;
import org.pennyledger.form.OccursFor;
import org.pennyledger.form.Validation;
import org.pennyledger.form.type.EnumType;
import org.pennyledger.form.type.IType;
import org.pennyledger.form.plan.IArrayPlan;
import org.pennyledger.form.plan.IGroupPlan;
import org.pennyledger.form.plan.IListPlan;
import org.pennyledger.form.plan.IObjectPlan;
import org.pennyledger.form.plan.IRuntimeDefaultProvider;
import org.pennyledger.form.plan.IRuntimeFactoryProvider;
import org.pennyledger.form.plan.IRuntimeLabelProvider;
import org.pennyledger.form.plan.IRuntimeModeProvider;
import org.pennyledger.form.plan.IRuntimeOccursProvider;
import org.pennyledger.form.plan.IRuntimeTypeProvider;
import org.pennyledger.form.plan.IValidationMethod;
import org.pennyledger.form.plan.ValidationMethod;
import org.pennyledger.util.CamelCase;
import org.pennyledger.util.UserEntryException;


public class ObjectPlanFactory {

  public static IObjectPlan buildObjectPlan (Object parentRef, Class<?> parentClass, String name, boolean withinCollection, Field field, Type type, EntryMode entryMode, ArraySizeList arraySizes, Field lastEntryField, Object staticDefaultValue) {
    IObjectPlan objPlan;
    
    if (type instanceof GenericArrayType) {
      Type type1 = ((GenericArrayType)type).getGenericComponentType();
      objPlan = arrayPlanDetail(parentRef, parentClass, name, field, type1, entryMode, arraySizes, lastEntryField);
    } else if (type instanceof ParameterizedType) {
      ParameterizedType ptype = (ParameterizedType)type;
      Type type1 = ptype.getRawType();
      if (type1.equals(List.class)) {
        Type[] typeArgs = ptype.getActualTypeArguments();
        if (typeArgs.length == 0) {
          throw new IllegalArgumentException("List must have a type parameter");
        }
        Type type2 = typeArgs[0];
        objPlan = listPlanDetail(parentRef, parentClass, name, field, type2, entryMode, arraySizes, lastEntryField);
      } else {
        throw new IllegalArgumentException("Parameterized type that is not a List");
      }
    } else if (type instanceof Class) {
      Class<?> klass = (Class<?>)type;
      if (klass.isArray()) {
        Type type1 = klass.getComponentType();
        objPlan = arrayPlanDetail(parentRef, parentClass, name, field, type1, entryMode, arraySizes, lastEntryField);
      } else {
        objPlan = fieldPlanDetail(parentRef, parentClass, name, withinCollection, field, type, entryMode, arraySizes, lastEntryField, staticDefaultValue);
      }
    } else {
      throw new IllegalArgumentException("Unsupported type: " + type);
    }
    return objPlan;
  }
  
  
  public static IGroupPlan buildGroupPlan (Object parentRef, String pathName, Class<?> groupClass) {
    GroupPlan groupPlan = groupPlanDetail(parentRef, pathName, groupClass);
    return groupPlan;
  }
  
  
  public static IGroupPlan buildGroupPlan (String pathName, Class<?> groupClass) {
    return buildGroupPlan (null, pathName, groupClass);
  }
  
  
  public static IGroupPlan buildGroupPlan (Class<?> groupClass) {
    String pathName = groupClass.getSimpleName();
    pathName = Character.toLowerCase(pathName.charAt(0)) + pathName.substring(1);
    return buildGroupPlan (null, pathName, groupClass);
  }
  
  
  public static IArrayPlan buildArrayPlan (Object parentRef, Type elemType, int size) {
    ArraySizeList arraySizes = new ArraySizeList(size);
    ArrayPlan arrayPlan = arrayPlanDetail(parentRef, null, null, null, elemType, EntryMode.UNSPECIFIED, arraySizes, null);
    return arrayPlan;
  }
  
  
  public static IArrayPlan buildArrayPlan (Type elemType, int size) {
    return buildArrayPlan (null, elemType, size);
  }
  
  
  public static IListPlan buildListPlan (Object parentRef, Type elemType) {
    ArraySizeList arraySizes = new ArraySizeList();
    ListPlan listPlan = listPlanDetail(parentRef, null, null, null, elemType, EntryMode.UNSPECIFIED, arraySizes, null);
    return listPlan;
  }
  
  
  public static IListPlan buildListPlan (Type elemType) {
    return buildListPlan (null, elemType);
  }
  
  
  private static GroupPlan groupPlanDetail (Object parentRef, String name, Class<?> groupClass) {
    EntryMode entryMode = EntryMode.UNSPECIFIED;
    Mode modeAnn = groupClass.getAnnotation(Mode.class);
    if (modeAnn != null) {
      entryMode = modeAnn.value();
    }
    
    GroupPlan groupPlan = new GroupPlan(parentRef, name, groupClass, entryMode);
    addGroupFields (groupPlan, groupClass, true);
    return groupPlan;
  }

  private static ReferencePlan referencePlanDetail (Object parentRef, String name, Class<?> targetEntityClass, boolean optional, EntryMode entryMode) {
    ReferencePlan referencePlan = new ReferencePlan(parentRef, name, targetEntityClass, optional, entryMode);
    addReferenceFields (referencePlan, targetEntityClass, true);
    return referencePlan;
  }

  
  private static ArrayPlan arrayPlanDetail (Object parentRef, Class<?> parentClass, String name, Field field, Type elemType, EntryMode entryMode, ArraySizeList arraySizes, Field lastEntryField) {
    int size = arraySizes.nextSize();
    ArrayPlan arrayPlan = new ArrayPlan(parentRef, name, (Class<?>)elemType, size, size);
    IObjectPlan elemPlan = buildObjectPlan(null, parentClass, "elem", true, field, elemType, entryMode, arraySizes, lastEntryField, null);
    arrayPlan.setElementPlan(elemPlan);
    return arrayPlan;
  }
  
  
  private static ListPlan listPlanDetail (Object parentRef, Class<?> parentClass, String name, Field field, Type elemType, EntryMode entryMode, ArraySizeList arraySizes, Field lastEntryField) {
    ListPlan listPlan = new ListPlan(parentRef, name);
    IObjectPlan elemPlan = buildObjectPlan(null, parentClass, "elem", true, field, elemType, entryMode, arraySizes, lastEntryField, null);
    listPlan.setElementPlan(elemPlan);
    return listPlan;
  }
  
  
  private static Field[] defaultKeyFields(Class<?> entityClass) {
    Field[] fields = entityClass.getDeclaredFields();

    for (Field field : fields) {
      int modifiers = field.getModifiers();
      if ((modifiers & (Modifier.STATIC | Modifier.TRANSIENT)) != 0) {
        // Static and transient fields are not key columns
        continue;
      }
      Id idann = field.getAnnotation(Id.class);
      if (idann != null) {
        // Version fields are not key columns
        continue;
      }
      Version vann = field.getAnnotation(Version.class);
      if (vann != null) {
        // Version fields are not key columns
        continue;
      }
      GeneratedValue gvann = field.getAnnotation(GeneratedValue.class);
      if (gvann != null) {
        // Generated fields are not key columns
        continue;
      }
      Transient tann = field.getAnnotation(Transient.class);
      if (tann != null) {
        // Transient marked fields are not key columns
        continue;
      }
      // The first non-special field is, by default, the key column
      return new Field[] {
        field,
      };
    }
    throw new RuntimeException("No default key field found");
  }

  
  private static Field[] getKeyFields (Class<?> entityClass) {
    Field[] keyFields;
    
    // Get the first unique constraint, and use it as the form key fields
    Table tableAnn = entityClass.getAnnotation(Table.class);
    if (tableAnn == null) {
      // There is not table annotation, so use the first non-special field
      keyFields = defaultKeyFields(entityClass);
    } else {
      UniqueConstraint[] uc = tableAnn.uniqueConstraints();
      if (uc == null || uc.length == 0) {
        // Again, there are no unique constraints, so use the first non-special field
        keyFields = defaultKeyFields(entityClass);
      } else {
        try {
          // TODO The following is wrong.  The following code compares column names with
          // field names.  This only works where, for key fields/columns, the column 
          // name and field names are the same, such as "name" and "NAME".  If will not
          // work when the field name is "firstName" and the column name is "FIRST_NAME".
          // Case of the UniqueConstraint should match the field names, not the database
          // columns.
          String[] keyColumnNames = uc[0].columnNames();
          keyFields = new Field[keyColumnNames.length];
          for (int i = 0; i < keyFields.length; i++) {
            keyFields[i] = entityClass.getField(keyColumnNames[i]);
          }
        } catch (SecurityException ex) {
          throw new RuntimeException(ex);
        } catch (NoSuchFieldException ex) {
          throw new RuntimeException(ex);
        }
      }
    }
    return keyFields;
  }

  
//  private static EntryMode adjustEntryMode (Field field, EntryMode parentMode) {
//    Mode modeAnn = field.getAnnotation(Mode.class);
//    if (modeAnn != null) {
//      EntryMode fieldMode = modeAnn.value();
//      if (fieldMode.ordinal() > parentMode.ordinal()) {
//        return fieldMode;
//      } else {
//        return parentMode;
//      }
//    } else {
//      return parentMode;
//    }
//  }


  /**
   * Returns a annotation attached to a field.  This method is a replacement for the
   * getAnnotation(String name) method on Class.  This method is used because it works
   * for proxied annotations whereas the getAnnotation method on Class does not.
   */
  private static boolean isAnnotated (Field field, Class<?> annClass) {
    String targetName = annClass.getName();
    
    Annotation[] anns = field.getAnnotations();
    for (Annotation an : anns) {
      if (an.annotationType().getName().equals(targetName)) {
        return true;
      }
    }
    return false;
  }

  
  /**
   * Returns a annotation attached to a class.  This method is a replacement for the
   * getAnnotation(String name) method on Class.  This method is used because it works
   * for proxied annotations whereas the getAnnotation method on Class does not.
   */
  private static boolean isAnnotated (Class<?> klass, Class<?> annClass) {
    String targetName = annClass.getName();
    
    Annotation[] anns = klass.getAnnotations();
    for (Annotation an : anns) {
      if (an.annotationType().getName().equals(targetName)) {
        return true;
      }
    }
    return false;
  }

  
  @SuppressWarnings({ "unchecked", "rawtypes" })
  private static IObjectPlan fieldPlanDetail (Object parentRef, Class<?> parentClass, String name, boolean withinCollection, Field field, Type fieldType, EntryMode entryMode, ArraySizeList arraySizes, Field lastEntryField, Object staticDefaultValue) {
    IObjectPlan objectPlan;
    
    // Calculate a new entryMode
    //entryMode = adjustEntryMode(field, entryMode);
    
    // Is there a type declaration within the class
    Class<?> fieldClass = (Class<?>)fieldType;
    FormField formFieldAnn = field.getAnnotation(FormField.class);
    IType<?> type = AtomicTypeList.lookupType(parentClass, name, formFieldAnn);
    if (type != null) {
      objectPlan = new FieldPlan(field, field, type, entryMode, lastEntryField, staticDefaultValue);
    } else {
      // Otherwise, is the type of the field a known type (primitive or String, Date, Decimal, etc)
      AtomicTypeList.TypeMap typeMap = AtomicTypeList.getTypeMap(fieldClass);
      if (typeMap != null) {
        javax.persistence.Column annColumn = field.getAnnotation(javax.persistence.Column.class);
        type = AtomicTypeList.getDefaultType(formFieldAnn, annColumn, typeMap);
        objectPlan = new FieldPlan(field, field, type, entryMode, lastEntryField, staticDefaultValue);
      } else {
        if (Enum.class.isAssignableFrom(fieldClass)) {
          type = new EnumType(fieldClass);
          objectPlan = new FieldPlan(field, field, type, entryMode, lastEntryField, staticDefaultValue);
        } else {
          boolean embdann = isAnnotated(field, Embedded.class);
          if (embdann) {
            objectPlan = groupPlanDetail(parentRef, field.getName(), fieldClass);
          } else {
            boolean emblann = isAnnotated(fieldClass, Embeddable.class);
            if (emblann) {
              objectPlan = groupPlanDetail(parentRef, field.getName(), fieldClass);
            } else {
              ManyToOne fkAnn = field.getAnnotation(ManyToOne.class);
              if (fkAnn != null) {
                Class<?> targetEntity = fkAnn.targetEntity();
                if (targetEntity.equals(void.class)) {
                  targetEntity = fieldClass;
                }
                objectPlan = referencePlanDetail(parentRef, field.getName(), targetEntity, fkAnn.optional(), entryMode);
              } else {
                OneToOne fkAnn1 = field.getAnnotation(OneToOne.class);
                if (fkAnn1 != null) {
                  Class<?> targetEntity = fkAnn1.targetEntity();
                  if (targetEntity.equals(void.class)) {
                    targetEntity = fieldClass;
                  }
                  objectPlan = referencePlanDetail(parentRef, field.getName(), targetEntity, fkAnn1.optional(), entryMode);
                } else {
                  if (withinCollection) {
                    objectPlan = groupPlanDetail(parentRef, field.getName(), fieldClass);
                  } else {
                    throw new RuntimeException("Field type not recognised: " + name + " " + field.getType());
                  }
                }
              }
            }
          }
        }
      }
    }
    return objectPlan;
  }
  
  /************************************************************************************/
  
//  public static IObjectPlan buildObjectPlan (Object parentRef, String pathName, Class<?> klass, EntryMode entryMode) {
//    if (klass.isArray()) {
//      ArrayPlan arrayPlan = buildMemberArray(parentRef, "elem", null, klass, entryMode, null);
//      return arrayPlan;
//    } else if (klass.isAssignableFrom(List.class)) {
//      ListPlan listPlan = buildMemberList(parentRef, "elem", null, klass, entryMode, null);
//      return listPlan;
//    } else {
//      GroupPlan groupPlan = new GroupPlan(parentRef, pathName, klass, entryMode);
//      addGroupFields(parentRef, groupPlan, klass, true, entryMode);
//      return groupPlan;
//    }
//  }
//  
//  
//  public static IObjectPlan buildListPlan (Object parentRef, String pathName, Class<?> klass, EntryMode entryMode) {
//    ListPlan listPlan = buildMemberList(parentRef, "elem", null, klass, entryMode, null);
//    return listPlan;
//  }
//
//  public static IGroupPlan buildMemberGroup (Object parentRef, String pathName, Class<?> klass, EntryMode entryMode) {
//    GroupPlan groupPlan = new GroupPlan(parentRef, pathName, klass, entryMode);
//    addGroupFields(parentRef, groupPlan, klass, true, entryMode);
//    return groupPlan;
//  }
//  
//  
//  private static String getPathName (Class<?> klass) {
//    String name = klass.getSimpleName();
//    return Character.toLowerCase(name.charAt(0)) + name.substring(1);
//  }
//  
//  
//  public static IGroupPlan buildMemberGroup (Object parentRef, Class<?> klass, EntryMode entryMode) {
//    return buildMemberGroup(parentRef, getPathName(klass), klass, entryMode);
//  }
//  
//  
//  public static IGroupPlan buildMemberGroup (Class<?> klass, EntryMode entryMode) {
//    return buildMemberGroup(null, getPathName(klass), klass, entryMode);
//  }
//  
//  
//  public static IGroupPlan buildMemberGroup (Class<?> klass) {
//    return buildMemberGroup(null, getPathName(klass), klass, EntryMode.UNSPECIFIED);
//  }
//  
//  
//  @SuppressWarnings({ "unchecked", "rawtypes" })
//  private static ObjectPlan buildMemberObject(Object parentRef, GroupPlan groupPlan, String pathName, Field field, Class<?> fieldClass, Field lastEntryField) {
//    ObjectPlan objectPlan;
//    FormField formFieldAnn = field.getAnnotation(FormField.class);
//    EntryMode initialEntryMode = formFieldAnn == null ? EntryMode.UNSPECIFIED : formFieldAnn.mode();
//
//    // Is this an array
//    if (fieldClass.isArray()) {
//      objectPlan = buildMemberArray(parentRef, field.getName(), field, fieldClass, initialEntryMode, lastEntryField);
//      groupPlan.put(field.getName(), objectPlan);
//    } else if (fieldClass.isAssignableFrom(List.class)) {
//      objectPlan = buildMemberList(parentRef, field.getName(), field, fieldClass, initialEntryMode, lastEntryField);
//      groupPlan.put(field.getName(), objectPlan);
//    } else {
//      // Is there a type declaration within the class
//      IType type = AtomicTypeList.lookupType(field.getDeclaringClass(), field.getName(), formFieldAnn);
//      if (type != null) {
//        objectPlan = new FieldPlan(field, field, type, lastEntryField);
//        groupPlan.put(field.getName(), objectPlan);
//      } else {
//        // Otherwise, is the type of the field a known type (primitive or String, Date, Decimal, etc)
//        AtomicTypeList.TypeMap typeMap = AtomicTypeList.getTypeMap(fieldClass);
//        if (typeMap != null) {
//          javax.persistence.Column annColumn = field.getAnnotation(javax.persistence.Column.class);
//          type = AtomicTypeList.getDefaultType(formFieldAnn, annColumn, typeMap);
//          objectPlan = new FieldPlan(field, field, type, lastEntryField);
//          groupPlan.put(field.getName(), objectPlan);
//        } else {
//          if (Enum.class.isAssignableFrom(fieldClass)) {
//            type = new EnumType(fieldClass);
//            objectPlan = new FieldPlan(field, field, type, lastEntryField);
//            groupPlan.put(field.getName(), objectPlan);
//          } else {
//            Embedded embdann = field.getAnnotation(Embedded.class);
//            if (embdann != null) {
//              objectPlan = new GroupPlan(field, field.getName(), fieldClass, initialEntryMode);
//              addGroupFields(parentRef, groupPlan, fieldClass, true, initialEntryMode);
//            } else {
//              Embeddable emblann = fieldClass.getAnnotation(Embeddable.class);
//              if (emblann != null) {
//                objectPlan = new GroupPlan(field, field.getName(), fieldClass, initialEntryMode);
//                addGroupFields(parentRef, groupPlan, fieldClass, true, initialEntryMode);
//              } else {
//                ManyToOne fkAnn = field.getAnnotation(ManyToOne.class);
//                if (fkAnn != null) {
//                  objectPlan = new GroupPlan(field, field.getName(), fieldClass, initialEntryMode);
//                  addGroupFields(parentRef, (GroupPlan)objectPlan, fieldClass, true, initialEntryMode);
//                  groupPlan.put(field.getName(), objectPlan);
//                } else {
//                  // Entity enann = fieldClass.getAnnotation(Entity.class);
//                  // if (enann != null) {
//                  // fieldPlan = new AnnotatedForeignKeyPlan(field, fieldClass);
//                  // } else {
//                  throw new RuntimeException("Field not recognised: " + field.getName() + " " + field.getType());
//                  // }
//                }
//              }
//            }
//          }
//        }
//      }
//    }
//    return objectPlan;
//  }
//  
//  
//  /**
//   * Build a member object that is known to be an array.  The 'fieldClass' argument identifies an array.
//   */
//  private static ArrayPlan buildMemberArray (Object parentRef, String pathName, Field field, Class<?> fieldClass, EntryMode entryMode, Field lastEntryField) {
//    int minSize = 0;
//    int maxSize = Integer.MAX_VALUE;
//    Occurs occursAnn = field.getAnnotation(Occurs.class);
//    if (occursAnn != null) {
//      int value = occursAnn.value();
//      if (value == -1) {
//        minSize = occursAnn.min();
//        maxSize = occursAnn.max();
//      } else {
//        minSize = value;
//        maxSize = value;
//      }
//    }
//    
//    Class<?> elemClass = fieldClass.getComponentType();
//    ObjectPlan elemPlan;
//    if (elemClass.isArray()) {
//      elemPlan = buildMemberArray(parentRef, "elem", field, elemClass, entryMode, lastEntryField);
//    } else {
//      // Is this an atomic type (primitive or String, Date, Decimal, etc)
//      AtomicTypeList.TypeMap typeMap = AtomicTypeList.getTypeMap(elemClass);
//      if (typeMap != null) {
//        javax.persistence.Column annColumn = field.getAnnotation(javax.persistence.Column.class);
//        FormField formFieldAnn = field.getAnnotation(FormField.class);
//        IType<?> type = AtomicTypeList.getDefaultType(formFieldAnn, annColumn, typeMap);
//        elemPlan = new FieldPlan(field, field, type, lastEntryField);
//      } else {
//        elemPlan = new GroupPlan(parentRef, pathName, elemClass, entryMode);
//        addGroupFields(parentRef, (GroupPlan)elemPlan, elemClass, true, entryMode);
//      }
//    }
//    ArrayPlan arrayPlan = new ArrayPlan(pathName, elemClass, elemPlan, minSize, maxSize);
//    return arrayPlan;
//  }
//
//  
//  /**
//   * Build a member object that is known to be a list.  The 'fieldClass' argument identifies a List.
//   */
//  private static ListPlan buildMemberList (Object parentRef, String pathName, Field field, Type fieldClass, EntryMode entryMode, Field lastEntryField) {
//    ParameterizedType genericType = (ParameterizedType)field.getGenericType();
//    Type[] typeArgs = genericType.getActualTypeArguments();
//    if (typeArgs.length == 0) {
//      throw new RuntimeException("Type parameter required on List");
//    } else if (typeArgs.length > 1) {
//      throw new RuntimeException("Illegal number of type parameters on List"); 
//    }
//    Type elemClass = typeArgs[0];
//    
//    return buildMemberList (parentRef, pathName, elemClass, entryMode, lastEntryField);
//  }  
//    
//    
//  private static ListPlan buildMemberList (Object parentRef, String pathName, Type elemClass, EntryMode entryMode, Field lastEntryField) {
//    ObjectPlan elemPlan;
//    if (elemClass instanceof GenericArrayType) {
//      elemPlan = buildMemberArray(parentRef, "elem", field, elemClass, entryMode, lastEntryField);
//    } else if (elemClass.isAssignableFrom(List.class)) {
//      elemPlan = buildMemberList(parentRef, "elem", field, elemClass, entryMode, lastEntryField);
//    } else {
//      // Is this an atomic type (primitive or String, Date, Decimal, etc)
//      AtomicTypeList.TypeMap typeMap = AtomicTypeList.getTypeMap(elemClass);
//      if (typeMap != null) {
//        javax.persistence.Column annColumn = field.getAnnotation(javax.persistence.Column.class);
//        FormField formFieldAnn = field.getAnnotation(FormField.class);
//        IType<?> type = AtomicTypeList.getDefaultType(formFieldAnn, annColumn, typeMap);
//        elemPlan = new FieldPlan(field, field, type, lastEntryField);
//      } else {
//        elemPlan = new GroupPlan(parentRef, pathName, elemClass, entryMode);
//        addGroupFields(parentRef, (GroupPlan)elemPlan, elemClass, true, entryMode);
//      }
//    }
//    ListPlan listPlan = new ListPlan(pathName, elemPlan);
//    return listPlan;
//  }

  
  private static void addGroupFields (GroupPlan memberGroup, Class<?> klass, boolean include) {
    Field[] declaredFields = klass.getDeclaredFields();
    addGroupFields2 (memberGroup, klass, declaredFields, include);
  }
  
  
  private static void addReferenceFields (GroupPlan memberGroup, Class<?> klass, boolean include) {
    Field[] keyFields = ObjectPlanFactory.getKeyFields(klass);
    addGroupFields2 (memberGroup, klass, keyFields, include);
  }
  
  
  private static void addGroupFields2 (GroupPlan memberGroup, Class<?> klass, Field[] fields, boolean include) {
    FieldDependency fieldDependency = new FieldDependency();
    fieldDependency.parseClass(klass.getName());

    // Parse the class hierarchy recursively
    Class<?> superKlass = klass.getSuperclass();
    if (superKlass != null && !superKlass.equals(Object.class)) {
      MappedSuperclass msc = superKlass.getAnnotation(MappedSuperclass.class);
      addGroupFields(memberGroup, superKlass, msc != null);
    }
    
    if (include) {
      Map<String, Field> lastEntryFields = new HashMap<String, Field>();

      // Parse the declared fields of this class, first for 'last entry' fields
      for (Field field : fields) {
        if (field.isSynthetic()) {
          // Synthetic fields cannot be form fields
          continue;
        }
        int m = field.getModifiers();
        if ((m & (Modifier.STATIC | Modifier.FINAL | Modifier.VOLATILE)) != 0) {
          // Exclude static, final and volatile fields (but transient fields are not excluded)
          continue;
        }

        // Remember the last entry fields
        LastEntryFor lastEntryForAnn = field.getAnnotation(LastEntryFor.class);
        if (lastEntryForAnn != null) {
          for (String name : lastEntryForAnn.value()) {
            lastEntryFields.put(name, field);
          }
          continue;
        }
        
        // add last entry fields named by convention
        String name = field.getName();
        if (name.endsWith("LastEntry")) {
          String n = name.substring(0, name.length() - 9);
          lastEntryFields.put(n, field);
          continue;
        }
      }

      Object groupInstance = null;
      
      // And again for the fields themselves
      for (Field field : fields) {
        if (field.isSynthetic()) {
          // Synthetic fields cannot be form fields
          continue;
        }
        int m = field.getModifiers();
        if ((m & (Modifier.STATIC | Modifier.FINAL | Modifier.VOLATILE)) != 0) {
          // Exclude static, final and volatile fields (but transient fields are not excluded)
          continue;
        }

        NotFormField notFieldAnn = field.getAnnotation(NotFormField.class);
        if (notFieldAnn != null) {
          continue;
        }
        
        // Last entry fields are not form input fields
        LastEntryFor lastEntryForAnn = field.getAnnotation(LastEntryFor.class);
        if (lastEntryForAnn != null && lastEntryForAnn.value().length > 0) {
          continue;
        } else {
          if (field.getName().endsWith("LastEntry")) {
            continue;
          }
          // unless they are annotated with an empty LastEntryFor
        }
        
        // Generated values are not form input fields
        GeneratedValue gvann = field.getAnnotation(GeneratedValue.class);
        if (gvann != null) {
          continue;
        }
        
        // Version values are not form input fields
        Version vann = field.getAnnotation(Version.class);
        if (vann != null) {
          continue;
        }
        
        // From here on in, all the fields are input fields.  It just depends on the
        // type as to whether the fields are embedded or not.
        EntryMode entryMode = EntryMode.UNSPECIFIED;
        Mode modeAnn = field.getAnnotation(Mode.class);
        if (modeAnn != null) {
          entryMode = modeAnn.value();
        }
        
        Field lastEntryField = lastEntryFields.get(field.getName());
        Type fieldType = field.getGenericType();
        ArraySizeList arraySizes = new ArraySizeList(field);

        if (groupInstance == null) {
          // Create an instance to user as the source of static default values
          groupInstance = newInstance(klass);
        }
        Object staticDefaultValue = getFieldValue(groupInstance, field);
        
        IObjectPlan fieldPlan = buildObjectPlan(field, klass, field.getName(), false, field, fieldType, entryMode, arraySizes, lastEntryField, staticDefaultValue);
        memberGroup.put(field.getName(), fieldPlan);
        
        setGroupAttributesDirectly(field, fieldPlan, memberGroup, entryMode, fieldDependency);
      }
    }
    
    findTypeForAnnotations (memberGroup, klass, fields);
    findLabelForAnnotations (memberGroup, klass, fields);
    findModeForAnnotations (memberGroup, klass, fields, fieldDependency);
    findDefaultForAnnotations (memberGroup, klass, fields, fieldDependency);
    findOccursForAnnotations (memberGroup, klass, fields, fieldDependency);
    findVariantForAnnotations (memberGroup, klass, fieldDependency);
    findValidationMethods (memberGroup, klass, fieldDependency);
  }
  
  
  private static Object newInstance (Class<?> klass) {
    Object instance = null;
    try {
      Constructor<?> constructor = klass.getDeclaredConstructor();
      constructor.setAccessible(true);
      instance = constructor.newInstance();
    } catch (InstantiationException ex) {
      throw new RuntimeException(ex);
    } catch (IllegalAccessException ex) {
      throw new RuntimeException(ex);
    } catch (SecurityException ex) {
      throw new RuntimeException(ex);
    } catch (NoSuchMethodException ex) {
      throw new RuntimeException(ex);
    } catch (IllegalArgumentException ex) {
      throw new RuntimeException(ex);
    } catch (InvocationTargetException ex) {
      throw new RuntimeException(ex);
    }
    return instance;
  }
  
  
  private static Object getFieldValue (Object instance, Field field) {
    Object value = null;
    try {
      field.setAccessible(true);
      value = field.get(instance);
    } catch (IllegalArgumentException ex) {
      throw new RuntimeException(ex);
    } catch (IllegalAccessException ex) {
      throw new RuntimeException(ex);
    }
    return value;
  }
  
  
  private static boolean throwsException (Method method) {
    Class<?>[] exceptions = method.getExceptionTypes();
    for (Class<?> ex : exceptions) {
      if (Exception.class.isAssignableFrom(ex)) {
        return true;
      }
    }
    return false;
  }

  
  private static boolean throwsUserEntryException (Method method) {
    Class<?>[] exceptions = method.getExceptionTypes();
    for (Class<?> ex : exceptions) {
      if (UserEntryException.class.isAssignableFrom(ex)) {
        return true;
      }
    }
    return false;
  }

  
  private static void findTypeForAnnotations (GroupPlan memberGroup, Class<?> klass, Field[] fields) {
    // Look for fields annotated with TypeFor. 
    for (Field field : fields) {
      org.pennyledger.form.TypeFor typeFor = field.getAnnotation(org.pennyledger.form.TypeFor.class);
      if (typeFor != null) {
        // This field has been explicitly annotated as the type for some field or fields.
        String[] xpaths = typeFor.value();
        try {
          int modifier = field.getModifiers();
          if (Modifier.isStatic(modifier)) {
            field.setAccessible(true);
            IType<?> type = (IType<?>)field.get(null);
            IRuntimeTypeProvider typeProvider = new RuntimeTypeProvider(type, xpaths);
            memberGroup.addRuntimeTypeProvider(typeProvider);
          }
        } catch (IllegalArgumentException e) {
          throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
          throw new RuntimeException(e);
        }
      }
    }

    // Look for methods annotated with TypeFor. 
    for (Method method : klass.getDeclaredMethods()) {
      org.pennyledger.form.TypeFor typeFor = method.getAnnotation(org.pennyledger.form.TypeFor.class);
      if (typeFor != null) {
        // This method has been explicitly annotated as the type for some field or fields.
        String[] xpaths = typeFor.value();
        try {
          int modifier = method.getModifiers();
          if (Modifier.isStatic(modifier)) {
            method.setAccessible(true);
            IType<?> type = (IType<?>)method.invoke(null);
            IRuntimeTypeProvider typeProvider = new RuntimeTypeProvider(type, xpaths);
            memberGroup.addRuntimeTypeProvider(typeProvider);
          }
        } catch (InvocationTargetException e) {
          throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
          throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
          throw new RuntimeException(e);
        }
      }
    }
  }
  
  
  private static void findModeForAnnotations (GroupPlan memberGroup, Class<?> klass, Field[] fields, FieldDependency fieldDependency) {
    // Look for fields annotated with ModeFor. 
    for (Field field : fields) {
      org.pennyledger.form.ModeFor modeFor = field.getAnnotation(org.pennyledger.form.ModeFor.class);
      if (modeFor != null) {
        // This field has been explicitly annotated as an mode for some field or fields.
        String[] xpaths = modeFor.value();
        try {
          int modifier = field.getModifiers();
          if (Modifier.isStatic(modifier)) {
            field.setAccessible(true);
            EntryMode mode = (EntryMode)field.get(null);
            IRuntimeModeProvider modeProvider = new RuntimeModeProvider(mode, xpaths);
            memberGroup.addRuntimeModeProvider(modeProvider);
          }
        } catch (IllegalArgumentException e) {
          throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
          throw new RuntimeException(e);
        }
      }
    }
    
    // Look for methods annotated with ModeFor. 
    for (Method method : klass.getDeclaredMethods()) {
      org.pennyledger.form.ModeFor modeFor = method.getAnnotation(org.pennyledger.form.ModeFor.class);
      if (modeFor != null) {
        // This method has been explicitly annotated as the use for some field or fields.
        String[] xpaths = modeFor.value();
        try {
          int modifier = method.getModifiers();
          if (Modifier.isStatic(modifier)) {
            method.setAccessible(true);
            EntryMode mode = (EntryMode)method.invoke(null);
            IRuntimeModeProvider modeProvider = new RuntimeModeProvider(mode, xpaths);
            memberGroup.addRuntimeModeProvider(modeProvider);
          } else {
            IRuntimeModeProvider modeProvider = new RuntimeModeProvider(klass, fieldDependency, method, xpaths);
            memberGroup.addRuntimeModeProvider(modeProvider);
          }
        } catch (InvocationTargetException e) {
          throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
          throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
          throw new RuntimeException(e);
        }
      }
    }
  }
  
  
  private static void findOccursForAnnotations (GroupPlan memberGroup, Class<?> klass, Field[] fields, FieldDependency fieldDependency) {
    // Look for fields annotated with OccursFor. 
    for (Field field : fields) {
      OccursFor occursFor = field.getAnnotation(OccursFor.class);
      if (occursFor != null) {
        // This field has been explicitly annotated as the size of an array field or fields.
        String[] xpaths = occursFor.value();
        try {
          int modifier = field.getModifiers();
          if (Modifier.isStatic(modifier)) {
            field.setAccessible(true);
            int size = (Integer)field.get(null);
            IRuntimeOccursProvider occursProvider = new RuntimeOccursProvider(size, xpaths);
            memberGroup.addRuntimeOccursProvider(occursProvider);
          } else {
            IRuntimeOccursProvider occursProvider = new RuntimeOccursProvider(field, xpaths);
            memberGroup.addRuntimeOccursProvider(occursProvider);
          }
        } catch (IllegalArgumentException e) {
          throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
          throw new RuntimeException(e);
        }
      }
    }
    
    // Look for methods annotated with OccursFor. 
    for (Method method : klass.getDeclaredMethods()) {
      OccursFor occursFor = method.getAnnotation(OccursFor.class);
      if (occursFor != null) {
        // This method has been explicitly annotated as the size of an array field or fields.
        String[] xpaths = occursFor.value();
        try {
          int modifier = method.getModifiers();
          if (Modifier.isStatic(modifier)) {
            method.setAccessible(true);
            int size = (Integer)method.invoke(null);
            IRuntimeOccursProvider occursProvider = new RuntimeOccursProvider(size, xpaths);
            memberGroup.addRuntimeOccursProvider(occursProvider);
          } else {
            IRuntimeOccursProvider occursProvider = new RuntimeOccursProvider(klass, fieldDependency, method, xpaths);
            memberGroup.addRuntimeOccursProvider(occursProvider);
          }
        } catch (InvocationTargetException e) {
          throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
          throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
          throw new RuntimeException(e);
        }
      }
    }
  }
  
  
  private static void findLabelForAnnotations (GroupPlan memberGroup, Class<?> klass, Field[] fields) {
    // Look for fields annotated with LabelFor. 
    for (Field field : fields) {
      org.pennyledger.form.LabelFor labelFor = field.getAnnotation(org.pennyledger.form.LabelFor.class);
      if (labelFor != null) {
        // This field has been explicitly annotated as a label for some field or fields.
        String[] xpaths = labelFor.value();
        try {
          int modifier = field.getModifiers();
          if (Modifier.isStatic(modifier)) {
            field.setAccessible(true);
            String label = (String)field.get(null);
            IRuntimeLabelProvider labelProvider = new RuntimeLabelProvider(label, xpaths);
            memberGroup.addRuntimeLabelProvider(labelProvider);
          }
        } catch (IllegalArgumentException e) {
          throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
          throw new RuntimeException(e);
        }
      }
    }
    
    // Look for methods annotated with LabelFor. 
    for (Method method : klass.getDeclaredMethods()) {
      org.pennyledger.form.LabelFor labelFor = method.getAnnotation(org.pennyledger.form.LabelFor.class);
      if (labelFor != null) {
        // This method has been explicitly annotated as the label for some field or fields.
        String[] xpaths = labelFor.value();
        try {
          int modifier = method.getModifiers();
          if (Modifier.isStatic(modifier)) {
            method.setAccessible(true);
            String label = (String)method.invoke(null);
            IRuntimeLabelProvider labelProvider = new RuntimeLabelProvider(label, xpaths);
            memberGroup.addRuntimeLabelProvider(labelProvider);
          }
        } catch (InvocationTargetException e) {
          throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
          throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
          throw new RuntimeException(e);
        }
      }
    }
    
  }
  
  
  private static void findDefaultForAnnotations (GroupPlan memberGroup, Class<?> klass, Field[] fields, FieldDependency fieldDependency) {
    // Look for fields annotated with DefaultFor. 
    for (Field field : fields) {
      org.pennyledger.form.DefaultFor defaultFor = field.getAnnotation(org.pennyledger.form.DefaultFor.class);
      if (defaultFor != null) {
        // This field has been explicitly annotated as the default for some field or fields.
        String[] xpaths = defaultFor.value();
        try {
          int modifier = field.getModifiers();
          if (Modifier.isStatic(modifier)) {
            field.setAccessible(true);
            Object defaultValue = field.get(null);
            IRuntimeDefaultProvider defaultProvider = new RuntimeDefaultProvider(defaultValue, xpaths);
            memberGroup.addRuntimeDefaultProvider(defaultProvider);
          }
        } catch (IllegalArgumentException e) {
          throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
          throw new RuntimeException(e);
        }
      }
    }

    // Look for methods annotated with DefaultFor. 
    for (Method method : klass.getDeclaredMethods()) {
      org.pennyledger.form.DefaultFor defaultFor = method.getAnnotation(org.pennyledger.form.DefaultFor.class);
      if (defaultFor != null) {
        // This method has been explicitly annotated as the default for some field or fields.
        String[] xpaths = defaultFor.value();
        try {
          int modifier = method.getModifiers();
          if (Modifier.isStatic(modifier)) {
            method.setAccessible(true);
            Object defaultValue = method.invoke(null);
            IRuntimeDefaultProvider defaultProvider = new RuntimeDefaultProvider(defaultValue, xpaths);
            memberGroup.addRuntimeDefaultProvider(defaultProvider);
          } else {
            String[] fieldNames = defaultFor.value();
            IRuntimeDefaultProvider defaultProvider = new RuntimeDefaultProvider(klass, fieldDependency, method, false, fieldNames);
            memberGroup.addRuntimeDefaultProvider(defaultProvider);
          }
        } catch (InvocationTargetException e) {
          throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
          throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
          throw new RuntimeException(e);
        }
      }
    }
  }
  
  
  private static void findVariantForAnnotations (GroupPlan memberGroup, Class<?> klass, FieldDependency fieldDependency) {
    // Look for methods annotated with VariationFor. 
    for (Method method : klass.getDeclaredMethods()) {
      org.pennyledger.form.FactoryFor variantFor = method.getAnnotation(org.pennyledger.form.FactoryFor.class);
      if (variantFor != null) {
        // This method has been explicitly annotated as the variant for some field or fields.
        String[] xpaths = variantFor.value();
        try {
          int modifier = method.getModifiers();
          if (Modifier.isStatic(modifier)) {
            method.setAccessible(true);
            Object variantValue = method.invoke(null);
            IRuntimeFactoryProvider variantProvider = new RuntimeFactoryProvider(variantValue, xpaths);
            memberGroup.addRuntimeVariantProvider(variantProvider);
          } else {
            String[] fieldNames = variantFor.value();
            IRuntimeFactoryProvider variantProvider = new RuntimeFactoryProvider(klass, fieldDependency, method, fieldNames);
            memberGroup.addRuntimeVariantProvider(variantProvider);
          }
        } catch (InvocationTargetException e) {
          throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
          throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
          throw new RuntimeException(e);
        }
      }
    }
  }
  
  
  private static void findValidationMethods (GroupPlan memberGroup, Class<?> klass, FieldDependency fieldDependency) {
    for (Method method : klass.getDeclaredMethods()) {
      // Consider only methods with zero parameters, a void return type and non-static.
      if (method.getParameterTypes().length == 0 &&
          method.getReturnType().equals(Void.TYPE) &&
          !Modifier.isStatic(method.getModifiers())) {
        Validation validation = method.getAnnotation(Validation.class);
        if (validation != null) {
          boolean isSlow = validation.slow();
          if (throwsException(method)) {
            IValidationMethod validationMethod = new ValidationMethod(klass, fieldDependency, method, isSlow);
            memberGroup.addValidationMethod(validationMethod);
          } else {
            throw new RuntimeException("Method with Validation annotation, but does not throw Exception");
          }
        } else {
          if (throwsUserEntryException(method)) {
            IValidationMethod validationMethod = new ValidationMethod(klass, fieldDependency, method, false);
            memberGroup.addValidationMethod(validationMethod);
          }
        }
      }
    }
  }


  private static void setGroupAttributesDirectly (Field field, IObjectPlan memberObject, GroupPlan memberGroup, EntryMode entryMode, FieldDependency fieldDependency) {
    setLabelDirectly (field, memberObject, fieldDependency);
    setModeDirectly (field, memberObject, memberGroup, entryMode, fieldDependency);
//    if (memberObject instanceof ArrayPlan) {
//      setSizeDirectly (field, (ArrayPlan)memberObject, memberGroup, fieldDependency);
//    }
  }
  
  
  private static void setLabelDirectly (Field field, IObjectPlan memberObject, FieldDependency fieldDependency) {
    FormField formFieldAnn = field.getAnnotation(FormField.class);
    if (formFieldAnn != null) {
      // Is there a "label" attribute on the FormField annotation?
      String lx = formFieldAnn.label();
      if (!lx.equals("\u0000")) {
        memberObject.setStaticLabel(lx);
        return;
      }
    }
    Class<?> klass = field.getDeclaringClass();
    String memberName = field.getName() + "Label";
    try {
      // Try for a method, named by default, that provides a label.
      Method m = klass.getDeclaredMethod(memberName);
      // The field must be a String
      if (String.class.isAssignableFrom(m.getReturnType()) && m.getParameterTypes().length == 0) {
        // The method must not be annotated with an LabelFor annotation. If it is so annotated,
        // it is not a label providing method by convention.
        if (m.getAnnotation(LabelFor.class) == null) {
          int modifiers = m.getModifiers();
          if (Modifier.isStatic(modifiers)) {
            m.setAccessible(true);
            String lx = (String)m.invoke(null);
            memberObject.setStaticLabel(lx);
          }
          return;
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
      Field f = klass.getDeclaredField(memberName);
      // The field must be an String field
      if (String.class.isAssignableFrom(f.getType())) {
        // The field must not be annotated with an LabelFor annotation. If it is so annotated,
        // it is not a label providing field by convention.
        if (f.getAnnotation(LabelFor.class) == null) {
          int modifiers = f.getModifiers();
          if (Modifier.isStatic(modifiers)) {
            f.setAccessible(true);
            String lx = (String)f.get(null);
            memberObject.setStaticLabel(lx);
          }
          return;
        }
      }
    } catch (NoSuchFieldException ex) {
      // Continue.
    } catch (IllegalAccessException ex) {
      throw new RuntimeException(ex);
    } catch (IllegalArgumentException ex) {
      throw new RuntimeException(ex);
    }
    
    // If none of the above
    String lx = CamelCase.toSentence(field.getName());
    memberObject.setStaticLabel(lx);
  }
  
  
  private static void setModeDirectly (Field field, IObjectPlan memberObject, GroupPlan memberGroup, EntryMode entryMode, FieldDependency fieldDependency) {
    // Is there a "mode" attribute on the FormField annotation?
    if (entryMode != EntryMode.UNSPECIFIED) {
      memberObject.setStaticMode(entryMode);
    }
 
    Class<?> klass = field.getDeclaringClass();
    String memberName = field.getName() + "Mode";
    try {
      // Try for a field, named by default, that provides a field mode value.
      Field f = klass.getDeclaredField(memberName);
      // The field must have a ControlMode type
      if (EntryMode.class.isAssignableFrom(f.getType())) {
        // The field must not be annotated with an ModeFor annotation. If it is so annotated,
        // it is not a field mode providing field by convention.
        if (f.getAnnotation(ModeFor.class) == null) {
          int modifiers = f.getModifiers();
          if (Modifier.isStatic(modifiers)) {
            f.setAccessible(true);
            EntryMode cm = (EntryMode)f.get(null);
            memberObject.setStaticMode(cm);
            return;
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
      
    try {
      // Otherwise, try for a method (with no parameters)
      Method m = klass.getDeclaredMethod(memberName);
      // The method must have a ControlMode return type, and no parameters
      if (EntryMode.class.isAssignableFrom(m.getReturnType()) && m.getParameterTypes().length == 0) {
        // The method must not be annotated with an ModeFor annotation. If it is so annotated,
        // it is not a field mode providing method by convention.
        if (m.getAnnotation(ModeFor.class) == null) {
          int modifiers = m.getModifiers();
          if (Modifier.isStatic(modifiers)) {
            m.setAccessible(true);
            EntryMode cm = (EntryMode)m.invoke(null);
            memberObject.setStaticMode(cm);
            return;
          } else {
            IRuntimeModeProvider runtimeModeProvider = new RuntimeModeProvider(klass, fieldDependency, m, field.getName());
            memberGroup.addRuntimeModeProvider(runtimeModeProvider);    
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
      
    // No field or method was found
  }
  
  
//  private static void setSizeDirectly (Field field, ArrayPlan memberArray, GroupPlan memberGroup, FieldDependency fieldDependency) {
//    Class<?> klass = field.getDeclaringClass();
//    String memberName = field.getName() + "Size";
//    try {
//      // Try for a field, named by default, that provides an array size value.
//      Field f = klass.getDeclaredField(memberName);
//      // The field must have a ControlMode type
//      if (Integer.class.isAssignableFrom(f.getType())) {
//        // The field must not be annotated with an SizeFor annotation. If it is so annotated,
//        // it is not an array size providing field (by convention).
//        if (f.getAnnotation(SizeFor.class) == null) {
//          int modifiers = f.getModifiers();
//          if (Modifier.isStatic(modifiers)) {
//            f.setAccessible(true);
//            int size = (Integer)f.get(null);
//            memberArray.setStaticSize(size);
//            return;
//          }
//        }
//      }
//    } catch (NoSuchFieldException ex) {
//      // Continue.
//    } catch (IllegalAccessException ex) {
//      throw new RuntimeException(ex);
//    } catch (IllegalArgumentException ex) {
//      throw new RuntimeException(ex);
//    }
//      
//    try {
//      // Otherwise, try for a method (with no parameters)
//      Method m = klass.getDeclaredMethod(memberName);
//      // The method must have a Integer return type, and no parameters
//      if (Integer.class.isAssignableFrom(m.getReturnType()) && m.getParameterTypes().length == 0) {
//        // The method must not be annotated with a SizeFor annotation. If it is so annotated,
//        // it is not an array size providing method (by convention).
//        if (m.getAnnotation(SizeFor.class) == null) {
//          int modifiers = m.getModifiers();
//          if (Modifier.isStatic(modifiers)) {
//            m.setAccessible(true);
//            int size = (Integer)m.invoke(null);
//            memberArray.setStaticSize(size);
//            return;
//          } else {
//            IRuntimeSizeProvider runtimeSizeProvider = new RuntimeSizeProvider(klass, fieldDependency, m, field.getName());
//            memberGroup.addRuntimeSizeProvider(runtimeSizeProvider);    
//          }
//        }
//      }
//    } catch (NoSuchMethodException ex) {
//      // Continue.
//    } catch (InvocationTargetException ex) {
//      throw new RuntimeException(ex);
//    } catch (IllegalAccessException ex) {
//      throw new RuntimeException(ex);
//    } catch (IllegalArgumentException ex) {
//      throw new RuntimeException(ex);
//    }
//      
//    // No field or method was found
//  }
  
  
//  @Override
//  public List<IRuntimeDefaultProvider> getRuntimeDefaultProviders() {
//    return runtimeDefaultProviders;
//  }


//  @Override
//  public Set<IValidationMethod> getValidationMethods() {
//    return validationMethods;
//  }
  
  
//  @Override
//  public void setInstance (Object instance) {
//    super.setInstance(instance);
//    
//    // Set the instance on all the controls of this group.
//    for (IControlPlan cp : plans.values()) {
//      cp.setInstance(instance);
//    }
//    
//    // Set the instance on all the runtime providers of this group.
//    for (IRuntimeDefaultProvider provider : runtimeDefaultProviders) {
//      provider.setInstance(instance);
//    }
//    for (IRuntimeUseProvider provider : runtimeUseProviders) {
//      provider.setInstance(instance);
//    }
//    for (IValidationMethod vm : validationMethods) {
//      vm.setInstance(instance);
//    }
//  }
}
