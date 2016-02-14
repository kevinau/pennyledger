package org.pennyledger.form.test.plan;

import org.pennyledger.form.Form;
import org.pennyledger.form.FormIcon;
import org.pennyledger.form.FormName;
import org.junit.Assert;
import org.junit.Test;
import org.pennyledger.form.plan.impl.FormPlan;


public class FormPlanTest {

  public static class TestForm0 {
  }

  @Form
  public static class TestForm1 {
  }

  @Form (name="TestForm2", icon="iconName.gif")
  public static class Test2 {
  }

  public static class Test3 {
    @FormName
    public static String shortNamex = "TestForm3";
    
    @FormName
    public static String longNamex = "Test title three";

    @FormIcon
    public static String iconNamex = "iconName.png";
  }

  public static class Test4 {
    @FormName
    public static String getFormNamex() {
      return "TestForm4";
    }
    
    @FormIcon
    public static String getIconx() {
      return "iconName.jpg";
    }
  }

  public static class Test5 {
    public static String shortName = "Test 5";
    
    public static String formIcon = "iconName.bmp";
  }

  @Test
  public void baseTest() {
    FormPlan plan = new FormPlan(TestForm0.class);
    Assert.assertEquals("Test form 0", plan.getFormName());
    Assert.assertEquals(null, plan.getIconName());
  }
  
  @Test
  public void baseTest2() {
    FormPlan plan = new FormPlan(TestForm1.class);
    Assert.assertEquals("Test form 1", plan.getFormName());
    Assert.assertEquals(null, plan.getIconName());    
  }
  
  @Test
  public void annotatedFormTest() {
    FormPlan plan = new FormPlan(Test2.class);
    Assert.assertEquals("TestForm2", plan.getFormName());
    Assert.assertEquals("iconName.gif", plan.getIconName());    
  }
  
  @Test
  public void annotatedFormFieldsTest() {
    FormPlan plan = new FormPlan(Test3.class);
    Assert.assertEquals("TestForm3", plan.getFormName());
    Assert.assertEquals("iconName.png", plan.getIconName());    
  }
  
  
  @Test
  public void annotatedFormMethodsTest() {
    FormPlan plan = new FormPlan(Test4.class);
    Assert.assertEquals("TestForm4", plan.getFormName());
    Assert.assertEquals("iconName.jpg", plan.getIconName());    
  }
  
  @Test
  public void byConventionTest() {
    FormPlan plan = new FormPlan(Test5.class);
    Assert.assertEquals("Test 5", plan.getFormName());
    Assert.assertEquals("iconName.bmp", plan.getIconName());
  }
  
}
