package org.pennyledger.form.plan.impl;



public class GroupPlanRegistry {

//  private final Map<Class<?>, IObjectPlan> registry = new HashMap<Class<?>, IObjectPlan>();
//  
//  
//  public IObjectPlan buildObjectPlan (Class<?> klass) {
//    IObjectPlan plan;
//    if (klass.isArray()) {
//      plan = buildArrayPlan(klass);
//    } else if (klass.isAssignableFrom(List.class)) {
//      plan = buildListPlan(klass);
//    } else {
//      plan = buildGroupPlan(klass);
//    }
//    return plan;
//  }
//  
//  
////  public IObjectPlan buildObjectPlan (Field field, Class<?> klass) {
////    IObjectPlan plan;
////    if (klass.isArray()) {
////      plan = buildMemberArray(klass, 0);
////    } else if (klass.isAssignableFrom(List.class)) {
////      plan = buildMemberList(klass, 0);
////    } else {
////      Embedded embedAnn = field.getAnnotation(Embedded.class);
////      if (embedAnn != null) {
////        IGroupPlan groupPlan = buildMemberGroup(klass);
////        plan = new EmbeddedPlan(groupPlan);
////      } else {
////        Embeddable embbleAnn = klass.getAnnotation(Embeddable.class);
////        if (embbleAnn != null) {
////          IGroupPlan groupPlan = buildMemberGroup(klass);
////          plan = new EmbeddedPlan(groupPlan);
////        } else {
////          OneToMany omAnn = klass.getAnnotation(OneToMany.class);
////          if (omAnn != null) {
////            IGroupPlan groupPlan = buildMemberGroup(klass);
////            plan = new ForeignKeyPlan(groupPlan, 2);
////          } else {
////            OneToOne ooAnn = klass.getAnnotation(OneToOne.class);
////            if (ooAnn != null) {
////              IGroupPlan groupPlan = buildMemberGroup(klass);
////              plan = new ForeignKeyPlan(groupPlan, 1);
////            } else {
////              plan = buildAtomicPlan(klass);
////            }
////          }
////        }
////      }
////    }
////    return plan;
////  }
//  
//  
//  private IGroupPlan buildGroupPlan (Class<?> klass) {
//    IObjectPlan plan = registry.get(klass);
//    if (plan == null) {
//      GroupPlan groupPlan = new GroupPlan(klass);
//      addGroupFields(groupPlan, klass, true);
//      plan = groupPlan;
//      registry.put(klass, plan);
//    }
//    return plan;
//  }
//  
//  
//  private void addGroupFields (GroupPlan memberGroup, Class<?> klass, boolean include) {
//    FieldDependency fieldDependency = new FieldDependency();
//    fieldDependency.parseClass(klass.getName());
//
//    // Parse the class hierarchy recursively
//    Class<?> superKlass = klass.getSuperclass();
//    if (superKlass != null && !superKlass.equals(Object.class)) {
//      MappedSuperclass msc = superKlass.getAnnotation(MappedSuperclass.class);
//      addGroupFields(memberGroup, superKlass, msc != null);
//    }
//    
//    if (include) {
//      Map<String, Field> lastEntryFields = new HashMap<String, Field>();
//
//      // Parse the declared fields of this class, first for 'last entry' fields
//      for (Field field : klass.getDeclaredFields()) {
//        if (field.isSynthetic()) {
//          // Synthetic fields cannot be form fields
//          continue;
//        }
//        int m = field.getModifiers();
//        if ((m & (Modifier.STATIC | Modifier.FINAL | Modifier.VOLATILE)) != 0) {
//          // Exclude static, final and volatile fields (but transient fields are not excluded)
//          continue;
//        }
//
//        // Remember the last entry fields
//        LastEntryFor lastEntryForAnn = field.getAnnotation(LastEntryFor.class);
//        if (lastEntryForAnn != null) {
//          for (String name : lastEntryForAnn.value()) {
//            lastEntryFields.put(name, field);
//          }
//          continue;
//        }
//        
//        // add last entry fields named by convention
//        String name = field.getName();
//        if (name.endsWith("LastEntry")) {
//          String n = name.substring(0, name.length() - 9);
//          lastEntryFields.put(n, field);
//          continue;
//        }
//      }
//
//      // And again for the fields themselves
//      for (Field field : klass.getDeclaredFields()) {
//        categoriseField(memberGroup, field, lastEntryFields, fieldDependency);
//      }
//    }
//    
//    findTypeForMethods (memberGroup, klass);
//    findLabelForMethods (memberGroup, klass);
//    findModeForMethods (memberGroup, klass, fieldDependency);
//    findDefaultForMethods (memberGroup, klass, fieldDependency);
//    findSizeForMethods (memberGroup, klass, fieldDependency);
//    findVariantForMethods (memberGroup, klass, fieldDependency);
//    findValidationMethods (memberGroup, klass, fieldDependency);
//  }
//  
//  
//  @SuppressWarnings({"unchecked", "rawtypes"})
//  private static ObjectPlan buildAtomicPlan(String pathName, Field field, Class<?> fieldClass, Field lastEntryField) {
//    ObjectPlan fieldPlan;
//    FormField formFieldAnn = field.getAnnotation(FormField.class);
//    
//    // Is there a type declaration within the class
//    IType type = AtomicTypeList.lookupType(field.getDeclaringClass(), field.getName(), formFieldAnn);
//    if (type != null) {
//      fieldPlan = new FieldPlan(field, field, type, lastEntryField);
//    } else {
//      // Otherwise, is the type of the field a known type (primitive or String, Date, Decimal, etc)
//      AtomicTypeList.TypeMap typeMap = AtomicTypeList.getTypeMap(fieldClass);
//      if (typeMap != null) {
//        javax.persistence.Column annColumn = field.getAnnotation(javax.persistence.Column.class);
//        type = AtomicTypeList.getDefaultType(formFieldAnn, annColumn, typeMap);
//        fieldPlan = new FieldPlan(field, field, type, lastEntryField);
//      } else {
//        if (Enum.class.isAssignableFrom(fieldClass)) {
//          type = new EnumType(fieldClass);
//          fieldPlan = new FieldPlan(field, field, type, lastEntryField);
//        } else {
//          throw new RuntimeException("Field not recognised: " + field.getName() + " " + field.getType());
//        }
//      }
//    }
//    return fieldPlan;
//  }
//  
//  
//  /**
//   * Build a member object that is known to be an array. The 'fieldClass'
//   * argument identifies an array.
//   * <p>
//   * If the field has an Occurs annotation and the array element is a simple
//   * field, treat the field as an embedded array, with the size set by the
//   * Occurs field.
//   * <p>
//   * Otherwise, the array element must be an entity in its own right--it cannot
//   * be a simple field. If the Occurs annotation, add a validation to ensure the
//   * array size is the size set by the Occurs field.
//   */
//  private ArrayPlan buildArrayPlan (String pathName, Field field, Class<?> fieldClass) {
//    int minOccursSize = 0;
//    int maxOccursSize = Integer.MAX_VALUE;
//    if (field != null) {
//      Occurs occursAnn = field.getAnnotation(Occurs.class);
//      if (occursAnn != null) {
//        minOccursSize = maxOccursSize = occursAnn.value();
//        if (maxOccursSize == Integer.MAX_VALUE) {
//          maxOccursSize = occursAnn.max();
//          minOccursSize = occursAnn.min();
//        }
//      }
//    }
//    
//    Class<?> elemClass = fieldClass.getComponentType();
//    IObjectPlan elemPlan = buildObjectPlan(pathName + "[]", field, elemClass);
//    if (elemPlan instanceof FieldPlan) {
//      // This is an array of basic objects
//      if (maxOccursSize == Integer.MAX_VALUE) {
//        throw new RuntimeException("An array of basic elements without an Occurs annotation");
//      }
//    }
//    ArrayPlan arrayPlan = new ArrayPlan(pathName, elemClass, elemPlan, minOccursSize, maxOccursSize);
//    return arrayPlan;
//  }
//
//  
//  /**
//   * Build a member object that is known to be a list.  The 'fieldClass' argument identifies a List<T>.
//   */
//  private ListPlan buildMemberList (String pathName, Field field, Class<?> fieldClass, Field lastEntryField, int dimension) {
//    int minOccursSize = 0;
//    int maxOccursSize = Integer.MAX_VALUE;
//    if (field != null) {
//      Occurs occursAnn = field.getAnnotation(Occurs.class);
//      if (occursAnn != null) {
//        minOccursSize = maxOccursSize = occursAnn.value();
//        if (maxOccursSize == Integer.MAX_VALUE) {
//          maxOccursSize = occursAnn.max();
//          minOccursSize = occursAnn.min();
//        }
//      }
//    }
//    
//    Class<?> elemClass = fieldClass.getComponentType();
//    IObjectPlan elemPlan = buildObjectPlan(field, elemClass);
//    if (elemPlan instanceof FieldPlan) {
//      // This is a list of basic objects
//      if (maxOccursSize == Integer.MAX_VALUE) {
//        throw new RuntimeException("An array of basic elements without an Occurs annotation");
//      }
//    }
//    ListPlan listPlan = new ListPlan(pathName, elemClass, elemPlan, minOccursSize, maxOccursSize);
//    return listPlan;
//  }
//
//  
//  private void categoriseField (GroupPlan memberGroup, Field field, Map<String, Field> lastEntryFields, FieldDependency fieldDependency) {
//    if (field.isSynthetic()) {
//      // Synthetic fields cannot be form or persistent fields
//      return;
//    }
//
//    // Last entry fields are not form or persistent fields
//    LastEntryFor lastEntryForAnn = field.getAnnotation(LastEntryFor.class);
//    if (lastEntryForAnn != null && lastEntryForAnn.value().length > 0) {
//      return;
//    } else {
//      if (field.getName().endsWith("LastEntry")) {
//        return;
//      }
//      // unless they are annotated with an empty LastEntryFor
//    }
//
//    int modifier = field.getModifiers();
//
//    // Look for fields annotated with TypeFor. 
//    org.pennyledger.form.TypeFor typeFor = field.getAnnotation(org.pennyledger.form.TypeFor.class);
//    if (typeFor != null) {
//      // This field has been explicitly annotated as the type for some field or fields.
//      String[] xpaths = typeFor.value();
//      if (Modifier.isStatic(modifier)) {
//        field.setAccessible(true);
//        IType type = (IType)field.get(null);
//        IRuntimeTypeProvider typeProvider = new RuntimeTypeProvider(type, xpaths);
//        memberGroup.addRuntimeTypeProvider(typeProvider);
//      }
//      // It is not a form or persistent field
//      return;
//    }
//
//    // Look for fields annotated with ModeFor. 
//    org.pennyledger.form.ModeFor modeFor = field.getAnnotation(org.pennyledger.form.ModeFor.class);
//    if (modeFor != null) {
//      // This field has been explicitly annotated as an mode for some field or fields.
//      String[] xpaths = modeFor.value();
//      if (Modifier.isStatic(modifier)) {
//        field.setAccessible(true);
//        EntryMode mode = (EntryMode)field.get(null);
//        IRuntimeModeProvider modeProvider = new RuntimeModeProvider(mode, xpaths);
//        memberGroup.addRuntimeModeProvider(modeProvider);
//      }
//      // It is not a form or persistent field
//      return;
//    }
//    
//    // Look for fields annotated with SizeFor. 
//    SizeFor sizeFor = field.getAnnotation(SizeFor.class);
//    if (sizeFor != null) {
//      // This field has been explicitly annotated as the size of an array field or fields.
//      String[] xpaths = sizeFor.value();
//      if (Modifier.isStatic(modifier)) {
//        field.setAccessible(true);
//        int size = (Integer)field.get(null);
//        IRuntimeSizeProvider sizeProvider = new RuntimeSizeProvider(size, xpaths);
//        memberGroup.addRuntimeSizeProvider(sizeProvider);
//      } else {
//        IRuntimeSizeProvider sizeProvider = new RuntimeSizeProvider(field, xpaths);
//        memberGroup.addRuntimeSizeProvider(sizeProvider);
//      }
//      // It is not a form or persistent field
//      return;
//    }
//    
//    // Look for fields annotated with LabelFor. 
//    org.pennyledger.form.LabelFor labelFor = field.getAnnotation(org.pennyledger.form.LabelFor.class);
//    if (labelFor != null) {
//      // This field has been explicitly annotated as a label for some field or fields.
//      String[] xpaths = labelFor.value();
//      if (Modifier.isStatic(modifier)) {
//        field.setAccessible(true);
//        String label = (String)field.get(null);
//        IRuntimeLabelProvider labelProvider = new RuntimeLabelProvider(label, xpaths);
//        memberGroup.addRuntimeLabelProvider(labelProvider);
//      }
//      // It is not a form or persistent field
//      return;
//    }
//    
//    // Look for fields annotated with DefaultFor. 
//    org.pennyledger.form.DefaultFor defaultFor = field.getAnnotation(org.pennyledger.form.DefaultFor.class);
//    if (defaultFor != null) {
//      // This field has been explicitly annotated as the default for some field or fields.
//      String[] xpaths = defaultFor.value();
//      if (Modifier.isStatic(modifier)) {
//        field.setAccessible(true);
//        Object defaultValue = field.get(null);
//        IRuntimeDefaultProvider defaultProvider = new RuntimeDefaultProvider(defaultValue, xpaths);
//        memberGroup.addRuntimeDefaultProvider(defaultProvider);
//      }
//      // It is not a form or persistent field
//      return;
//    }
//
//    if ((modifier & (Modifier.STATIC | Modifier.FINAL | Modifier.VOLATILE)) != 0) {
//      // Exclude final and volatile fields (but transient fields are not excluded here)
//      return;
//    }
//
//    // Generated values are not form input fields, but they are persistent fields
//    GeneratedValue gvann = field.getAnnotation(GeneratedValue.class);
//    if (gvann != null) {
//      // It is not a form field
//      return;
//    }
//
//    // Version values are not form input fields, but they are persistent fields
//    Version vann = field.getAnnotation(Version.class);
//    if (vann != null) {
//      // It is not a form field
//      return;
//    }
//
//    NotFormField notFieldAnn = field.getAnnotation(NotFormField.class);
//    if (notFieldAnn != null) {
//      // It is not a form field
//      return;
//    }
//    
////    if ((m & Modifier.TRANSIENT) != 0) {
////      exclude |= PERSISTENT;
////    }
////    Transient transAnn = field.getAnnotation(Transient.class);
////    if (transAnn != null) {
////      exclude |= PERSISTENT;
////    }
//    
//    // The field is a form field
//    String fieldName = field.getName();
//    Field lastEntryField = lastEntryFields.get(fieldName);
//    Class<?> fieldClass = field.getType();
//    IObjectPlan fieldPlan;
//    
//    if (fieldClass.isArray()) {
//      fieldPlan = buildArrayPlan(field, fieldClass);
//    } else if (fieldClass.isAssignableFrom(List.class)) {
//      fieldPlan = buildListPlan(field, fieldClass);
//    } else {
//      Embedded embedAnn = field.getAnnotation(Embedded.class);
//      if (embedAnn != null) {
//        IGroupPlan groupPlan = buildMemberGroup(fieldClass);
//        fieldPlan = new EmbeddedPlan(groupPlan);
//      } else {
//        Embeddable embbleAnn = fieldClass.getAnnotation(Embeddable.class);
//        if (embbleAnn != null) {
//          IGroupPlan groupPlan = buildMemberGroup(fieldClass);
//          fieldPlan = new EmbeddedPlan(groupPlan);
//        } else {
//          OneToMany omAnn = fieldClass.getAnnotation(OneToMany.class);
//          if (omAnn != null) {
//            IGroupPlan groupPlan = buildMemberGroup(fieldClass);
//            fieldPlan = new ForeignKeyPlan(groupPlan, 2);
//          } else {
//            OneToOne ooAnn = fieldClass.getAnnotation(OneToOne.class);
//            if (ooAnn != null) {
//              IGroupPlan groupPlan = buildMemberGroup(fieldClass);
//              fieldPlan = new ForeignKeyPlan(groupPlan, 1);
//            } else {
//              fieldPlan = buildAtomicPlan(fieldName, field, fieldClass, lastEntryField);
//            }
//          }
//        }
//      }
//    }
//    
////    fieldPlan = buildObjectPlan(fieldName, field, fieldClass, lastEntryField, PERSISTENT);
//    memberGroup.put(fieldName, fieldPlan);
//    setGroupAttributesDirectly(field, fieldClass, fieldPlan, memberGroup, fieldDependency);
//  }
//
//  
//  private static boolean throwsException (Method method) {
//    Class<?>[] exceptions = method.getExceptionTypes();
//    for (Class<?> ex : exceptions) {
//      if (Exception.class.isAssignableFrom(ex)) {
//        return true;
//      }
//    }
//    return false;
//  }
//
//  
//  private static boolean throwsUserEntryException (Method method) {
//    Class<?>[] exceptions = method.getExceptionTypes();
//    for (Class<?> ex : exceptions) {
//      if (UserEntryException.class.isAssignableFrom(ex)) {
//        return true;
//      }
//    }
//    return false;
//  }
//
//  
//  private static void findTypeForMethods (GroupPlan memberGroup, Class<?> klass) {
//    // Look for methods annotated with TypeFor. 
//    for (Method method : klass.getDeclaredMethods()) {
//      org.pennyledger.form.TypeFor typeFor = method.getAnnotation(org.pennyledger.form.TypeFor.class);
//      if (typeFor != null) {
//        // This method has been explicitly annotated as the type for some field or fields.
//        String[] xpaths = typeFor.value();
//        try {
//          int modifier = method.getModifiers();
//          if (Modifier.isStatic(modifier)) {
//            method.setAccessible(true);
//            IType type = (IType)method.invoke(null);
//            IRuntimeTypeProvider typeProvider = new RuntimeTypeProvider(type, xpaths);
//            memberGroup.addRuntimeTypeProvider(typeProvider);
//          }
//        } catch (InvocationTargetException e) {
//          throw new RuntimeException(e);
//        } catch (IllegalArgumentException e) {
//          throw new RuntimeException(e);
//        } catch (IllegalAccessException e) {
//          throw new RuntimeException(e);
//        }
//      }
//    }
//  }
//  
//  
//  private static void findModeForMethods (GroupPlan memberGroup, Class<?> klass, FieldDependency fieldDependency) {
//    // Look for methods annotated with ModeFor. 
//    for (Method method : klass.getDeclaredMethods()) {
//      org.pennyledger.form.ModeFor modeFor = method.getAnnotation(org.pennyledger.form.ModeFor.class);
//      if (modeFor != null) {
//        // This method has been explicitly annotated as the use for some field or fields.
//        String[] xpaths = modeFor.value();
//        try {
//          int modifier = method.getModifiers();
//          if (Modifier.isStatic(modifier)) {
//            method.setAccessible(true);
//            EntryMode mode = (EntryMode)method.invoke(null);
//            IRuntimeModeProvider modeProvider = new RuntimeModeProvider(mode, xpaths);
//            memberGroup.addRuntimeModeProvider(modeProvider);
//          } else {
//            IRuntimeModeProvider modeProvider = new RuntimeModeProvider(klass, fieldDependency, method, xpaths);
//            memberGroup.addRuntimeModeProvider(modeProvider);
//          }
//        } catch (InvocationTargetException e) {
//          throw new RuntimeException(e);
//        } catch (IllegalArgumentException e) {
//          throw new RuntimeException(e);
//        } catch (IllegalAccessException e) {
//          throw new RuntimeException(e);
//        }
//      }
//    }
//  }
//  
//  
//  private static void findSizeForMethods (GroupPlan memberGroup, Class<?> klass, FieldDependency fieldDependency) {
//    // Look for methods annotated with SizeFor. 
//    for (Method method : klass.getDeclaredMethods()) {
//      SizeFor sizeFor = method.getAnnotation(SizeFor.class);
//      if (sizeFor != null) {
//        // This method has been explicitly annotated as the size of an array field or fields.
//        String[] xpaths = sizeFor.value();
//        try {
//          int modifier = method.getModifiers();
//          if (Modifier.isStatic(modifier)) {
//            method.setAccessible(true);
//            int size = (Integer)method.invoke(null);
//            IRuntimeSizeProvider sizeProvider = new RuntimeSizeProvider(size, xpaths);
//            memberGroup.addRuntimeSizeProvider(sizeProvider);
//          } else {
//            IRuntimeSizeProvider sizeProvider = new RuntimeSizeProvider(klass, fieldDependency, method, xpaths);
//            memberGroup.addRuntimeSizeProvider(sizeProvider);
//          }
//        } catch (InvocationTargetException e) {
//          throw new RuntimeException(e);
//        } catch (IllegalArgumentException e) {
//          throw new RuntimeException(e);
//        } catch (IllegalAccessException e) {
//          throw new RuntimeException(e);
//        }
//      }
//    }
//  }
//  
//  
//  private static void findLabelForMethods (GroupPlan memberGroup, Class<?> klass) {
//    // Look for methods annotated with LabelFor. 
//    for (Method method : klass.getDeclaredMethods()) {
//      org.pennyledger.form.LabelFor labelFor = method.getAnnotation(org.pennyledger.form.LabelFor.class);
//      if (labelFor != null) {
//        // This method has been explicitly annotated as the label for some field or fields.
//        String[] xpaths = labelFor.value();
//        try {
//          int modifier = method.getModifiers();
//          if (Modifier.isStatic(modifier)) {
//            method.setAccessible(true);
//            String label = (String)method.invoke(null);
//            IRuntimeLabelProvider labelProvider = new RuntimeLabelProvider(label, xpaths);
//            memberGroup.addRuntimeLabelProvider(labelProvider);
//          }
//        } catch (InvocationTargetException e) {
//          throw new RuntimeException(e);
//        } catch (IllegalArgumentException e) {
//          throw new RuntimeException(e);
//        } catch (IllegalAccessException e) {
//          throw new RuntimeException(e);
//        }
//      }
//    }
//    
//  }
//  
//  
//  private static void findDefaultForMethods (GroupPlan memberGroup, Class<?> klass, FieldDependency fieldDependency) {
//    // Look for methods annotated with DefaultFor. 
//    for (Method method : klass.getDeclaredMethods()) {
//      org.pennyledger.form.DefaultFor defaultFor = method.getAnnotation(org.pennyledger.form.DefaultFor.class);
//      if (defaultFor != null) {
//        // This method has been explicitly annotated as the default for some field or fields.
//        String[] xpaths = defaultFor.value();
//        try {
//          int modifier = method.getModifiers();
//          if (Modifier.isStatic(modifier)) {
//            method.setAccessible(true);
//            Object defaultValue = method.invoke(null);
//            IRuntimeDefaultProvider defaultProvider = new RuntimeDefaultProvider(defaultValue, xpaths);
//            memberGroup.addRuntimeDefaultProvider(defaultProvider);
//          } else {
//            String[] fieldNames = defaultFor.value();
//            IRuntimeDefaultProvider defaultProvider = new RuntimeDefaultProvider(klass, fieldDependency, method, false, fieldNames);
//            memberGroup.addRuntimeDefaultProvider(defaultProvider);
//          }
//        } catch (InvocationTargetException e) {
//          throw new RuntimeException(e);
//        } catch (IllegalArgumentException e) {
//          throw new RuntimeException(e);
//        } catch (IllegalAccessException e) {
//          throw new RuntimeException(e);
//        }
//      }
//    }
//  }
//  
//  
//  private static void findVariantForMethods (GroupPlan memberGroup, Class<?> klass, FieldDependency fieldDependency) {
//    // Look for methods annotated with VariationFor. 
//    for (Method method : klass.getDeclaredMethods()) {
//      org.pennyledger.form.VariantFor variantFor = method.getAnnotation(org.pennyledger.form.VariantFor.class);
//      if (variantFor != null) {
//        // This method has been explicitly annotated as the default for some field or fields.
//        String[] xpaths = variantFor.value();
//        try {
//          int modifier = method.getModifiers();
//          if (Modifier.isStatic(modifier)) {
//            method.setAccessible(true);
//            Object variantValue = method.invoke(null);
//            IRuntimeVariantProvider variantProvider = new RuntimeVariantProvider(variantValue, xpaths);
//            memberGroup.addRuntimeVariantProvider(variantProvider);
//          } else {
//            String[] fieldNames = variantFor.value();
//            IRuntimeVariantProvider variantProvider = new RuntimeVariantProvider(klass, fieldDependency, method, fieldNames);
//            memberGroup.addRuntimeVariantProvider(variantProvider);
//          }
//        } catch (InvocationTargetException e) {
//          throw new RuntimeException(e);
//        } catch (IllegalArgumentException e) {
//          throw new RuntimeException(e);
//        } catch (IllegalAccessException e) {
//          throw new RuntimeException(e);
//        }
//      }
//    }
//  }
//  
//  
//  private static void findValidationMethods (GroupPlan memberGroup, Class<?> klass, FieldDependency fieldDependency) {
//    for (Method method : klass.getDeclaredMethods()) {
//      // Consider only methods with zero parameters, a void return type and non-static.
//      if (method.getParameterTypes().length == 0 &&
//          method.getReturnType().equals(Void.TYPE) &&
//          !Modifier.isStatic(method.getModifiers())) {
//        Validation validation = method.getAnnotation(Validation.class);
//        if (validation != null) {
//          boolean isSlow = validation.slow();
//          if (throwsException(method)) {
//            IValidationMethod validationMethod = new ValidationMethod(klass, fieldDependency, method, isSlow);
//            memberGroup.addValidationMethod(validationMethod);
//          } else {
//            throw new RuntimeException("Method with Validation annotation, but does not throw Exception");
//          }
//        } else {
//          if (throwsUserEntryException(method)) {
//            IValidationMethod validationMethod = new ValidationMethod(klass, fieldDependency, method, false);
//            memberGroup.addValidationMethod(validationMethod);
//          }
//        }
//      }
//    }
//  }
//
//
//  private static void setGroupAttributesDirectly (Field field, Class<?> fieldClass, IObjectPlan memberObject, GroupPlan memberGroup, FieldDependency fieldDependency) {
//    setLabelDirectly (field, memberObject, fieldDependency);
//    setModeDirectly (field, memberObject, memberGroup, fieldDependency);
//    if (memberObject instanceof ArrayPlan) {
//      setSizeDirectly (field, (ArrayPlan)memberObject, memberGroup, fieldDependency);
//    }
//  }
//  
//  
//  private static void setLabelDirectly (Field field, IObjectPlan memberObject, FieldDependency fieldDependency) {
//    FormField formFieldAnn = field.getAnnotation(FormField.class);
//    if (formFieldAnn != null) {
//      // Is there a "label" attribute on the FormField annotation?
//      String lx = formFieldAnn.label();
//      if (!lx.equals("\u0000")) {
//        memberObject.setStaticLabel(lx);
//        return;
//      }
//    }
//    Class<?> klass = field.getDeclaringClass();
//    String memberName = field.getName() + "Label";
//    try {
//      // Try for a method, named by default, that provides a label.
//      Method m = klass.getDeclaredMethod(memberName);
//      // The field must be a String
//      if (String.class.isAssignableFrom(m.getReturnType()) && m.getParameterTypes().length == 0) {
//        // The method must not be annotated with an LabelFor annotation. If it is so annotated,
//        // it is not a label providing method by convention.
//        if (m.getAnnotation(LabelFor.class) == null) {
//          int modifiers = m.getModifiers();
//          if (Modifier.isStatic(modifiers)) {
//            m.setAccessible(true);
//            String lx = (String)m.invoke(null);
//            memberObject.setStaticLabel(lx);
//          }
//          return;
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
//    try {
//      // Otherwise, try for a field
//      Field f = klass.getDeclaredField(memberName);
//      // The field must be an String field
//      if (String.class.isAssignableFrom(f.getType())) {
//        // The field must not be annotated with an LabelFor annotation. If it is so annotated,
//        // it is not a label providing field by convention.
//        if (f.getAnnotation(LabelFor.class) == null) {
//          int modifiers = f.getModifiers();
//          if (Modifier.isStatic(modifiers)) {
//            f.setAccessible(true);
//            String lx = (String)f.get(null);
//            memberObject.setStaticLabel(lx);
//          }
//          return;
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
//    // If none of the above
//    String lx = CamelCase.toSentence(field.getName());
//    memberObject.setStaticLabel(lx);
//  }
//  
//  
//  private static void setModeDirectly (Field field, IObjectPlan memberObject, GroupPlan memberGroup, FieldDependency fieldDependency) {
//    // Is there a "mode" attribute on the FormField annotation?
//    FormField formFieldAnn = field.getAnnotation(FormField.class);
//    if (formFieldAnn != null) {
//      EntryMode cm = formFieldAnn.mode();
//      if (cm != EntryMode.UNSPECIFIED) {
//        memberObject.setStaticMode(cm);
//        return;
//      }
//    }
//    Class<?> klass = field.getDeclaringClass();
//    String memberName = field.getName() + "Mode";
//    try {
//      // Try for a field, named by default, that provides a field mode value.
//      Field f = klass.getDeclaredField(memberName);
//      // The field must have a ControlMode type
//      if (EntryMode.class.isAssignableFrom(f.getType())) {
//        // The field must not be annotated with an ModeFor annotation. If it is so annotated,
//        // it is not a field mode providing field by convention.
//        if (f.getAnnotation(ModeFor.class) == null) {
//          int modifiers = f.getModifiers();
//          if (Modifier.isStatic(modifiers)) {
//            f.setAccessible(true);
//            EntryMode cm = (EntryMode)f.get(null);
//            memberObject.setStaticMode(cm);
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
//      // The method must have a ControlMode return type, and no parameters
//      if (EntryMode.class.isAssignableFrom(m.getReturnType()) && m.getParameterTypes().length == 0) {
//        // The method must not be annotated with an ModeFor annotation. If it is so annotated,
//        // it is not a field mode providing method by convention.
//        if (m.getAnnotation(ModeFor.class) == null) {
//          int modifiers = m.getModifiers();
//          if (Modifier.isStatic(modifiers)) {
//            m.setAccessible(true);
//            EntryMode cm = (EntryMode)m.invoke(null);
//            memberObject.setStaticMode(cm);
//            return;
//          } else {
//            IRuntimeModeProvider runtimeModeProvider = new RuntimeModeProvider(klass, fieldDependency, m, field.getName());
//            memberGroup.addRuntimeModeProvider(runtimeModeProvider);    
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
//  
//  
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
//  
//  
////  @Override
////  public List<IRuntimeDefaultProvider> getRuntimeDefaultProviders() {
////    return runtimeDefaultProviders;
////  }
//
//
////  @Override
////  public Set<IValidationMethod> getValidationMethods() {
////    return validationMethods;
////  }
//  
//  
////  @Override
////  public void setInstance (Object instance) {
////    super.setInstance(instance);
////    
////    // Set the instance on all the controls of this group.
////    for (IControlPlan cp : plans.values()) {
////      cp.setInstance(instance);
////    }
////    
////    // Set the instance on all the runtime providers of this group.
////    for (IRuntimeDefaultProvider provider : runtimeDefaultProviders) {
////      provider.setInstance(instance);
////    }
////    for (IRuntimeUseProvider provider : runtimeUseProviders) {
////      provider.setInstance(instance);
////    }
////    for (IValidationMethod vm : validationMethods) {
////      vm.setInstance(instance);
////    }
////  }
}
