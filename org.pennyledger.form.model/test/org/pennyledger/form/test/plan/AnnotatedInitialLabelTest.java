package org.pennyledger.form.test.plan;

import org.pennyledger.form.FormField;
import org.pennyledger.form.LabelFor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.pennyledger.form.plan.IGroupPlan;
import org.pennyledger.form.plan.IObjectPlan;
import org.pennyledger.form.plan.impl.ObjectPlanFactory;


public class AnnotatedInitialLabelTest {

  @SuppressWarnings("unused")
  private static class TestClass1 {
    String field0;
    
    @FormField (label = "Field one")
    String field1;
    
    static String field2Label = "Field two";
    
    String field2;
    
    static String field3Label() {
      return "Field three";
    }
    
    String field3;

    @LabelFor("field4")
    static String field4LabelDefn = "Field four";
    
    String field4;

    @LabelFor("field5")
    static String field5LabelDefn() {
      return "Field five";
    }
    
    String field5;
    
    @LabelFor("field6")
    String field6LabelDefn() {
      return "Field six";
    }
    
    String field6;
  }


  private IGroupPlan groupPlan;
  
  
  @Before
  public void setup () {
    groupPlan = ObjectPlanFactory.buildGroupPlan(TestClass1.class);
  }

  
  public String getFieldLabel (String fieldName) {
    IObjectPlan fieldPlan = groupPlan.getMemberPlan(fieldName);
    String label = fieldPlan.getStaticLabel();
    return label;
  }


  @Test
  public void checkSetup () {
    Assert.assertEquals (7, groupPlan.getMemberPlans().length);
  }
  
  
  @Test
  public void noLabelSpecified () {
    String label = getFieldLabel("field0");
    Assert.assertEquals("Field 0", label);
  }

  @Test
  public void formFieldLabel () {
    String label = getFieldLabel("field1");
    Assert.assertEquals("Field one", label);
  }

  @Test
  public void conventionLabelFieldLabel () {
    String label = getFieldLabel("field2");
    Assert.assertEquals("Field two", label);
  }

  @Test
  public void conventionLabelMethodLabel () {
    String label = getFieldLabel("field3");
    Assert.assertEquals("Field three", label);
  }

  @Test
  public void LabelForFieldLabel () {
    String label = getFieldLabel("field4");
    // The label here is the default label.  The real label is provided by RuntimeLabelProvider
    // on form startup.
    Assert.assertEquals("Field 4", label);
  }

  @Test
  public void LabelForStaticMethodLabel () {
    String label = getFieldLabel("field5");
    // The label here is the default label.  The real label is provided by RuntimeLabelProvider
    // on form startup.
    Assert.assertEquals("Field 5", label);
  }
  
  @Test
  public void LabelForRuntimeMethodLabel () {
    String label = getFieldLabel("field6");
    Assert.assertEquals("Field 6", label);
  }
  
}
