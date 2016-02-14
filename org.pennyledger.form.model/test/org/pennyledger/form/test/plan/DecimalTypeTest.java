package org.pennyledger.form.test.plan;

import javax.persistence.Column;

import org.pennyledger.form.FormField;
import org.pennyledger.form.NumberSign;
import org.pennyledger.form.TypeFor;
import org.pennyledger.form.type.DecimalType;
import org.pennyledger.form.type.IType;
import org.pennyledger.form.plan.IGroupPlan;
import org.pennyledger.form.plan.impl.FieldPlan;
import org.pennyledger.form.plan.impl.ObjectPlanFactory;
import org.pennyledger.math.Decimal;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class DecimalTypeTest {

  @SuppressWarnings("unused")
  private static class TestClassz {
    Decimal field0;
    
    @FormField (precision=1)
    Decimal field1;
    
    @FormField (sign=NumberSign.UNSIGNED, precision=1)
    Decimal field1a;
    
    @Column (precision=2)
    Decimal field2;
    
    static IType<?> field5Type = new DecimalType(5);
    
    Decimal field5;
    
    static IType<?> field6Type() {
      return new DecimalType(6);
    }
    
    Decimal field6;

    @TypeFor("field7")
    static IType<?> field7TypeDefn = new DecimalType(7);
    
    Decimal field7;

    @TypeFor("field8")
    static IType<?> field8TypeDefn() {
      return new DecimalType(6);
    }
    
    Decimal field8;
  }


  private IGroupPlan groupPlan;
  
  
  @Before
  public void setup () {
    groupPlan = ObjectPlanFactory.buildGroupPlan(TestClassz.class);
  }

  
  public DecimalType getDecimalType (String fieldName) {
    FieldPlan fieldPlan = (FieldPlan)groupPlan.getMemberPlan(fieldName);
    IType<?> type = fieldPlan.getType();
    Assert.assertTrue(type instanceof DecimalType);
    DecimalType decimalType = (DecimalType)type;
    return decimalType;
  }


  @Test
  public void checkSetup () {
    Assert.assertEquals (8, groupPlan.getMemberPlans().length);
  }
  
  
  @Test
  public void noLengthSpecified () {
    DecimalType decimalType = getDecimalType("field0");
    Assert.assertEquals(10, decimalType.getFieldSize());
  }

  @Test
  public void formFieldLength () {
    DecimalType decimalType = getDecimalType("field1");
    // This field length includes a sign
    Assert.assertEquals(1 + 1, decimalType.getFieldSize());

    DecimalType decimalType2 = getDecimalType("field1a");
    // This field length does not include a sign
    Assert.assertEquals(1, decimalType2.getFieldSize());
  }

  @Test
  public void columnLength () {
    DecimalType decimalType = getDecimalType("field2");
    // This field length includes a sign
    Assert.assertEquals(2 + 1, decimalType.getFieldSize());
  }

  @Test
  public void conventionTypeFieldLength () {
    DecimalType decimalType = getDecimalType("field5");
    Assert.assertEquals(5, decimalType.getFieldSize());
  }

  @Test
  public void conventionTypeMethodLength () {
    DecimalType decimalType = getDecimalType("field6");
    Assert.assertEquals(6, decimalType.getFieldSize());
  }

  @Test
  public void typeForFieldLength () {
    DecimalType decimalType = getDecimalType("field7");
    // The decimal type is the default decimal type, which has a field size of 10.
    Assert.assertEquals(10, decimalType.getFieldSize());
  }

  @Test
  public void typeForMethodLength () {
    DecimalType decimalType = getDecimalType("field8");
    // The decimal type is the default decimal type, which has a field size of 10.
    Assert.assertEquals(10, decimalType.getFieldSize());
  }
  
}
