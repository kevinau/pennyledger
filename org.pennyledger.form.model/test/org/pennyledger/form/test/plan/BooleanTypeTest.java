package org.pennyledger.form.test.plan;

import org.pennyledger.form.FormField;
import org.pennyledger.form.TypeFor;
import org.pennyledger.form.type.BooleanType;
import org.pennyledger.form.type.IType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.pennyledger.form.plan.IGroupPlan;
import org.pennyledger.form.plan.impl.FieldPlan;
import org.pennyledger.form.plan.impl.ObjectPlanFactory;


public class BooleanTypeTest {

  @SuppressWarnings("unused")
  private static class TestClass {
    Boolean field0;
    
    @FormField (nullable=true)
    Boolean field1;
    
    @FormField (nullable=false)
    Boolean field2;
    
    static IType<?> field3TypeDefn = new BooleanType();
    
    @FormField (type = BooleanType.class)
    Boolean field3;
    
    static IType<?> field4Type = new BooleanType();
    
    Boolean field4;
    
    @TypeFor("field5")
    static IType<?> field5TypeDefn = new BooleanType();
    
    Boolean field5;

    boolean field6;
    
  }


  private IGroupPlan groupPlan;
  
  
  @Before
  public void setup () {
    groupPlan = ObjectPlanFactory.buildGroupPlan(TestClass.class);
  }

  
  public IType<?> getType (String fieldName) {
    FieldPlan fieldPlan = (FieldPlan)groupPlan.getMemberPlan(fieldName);
    IType<?> type = fieldPlan.getType();
    return type;
  }


  @Test
  public void checkSetup () {
    Assert.assertEquals (7, groupPlan.getMemberPlans().length);
  }
  
  
  @Test
  public void checkTypes () {
    for (int i = 0; i < 7; i++) {
      String name = "field" + i;
      IType<?> type = getType(name);
      Assert.assertTrue("Field " + i + " is BooleanType", type instanceof BooleanType);
    }
  }
  
  
  @Test
  public void checkNullable () {
    for (int i = 0; i < 7; i++) {
      String name = "field" + i;
      IType<?> type = getType(name);
      boolean primitive = type.isPrimitive();
      if (i == 6) {
        Assert.assertTrue("Field " + i + " is primitive", primitive);
      } else {
        Assert.assertTrue("Field " + i + " is NOT primitive", !primitive);
      }
    }
  }
  
  
  
}
