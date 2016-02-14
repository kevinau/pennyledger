package org.pennyledger.form.test.plan;

import javax.persistence.Column;
import javax.persistence.Embedded;

import org.pennyledger.form.FormField;
import org.pennyledger.form.TypeFor;
import org.pennyledger.form.type.IType;
import org.pennyledger.form.type.TextType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.pennyledger.form.plan.IGroupPlan;
import org.pennyledger.form.plan.impl.FieldPlan;
import org.pennyledger.form.plan.impl.ObjectPlanFactory;


@SuppressWarnings("unused")
public class StringTypeTest {

  private static class TestClassz {
    String field0;
    
    @FormField (length = 1)
    String field1;
    
    @Column (length = 2)
    String field2;
    
    static IType<?> field5Type = new TextType(5);
    
    String field5;
    
    static IType<?> field6Type() {
      return new TextType(6);
    }
    
    String field6;

    @TypeFor("field7")
    static IType<?> field7TypeDefn = new TextType(7);
    
    String field7;

    @TypeFor("field8")
    static IType<?> field8TypeDefn() {
      return new TextType(8);
    }
    
    String field8;
  }


  private static class TestClassz2 {
    @Embedded
    TestClassz inner;
  }

  
  private IGroupPlan groupPlan;
  
  
  @Before
  public void setup () {
    groupPlan = ObjectPlanFactory.buildGroupPlan(TestClassz.class);
  }

  
  public TextType getTextType (String fieldName) {
    FieldPlan fieldPlan = (FieldPlan)groupPlan.getMemberPlan(fieldName);
    IType<?> type = fieldPlan.getType();
    Assert.assertTrue(type instanceof TextType);
    TextType textType = (TextType)type;
    return textType;
  }


  @Test
  public void checkSetup () {
    Assert.assertEquals (7, groupPlan.getMemberPlans().length);
  }
  
  
  @Test
  public void noLengthSpecified () {
    TextType textType = getTextType("field0");
    Assert.assertEquals(255, textType.getMaxSize());
  }

  @Test
  public void formFieldLength () {
    TextType textType = getTextType("field1");
    Assert.assertEquals(1, textType.getMaxSize());
  }

  @Test
  public void columnLength () {
    TextType textType = getTextType("field2");
    Assert.assertEquals(2, textType.getMaxSize());
  }

  @Test
  public void conventionTypeFieldLength () {
    TextType textType = getTextType("field5");
    Assert.assertEquals(5, textType.getMaxSize());
  }

  @Test
  public void conventionTypeMethodLength () {
    TextType textType = getTextType("field6");
    Assert.assertEquals(6, textType.getMaxSize());
  }

  @Test
  public void typeForFieldLength () {
    TextType textType = getTextType("field7");
    // The text type is the default text type, which has a max size of 255.
    Assert.assertEquals(255, textType.getMaxSize());
  }

  @Test
  public void typeForMethodLength () {
    TextType textType = getTextType("field8");
    // The text type is the default text type, which has a max size of 255.
    Assert.assertEquals(255, textType.getMaxSize());
  }
  
}
