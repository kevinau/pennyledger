package org.pennyledger.form.test.plan;

import javax.persistence.Column;

import org.pennyledger.form.FormField;
import org.pennyledger.form.TypeFor;
import org.pennyledger.form.type.IType;
import org.pennyledger.form.type.IntegerType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.pennyledger.form.plan.IGroupPlan;
import org.pennyledger.form.plan.impl.FieldPlan;
import org.pennyledger.form.plan.impl.ObjectPlanFactory;


public class IntegerTypeTest {

  @SuppressWarnings("unused")
  private static class TestClassz {
    Integer field0;
    
    @FormField (precision = 1)
    Integer field1;
    
    @Column (precision = 2)
    Integer field2;
    
    static IType<?> field5Type = new IntegerType(0, 99999);
    
    Integer field5;
    
    static IType<?> field6Type() {
      return new IntegerType(0, 999999);
    }
    
    Integer field6;

    @TypeFor("field7")
    static IType<?> field7TypeDefn = new IntegerType(0, 9999999);
    
    Integer field7;

    @TypeFor("field8")
    static IType<?> field8TypeDefn() {
      return new IntegerType(0, 999999);
    }
    
    Integer field8;
  }


  private IGroupPlan groupPlan;
  
  
  @Before
  public void setup () {
    groupPlan = ObjectPlanFactory.buildGroupPlan(TestClassz.class);
  }

  
  public IntegerType getIntegerType (String fieldName) {
    FieldPlan fieldPlan = (FieldPlan)groupPlan.getMemberPlan(fieldName);
    IType<?> type = fieldPlan.getType();
    Assert.assertTrue(type instanceof IntegerType);
    IntegerType IntegerType = (IntegerType)type;
    return IntegerType;
  }


  @Test
  public void checkSetup () {
    Assert.assertEquals (7, groupPlan.getMemberPlans().length);
  }
  
  
  @Test
  public void noLengthSpecified () {
    IntegerType integerType = getIntegerType("field0");
    Assert.assertEquals(10 + 1, integerType.getFieldSize());
  }

  @Test
  public void formFieldLength () {
    IntegerType integerType = getIntegerType("field1");
    Assert.assertEquals(1 + 1, integerType.getFieldSize());
  }

  @Test
  public void columnLength () {
    IntegerType integerType = getIntegerType("field2");
    Assert.assertEquals(2 + 1, integerType.getFieldSize());
  }

  @Test
  public void conventionTypeFieldLength () {
    IntegerType integerType = getIntegerType("field5");
    Assert.assertEquals(5, integerType.getFieldSize());
  }

  @Test
  public void conventionTypeMethodLength () {
    IntegerType integerType = getIntegerType("field6");
    Assert.assertEquals(6, integerType.getFieldSize());
  }

  @Test
  public void typeForFieldLength () {
    IntegerType integerType = getIntegerType("field7");
    // The integer type is the default integer type, which has a field size of 11.
    Assert.assertEquals(11, integerType.getFieldSize());
  }

  @Test
  public void typeForMethodLength () {
    IntegerType integerType = getIntegerType("field8");
    // The integer type is the default integer type, which has a field size of 11.
    Assert.assertEquals(11, integerType.getFieldSize());
  }
  
}
