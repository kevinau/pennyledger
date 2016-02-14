package org.pennyledger.form.plan.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.pennyledger.form.EntryMode;
import org.pennyledger.form.model.GroupModel;
import org.pennyledger.form.model.IContainerModel;
import org.pennyledger.form.model.IFormModel;
import org.pennyledger.form.model.IObjectModel;
import org.pennyledger.form.plan.IGroupPlan;
import org.pennyledger.form.plan.IObjectPlan;
import org.pennyledger.form.plan.IRuntimeDefaultProvider;
import org.pennyledger.form.plan.IRuntimeFactoryProvider;
import org.pennyledger.form.plan.IRuntimeLabelProvider;
import org.pennyledger.form.plan.IRuntimeModeProvider;
import org.pennyledger.form.plan.IRuntimeOccursProvider;
import org.pennyledger.form.plan.IRuntimeTypeProvider;
import org.pennyledger.form.plan.IValidationMethod;
import org.pennyledger.form.plan.ObjectKind;

public class GroupPlan extends ObjectPlan implements IGroupPlan {

  protected final Class<?> groupClass;

  protected final Map<String, IObjectPlan> members = new LinkedHashMap<String, IObjectPlan>();

  private List<IRuntimeTypeProvider> runtimeTypeProviders = new ArrayList<IRuntimeTypeProvider>(0);
  private List<IRuntimeLabelProvider> runtimeLabelProviders = new ArrayList<IRuntimeLabelProvider>(0);
  private List<IRuntimeModeProvider> runtimeModeProviders = new ArrayList<IRuntimeModeProvider>(0);
  private List<IRuntimeDefaultProvider> runtimeDefaultProviders = new ArrayList<IRuntimeDefaultProvider>(0);
  private List<IRuntimeFactoryProvider> runtimeFactoryProviders = new ArrayList<IRuntimeFactoryProvider>(0);
  private List<IRuntimeOccursProvider> runtimeOccursProviders = new ArrayList<IRuntimeOccursProvider>(0);
  //private List<IRuntimeFactoryProvider2> runtimeFactoryProviders2 = new ArrayList<IRuntimeFactoryProvider2>(0);
  private Set<IValidationMethod> validationMethods = new TreeSet<IValidationMethod>();

  
  public GroupPlan (Object parentRef, String pathName, Class<?> groupClass, EntryMode entryMode) {
    super (parentRef, pathName, entryMode);
    this.groupClass = groupClass;
  }
  

  void put (String name, IObjectPlan member) {
    members.put(name, member);
  }
  
  
  void addRuntimeLabelProvider (IRuntimeLabelProvider labelProvider) {
    runtimeLabelProviders.add(labelProvider);
  }


  void addRuntimeModeProvider (IRuntimeModeProvider modeProvider) {
    runtimeModeProviders.add(modeProvider);
  }


  void addRuntimeDefaultProvider (IRuntimeDefaultProvider defaultProvider) {
    runtimeDefaultProviders.add(defaultProvider);
  }
  
  
  void addRuntimeVariantProvider (IRuntimeFactoryProvider factoryProvider) {
    runtimeFactoryProviders.add(factoryProvider);
  }
  
  
//  void addRuntimeFactoryProvider2 (IRuntimeFactoryProvider2 factoryProvider) {
//    runtimeFactoryProviders2.add(factoryProvider);
//  }
  
  
  void addRuntimeTypeProvider (IRuntimeTypeProvider typeProvider) {
    runtimeTypeProviders.add(typeProvider);
  }


  void addValidationMethod (IValidationMethod validationMethod) {
    validationMethods.add(validationMethod);
  }


  void addRuntimeOccursProvider (IRuntimeOccursProvider occursProvider) {
    runtimeOccursProviders.add(occursProvider);
  }


  @Override
  public List<IRuntimeLabelProvider> getRuntimeLabelProviders() {
    return runtimeLabelProviders;
  }

  
  @Override
  public List<IRuntimeModeProvider> getRuntimeModeProviders() {
    return runtimeModeProviders;
  }

  
  @Override
  public List<IRuntimeDefaultProvider> getRuntimeDefaultProviders() {
    return runtimeDefaultProviders;
  }

  
  @Override
  public List<IRuntimeFactoryProvider> getRuntimeFactoryProviders() {
    return runtimeFactoryProviders;
  }

  
//  @Override
//  public List<IRuntimeFactoryProvider2> getRuntimeFactoryProviders2() {
//    return runtimeFactoryProviders2;
//  }

  
  @Override
  public List<IRuntimeTypeProvider> getRuntimeTypeProviders() {
    return runtimeTypeProviders;
  }

  
  @Override
  public Set<IValidationMethod> getValidationMethods() {
    return validationMethods;
  }

  
  @Override
  public List<IRuntimeOccursProvider> getRuntimeOccursProviders() {
    return runtimeOccursProviders;
  }

  
  @Override
  public IObjectPlan getMemberPlan(String name) {
    return members.get(name);
  }


  @Override
  public IObjectPlan[] getMemberPlans() {
    IObjectPlan[] mx = new IObjectPlan[members.size()];
    int i = 0;
    for (IObjectPlan m : members.values()) {
      mx[i++] = m;
    }
    return mx;
  }

  
  @Override
  public Object newValue () {
    try {
      // Look for constructor with no arguments
      Constructor<?> constructor = groupClass.getDeclaredConstructor();
      if (constructor == null) {
        throw new RuntimeException("No way of constructing group object (no default constructor)");
      }
      constructor.setAccessible(true);
      return constructor.newInstance();
    } catch (SecurityException ex) {
      throw new RuntimeException(ex);
    } catch (IllegalArgumentException ex) {
      throw new RuntimeException(ex);
    } catch (NoSuchMethodException ex) {
      throw new RuntimeException(ex);
    } catch (InstantiationException ex) {
      throw new RuntimeException(ex);
    } catch (IllegalAccessException ex) {
      throw new RuntimeException(ex);
    } catch (InvocationTargetException ex) {
      throw new RuntimeException(ex);
    }
  }
  

  @Override
  protected void dump (int level) {
    for (int i = 0; i < level; i++) {
      System.out.print("  ");
    }
    System.out.println("Group: " + getStaticLabel() + " " + getStaticMode() + " (" + groupClass.getSimpleName() + ")");
    for (IRuntimeFactoryProvider factoryProvider : runtimeFactoryProviders) {
      System.out.println("       " + factoryProvider);
    }
    for (Map.Entry<String, IObjectPlan> entry : members.entrySet()) {
      for (int i = 0; i < level + 1; i++) {
        System.out.print("  ");
      }
      System.out.println(entry.getKey() + ":");
      IObjectPlan member = entry.getValue();
      ((ObjectPlan)member).dump(level + 1);
    }
  }


  @Override
  public IObjectModel createModel(IFormModel<?> ownerForm, IContainerModel parentModel, Object parentRef, String pathName, Object instance) {
    return new GroupModel(ownerForm, parentModel, parentRef, this, groupClass, instance);
  }

    
  @Override
  public boolean isSolitary() {
    if (members.size() == 1) {
      // If there is only one member, and that member is solitary
      return members.values().iterator().next().isSolitary();
    } else {
      return false;
    }
  }


  @Override
  public ObjectKind kind() {
    return ObjectKind.GROUP;
  }
}
