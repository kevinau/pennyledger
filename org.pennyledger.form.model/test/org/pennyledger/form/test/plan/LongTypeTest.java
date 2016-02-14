package org.pennyledger.form.test.plan;

import javax.persistence.Column;

import org.pennyledger.form.FormField;
import org.pennyledger.form.TypeFor;
import org.pennyledger.form.type.IType;
import org.pennyledger.form.type.LongType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.pennyledger.form.plan.IGroupPlan;
import org.pennyledger.form.plan.impl.FieldPlan;
import org.pennyledger.form.plan.impl.ObjectPlanFactory;


@SuppressWarnings("unused")
public class LongTypeTest {

  private static class TestClassz {
    Long field0;
    
    @FormField (precision = 1)
    Long field1;
    
    @Column (precision = 2)
    Long field2;
    
    static IType<?> field5Type = new LongType(0, 99999);
    
    Long field5;
    
    static IType<?> field6Type() {
      return new LongType(0, 999999);
    }
    
    Long field6;

    @TypeFor("field7")
    static IType<?> field7TypeDefn = new LongType(0, 9999999);
    
    Long field7;

    @TypeFor("field8")
    static IType<?> field8TypeDefn() {
      return new LongType(0, 99999999);
    }
    
    Long field8;
  }


  private IGroupPlan groupPlan;
  
  
  @Before
  public void setup () {
    groupPlan = ObjectPlanFactory.buildGroupPlan(TestClassz.class);
  }

  
  public LongType getLongType (String fieldName) {
    FieldPlan fieldPlan = (FieldPlan)groupPlan.getMemberPlan(fieldName);
    IType<?> type = fieldPlan.getType();
    Assert.assertTrue(type instanceof LongType);
    LongType LongType = (LongType)type;
    return LongType;
  }


  @Test
  public void checkSetup () {
    Assert.assertEquals (7, groupPlan.getMemberPlans().length);
  }
  
  
  @Test
  public void noLengthSpecified () {
    LongType longType = getLongType("field0");
    Assert.assertEquals(18 + 1, longType.getFieldSize());
  }

  @Test
  public void formFieldLength () {
    LongType longType = getLongType("field1");
    Assert.assertEquals(1 + 1, longType.getFieldSize());
  }

  @Test
  public void columnLength () {
    LongType longType = getLongType("field2");
    Assert.assertEquals(2 + 1, longType.getFieldSize());
  }

  @Test
  public void conventionTypeFieldLength () {
    LongType longType = getLongType("field5");
    Assert.assertEquals(5, longType.getFieldSize());
  }

  @Test
  public void conventionTypeMethodLength () {
    LongType longType = getLongType("field6");
    Assert.assertEquals(6, longType.getFieldSize());
  }

  @Test
  public void typeForFieldLength () {
    LongType longType = getLongType("field7");
    // The long type is the default long type, which has a field size of 19.
    Assert.assertEquals(19, longType.getFieldSize());
  }

  @Test
  public void typeForMethodLength () {
    LongType longType = getLongType("field8");
    // The long type is the default long type, which has a field size of 19.
    Assert.assertEquals(19, longType.getFieldSize());
  }
  
}
