package org.pennyledger.form.plan.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.pennyledger.form.EntryMode;
import org.pennyledger.form.Form;
import org.pennyledger.form.FormIcon;
import org.pennyledger.form.FormName;
import org.pennyledger.form.Mode;
import org.pennyledger.form.plan.IContainerPlan;
import org.pennyledger.form.plan.IFormPlan;
import org.pennyledger.form.plan.IObjectPlan;
import org.pennyledger.util.CamelCase;


public class FormPlan implements IFormPlan {

  private final Class<?> klass;

  private String formName;
  
  private String iconName;
  
  private final IContainerPlan rootPlan;
  
  private final EntryMode entryMode;
  
  
  public FormPlan(Class<?> klass) {
    this.klass = klass;
    Form form = klass.getAnnotation(Form.class);
    if (form != null) {
      formName = (String)getByFormAttribute(form.name(), FormName.class, "formName");
      iconName = (String)getByFormAttribute(form.icon(), FormIcon.class, "formIcon");
    } else {
      formName = (String)getByFieldMethod(FormName.class, "formName");
      iconName = (String)getByFieldMethod(FormIcon.class, "formIcon");
    }
    if (formName == null) {
      formName = CamelCase.toSentence(klass.getSimpleName());
    }
    
    Mode modeAnn = klass.getAnnotation(Mode.class);
    if (modeAnn != null) {
      entryMode = modeAnn.value();
    } else {
      entryMode = EntryMode.UNSPECIFIED;
    }
    
    String klassName = klass.getSimpleName();
    klassName = Character.toLowerCase(klassName.charAt(0)) + klassName.substring(1);
    
    rootPlan = ObjectPlanFactory.buildGroupPlan(klassName, klass);
  }


  private Object getByFormAttribute (String attributeValue, Class<? extends Annotation> memberAnnotation, String conventionName) {
    /* If there is a Form attribute, use that. */
    if (!attributeValue.equals("\u0000")) {
      return attributeValue;
    }
    return getByFieldMethod (memberAnnotation, conventionName);
  }
  
  
  private Object getByFieldMethod (Class<? extends Annotation> memberAnnotation, String conventionName) {
    /* If there is a static member that is marked with the right annotation, use that. */
    try {
      for (Field field : klass.getDeclaredFields()) {
        if (field.getAnnotation(memberAnnotation) != null && Modifier.isStatic(field.getModifiers())) {
          field.setAccessible(true);
          Object obj = field.get(null);
          if (obj != null) {
           return obj;
          } 
        }
      }
      for (Method method : klass.getDeclaredMethods()) {
        if (method.getAnnotation(memberAnnotation) != null && Modifier.isStatic(method.getModifiers())) {
          method.setAccessible(true);
          Object obj = method.invoke(null);
          if (obj != null) {
            return obj;
          } 
        }
      }
    } catch (SecurityException ex) {
      throw new RuntimeException(ex);
    } catch (IllegalArgumentException ex) {
      throw new RuntimeException(ex);
    } catch (IllegalAccessException ex) {
      throw new RuntimeException(ex);
    } catch (InvocationTargetException ex) {
      throw new RuntimeException(ex);
    }

    /* Otherwise, is there a static member named by convention, use that. */
    try {
      Field field = klass.getDeclaredField(conventionName);
      if (field.getAnnotation(memberAnnotation) == null && Modifier.isStatic(field.getModifiers())) {
        field.setAccessible(true);
        Object obj = field.get(null);
        if (obj != null) {
          return obj;
        } 
      }
    } catch (NoSuchFieldException ex) {
      /* continue */
    } catch (SecurityException ex) {
      throw new RuntimeException(ex);
    } catch (IllegalArgumentException ex) {
      throw new RuntimeException(ex);
    } catch (IllegalAccessException ex) {
      throw new RuntimeException(ex);
    }

    /* Otherwise. */
    return null;
  }

  
  @Override
  public String getFormName() {
    return formName;
  }


  @Override
  public String getIconName() {
    return iconName;
  }

  
  @Override
  public EntryMode getEntryMode() {
    return entryMode;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <X extends IObjectPlan> X getRootPlan() {
    return (X)rootPlan;
  }
  
  
  @Override 
  public Class<?> getFormClass() {
    return klass;
  }

}
