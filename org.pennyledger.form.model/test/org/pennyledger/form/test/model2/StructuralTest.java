package org.pennyledger.form.test.model2;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.pennyledger.form.plan.IFieldPlan;
import org.pennyledger.form.plan.IGroupPlan;
import org.pennyledger.form.plan.IObjectPlan;
import org.pennyledger.form.plan.impl.ArrayPlan;
import org.pennyledger.form.plan.impl.ListPlan;
import org.pennyledger.form.plan.impl.ObjectPlanFactory;

public class StructuralTest {

  private IGroupPlan groupPlan;
  
  @Before
  public void setup () {
    groupPlan = ObjectPlanFactory.buildGroupPlan(StructuralRecord.class);
  }
  
  
  public static void main (String[] args) {
    IObjectPlan groupPlan = ObjectPlanFactory.buildGroupPlan(StructuralRecord.class);
    groupPlan.dump();
  }

  
  @Test
  public void testField0 () {
    IObjectPlan objectPlan = groupPlan.getMemberPlan("field0");
    Assert.assertNotNull(objectPlan);
    Assert.assertEquals(true, objectPlan instanceof IFieldPlan);
    IFieldPlan fieldPlan = (IFieldPlan)objectPlan;
    Assert.assertEquals("field0", fieldPlan.getPathName());
  }


  @Test
  public void testField1 () {
    IObjectPlan objectPlan = groupPlan.getMemberPlan("field1");
    Assert.assertNotNull(objectPlan);
    Assert.assertEquals(true, objectPlan instanceof IFieldPlan);
    IFieldPlan fieldPlan = (IFieldPlan)objectPlan;
    Assert.assertEquals("field1", fieldPlan.getPathName());
  }


  @Test
  public void testField2 () {
    IObjectPlan objectPlan = groupPlan.getMemberPlan("field2");
    Assert.assertNotNull(objectPlan);
    Assert.assertEquals(true, objectPlan instanceof ArrayPlan);
    ArrayPlan fieldPlan = (ArrayPlan)objectPlan;
    Assert.assertEquals("field2", fieldPlan.getPathName());
  }


  @Test
  public void testField3 () {
    IObjectPlan objectPlan0 = groupPlan.getMemberPlan("referenced");
    Assert.assertNotNull(objectPlan0);
    Assert.assertEquals(true, objectPlan0 instanceof IGroupPlan);
    IGroupPlan groupPlan2 = (IGroupPlan)objectPlan0;
    IObjectPlan objectPlan = groupPlan2.getMemberPlan("field3");
    Assert.assertNotNull(objectPlan);
    Assert.assertEquals(true, objectPlan instanceof IFieldPlan);
    IFieldPlan fieldPlan = (IFieldPlan)objectPlan;
    Assert.assertEquals("field3", fieldPlan.getPathName());
  }


  @Test
  public void testField4 () {
    IObjectPlan objectPlan0 = groupPlan.getMemberPlan("embedded");
    Assert.assertNotNull(objectPlan0);
    Assert.assertEquals(true, objectPlan0 instanceof IGroupPlan);
    IGroupPlan groupPlan2 = (IGroupPlan)objectPlan0;
    IObjectPlan objectPlan = groupPlan2.getMemberPlan("field4");
    Assert.assertNotNull(objectPlan);
    Assert.assertEquals(true, objectPlan instanceof IFieldPlan);
    IFieldPlan fieldPlan = (IFieldPlan)objectPlan;
    Assert.assertEquals("field4", fieldPlan.getPathName());
  }

  
  @Test
  public void testField5 () {
    IObjectPlan objectPlan0 = groupPlan.getMemberPlan("array");
    Assert.assertNotNull(objectPlan0);
    Assert.assertEquals(true, objectPlan0 instanceof ArrayPlan);
    ArrayPlan arrayPlan2 = (ArrayPlan)objectPlan0;
    IObjectPlan objectPlan = arrayPlan2.getElementPlan();
    Assert.assertNotNull(objectPlan);
    Assert.assertEquals(true, objectPlan instanceof IGroupPlan);
    IGroupPlan groupPlan = (IGroupPlan)objectPlan;
    IObjectPlan objectPlan2 = groupPlan.getMemberPlan("field5");
    Assert.assertNotNull(objectPlan2);
    Assert.assertEquals(true, objectPlan2 instanceof IFieldPlan);
    IFieldPlan fieldPlan = (IFieldPlan)objectPlan2;
    Assert.assertEquals("field5", fieldPlan.getPathName());
  }


  @Test
  public void testField6 () {
    IObjectPlan objectPlan0 = groupPlan.getMemberPlan("list");
    Assert.assertNotNull(objectPlan0);
    Assert.assertEquals(true, objectPlan0 instanceof ListPlan);
    ListPlan listPlan2 = (ListPlan)objectPlan0;
    IObjectPlan objectPlan = listPlan2.getElementPlan();
    Assert.assertNotNull(objectPlan);
    Assert.assertEquals(true, objectPlan instanceof IGroupPlan);
    IGroupPlan groupPlan = (IGroupPlan)objectPlan;
    IObjectPlan objectPlan2 = groupPlan.getMemberPlan("field6");
    Assert.assertNotNull(objectPlan2);
    Assert.assertEquals(true, objectPlan2 instanceof IFieldPlan);
    IFieldPlan fieldPlan = (IFieldPlan)objectPlan2;
    Assert.assertEquals("field6", fieldPlan.getPathName());
  }


}
