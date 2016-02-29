package org.pennyledger.form.test;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.pennyledger.form.DefaultFor;
import org.pennyledger.form.EntryMode;
import org.pennyledger.form.LabelFor;
import org.pennyledger.form.ModeFor;
import org.pennyledger.form.TypeFor;
import org.pennyledger.form.plan.IClassPlan;
import org.pennyledger.form.plan.IRuntimeDefaultProvider;
import org.pennyledger.form.plan.IRuntimeLabelProvider;
import org.pennyledger.form.plan.IRuntimeModeProvider;
import org.pennyledger.form.plan.IRuntimeTypeProvider;
import org.pennyledger.form.plan.impl.ClassPlan;
import org.pennyledger.form.type.IType;
import org.pennyledger.form.type.builtin.StringType;

public class MethodForOnClass {

  public static class Klass {
    private String field = "abc";

    public String getField() {
      return field;
    }

    @DefaultFor("field")
    private String fieldDefault() {
      return "ABC";
    }
    
    @ModeFor("field")
    private EntryMode fieldEntryMode() {
      return EntryMode.VIEW;
    }
    
    @LabelFor("field")
    private String fieldLabel() {
      return "This is a field";
    }
    
    @TypeFor("field")
    private IType<?> fieldType() {
      return new StringType(10);
    }
    
    @Test
    public void fieldProviders() {
      IClassPlan<Klass> plan = new ClassPlan<Klass>(Klass.class);
      
      List<IRuntimeDefaultProvider> defaultProviders = plan.getRuntimeDefaultProviders();
      Assert.assertNotNull(defaultProviders);
      Assert.assertEquals(1, defaultProviders.size());
      
      List<IRuntimeModeProvider> modeProviders = plan.getRuntimeModeProviders();
      Assert.assertNotNull(modeProviders);
      Assert.assertEquals(1, modeProviders.size());
      
      List<IRuntimeLabelProvider> labelProviders = plan.getRuntimeLabelProviders();
      Assert.assertNotNull(labelProviders);
      Assert.assertEquals(1, labelProviders.size());
      
      List<IRuntimeTypeProvider> typeProviders = plan.getRuntimeTypeProviders();
      Assert.assertNotNull(typeProviders);
      Assert.assertEquals(1, typeProviders.size());
    }

  }
}
