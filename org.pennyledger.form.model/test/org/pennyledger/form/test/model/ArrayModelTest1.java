package org.pennyledger.form.test.model;

import org.pennyledger.form.FormField;
import org.pennyledger.form.Occurs;
import org.junit.Assert;
import org.junit.Test;
import org.pennyledger.form.model.FormModel;
import org.pennyledger.form.model.IArrayModel;
import org.pennyledger.form.model.IFieldModel;
import org.pennyledger.form.model.IFormModel;
import org.pennyledger.form.model.IObjectModel;
import org.pennyledger.form.plan.IArrayPlan;
import org.pennyledger.form.plan.IFieldPlan;
import org.pennyledger.form.plan.IGroupPlan;
import org.pennyledger.form.plan.IObjectPlan;
import org.pennyledger.form.plan.impl.ObjectPlanFactory;


public class ArrayModelTest1 {

  private static class TestClass {
    @Occurs(2)
    private Integer[] field1 = new Integer[] {100, 200};
  }
  
  
  private static class TestClass1 {
    @Occurs(3)
    private Integer[] field1 = new Integer[] {100, 200};
  }
  
  
  private static class TestClass2 {
    private Integer[] field1 = new Integer[] {100, 200};
  }
  
  
  private static class TestClass3 {
    @Occurs(3)
    @FormField(nullable=false)
    private Integer[] field1;
  }
  

  private static class TestClass4 {
    private Integer[] field1 = new Integer[0];
  }
  
 
  private EventCounter eventCounter = new EventCounter();
  
  
  @Test
  public void testArrayPlan () {
    IGroupPlan rootPlan = ObjectPlanFactory.buildGroupPlan(TestClass.class);
    IObjectPlan arrayPlanx = rootPlan.getMemberPlan("field1");
    Assert.assertEquals(true,  arrayPlanx instanceof IArrayPlan);
    IArrayPlan arrayPlan = (IArrayPlan)arrayPlanx;
    IObjectPlan elemPlanx = arrayPlan.getElementPlan();
    Assert.assertEquals(true,  elemPlanx instanceof IFieldPlan);
    Assert.assertEquals(2, arrayPlan.getMaxSize());
  }

  
  @Test
  public void testBasicForm () {
    TestClass1 instance = new TestClass1();
    IFormModel<TestClass1> formModel = new FormModel<TestClass1>(instance);
    IArrayModel arrayModel = formModel.selectObjectModel("//field1");
    Assert.assertEquals(3, arrayModel.getArraySize());
    IFieldModel fieldModel1 = (IFieldModel)arrayModel.getMember(0);
    Assert.assertEquals(new Integer(100), fieldModel1.getValue());
    Assert.assertEquals(3, instance.field1.length);
    Assert.assertEquals(new Integer(100), instance.field1[0]);
    Assert.assertEquals(new Integer(200), instance.field1[1]);
    
    formModel.addEntryModeListener(eventCounter);
    formModel.addFieldEventListener(eventCounter);

    Assert.assertEquals(1, eventCounter.modeChangedCount);
    
    // The following are for two array fields
    Assert.assertEquals(3, eventCounter.valueChangeCount);
    Assert.assertEquals(3, eventCounter.compareEqualityChangeCount);
    Assert.assertEquals(3, eventCounter.compareShowingChangeCount);
    Assert.assertEquals(3, eventCounter.errorClearedCount);
    
    // The following event is fired when a listener is added
    Assert.assertEquals(3, eventCounter.sourceChangeCount);
  }


  @Test
  public void testArrayFieldSelect () {
    TestClass1 instance = new TestClass1();
    IFormModel<TestClass1> formModel = new FormModel<TestClass1>(instance);    
    IArrayModel arrayModel = formModel.selectObjectModel("//field1");
    Assert.assertEquals(3, arrayModel.getArraySize());
    IFieldModel fieldModel1 = (IFieldModel)arrayModel.getMember(0);
    Assert.assertEquals("0", fieldModel1.getPathName());
    Assert.assertEquals("field1", fieldModel1.getParent().getPathName());
    System.out.println("----------------------------");
    formModel.dump();
    System.out.println("----------------------------");
    IObjectModel objectModel0 = formModel.selectObjectModel("//field1[1][1]");
    System.out.println(objectModel0.getClass());
    IFieldModel fieldModel0 = formModel.selectFieldModel("//field1/elem[1]");
    Assert.assertEquals(new Integer(200), fieldModel0.getValue());
  }

  @Test
  public void testAssignedValueOnly () {
    TestClass2 instance = new TestClass2();
    IFormModel<TestClass2> formModel = new FormModel<TestClass2>(instance);
    IArrayModel arrayModel = formModel.selectObjectModel("//field1");
    Assert.assertEquals(2, arrayModel.getArraySize());
    IFieldModel fieldModel1 = (IFieldModel)arrayModel.getMember(0);
    Assert.assertEquals(new Integer(100), fieldModel1.getValue());
    Assert.assertEquals(2, instance.field1.length);
    Assert.assertEquals(new Integer(100), instance.field1[0]);
    Assert.assertEquals(new Integer(200), instance.field1[1]);
  }

  
  @Test
  public void testAssignedOccursEqual () {
    TestClass instance = new TestClass();
    IFormModel<TestClass> formModel = new FormModel<TestClass>(instance);
    IArrayModel arrayModel = formModel.selectObjectModel("//field1");
    Assert.assertEquals(2, arrayModel.getArraySize());
    IFieldModel fieldModel1 = (IFieldModel)arrayModel.getMember(0);
    Assert.assertEquals(new Integer(100), fieldModel1.getValue());
    Assert.assertEquals(2, instance.field1.length);
    Assert.assertEquals(new Integer(100), instance.field1[0]);
    Assert.assertEquals(new Integer(200), instance.field1[1]);
  }

  
  @Test
  public void testOccursAnnotationOnly () {
    TestClass3 instance = new TestClass3();
    IFormModel<TestClass3> formModel = new FormModel<TestClass3>(instance);
    IArrayModel arrayModel = formModel.selectObjectModel("//field1");
    Assert.assertEquals(3, arrayModel.getArraySize());
    IFieldModel fieldModel1 = (IFieldModel)arrayModel.getMember(0);
    Assert.assertEquals(new Integer(0), fieldModel1.getValue());
    Assert.assertEquals(3, instance.field1.length);
    Assert.assertEquals(new Integer(0), instance.field1[0]);
  }

  
  @Test
  public void testSetFixedArrayValue () {
    TestClass3 instance = new TestClass3();
    IFormModel<TestClass3> formModel = new FormModel<TestClass3>(instance);
    IArrayModel arrayModel = formModel.selectObjectModel("//field1");
    
    Integer[] value0 = new Integer[0];
    arrayModel.setValue(value0);
    Assert.assertEquals(3, instance.field1.length);

    Integer[] value1 = new Integer[] {
        new Integer(100),
        new Integer(200),
    };
    arrayModel.setValue(value1);
    Assert.assertEquals(3, instance.field1.length);
    Assert.assertEquals(new Integer(200), instance.field1[1]);

    Integer[] value2 = new Integer[] {
        new Integer(100),
        new Integer(200),
        new Integer(300),
    };
    arrayModel.setValue(value2);
    Assert.assertEquals(3, instance.field1.length);
    Assert.assertEquals(new Integer(300), instance.field1[2]);

    arrayModel.setValue(null);
    Assert.assertEquals(3, instance.field1.length);
  }

  @Test
  public void testSetDynamicArrayValue () {
    TestClass4 instance = new TestClass4();
    IFormModel<TestClass4> formModel = new FormModel<TestClass4>(instance);
    IArrayModel arrayModel = formModel.selectObjectModel("//field1");
    
    Integer[] value0 = new Integer[0];
    arrayModel.setValue(value0);
    Assert.assertEquals(0, instance.field1.length);

    Integer[] value1 = new Integer[] {
        new Integer(100),
        new Integer(200),
    };
    arrayModel.setValue(value1);
    Assert.assertEquals(2, instance.field1.length);
    Assert.assertEquals(new Integer(200), instance.field1[1]);

    Integer[] value2 = new Integer[] {
        new Integer(100),
        new Integer(200),
        new Integer(300),
    };
    arrayModel.setValue(value2);
    Assert.assertEquals(3, instance.field1.length);
    Assert.assertEquals(new Integer(300), instance.field1[2]);
  }

  @Test
  public void testSetSize () {
    TestClass2 instance = new TestClass2();
    IFormModel<TestClass2> formModel = new FormModel<TestClass2>(instance);
    IArrayModel arrayModel = formModel.selectObjectModel("//field1");
    
    arrayModel.setArraySize(2);
    Assert.assertEquals(2, instance.field1.length);
    Assert.assertEquals(new Integer(100), instance.field1[0]);
    Assert.assertEquals(new Integer(200), instance.field1[1]);

    arrayModel.setArraySize(1);
    Assert.assertEquals(1, instance.field1.length);
    Assert.assertEquals(new Integer(100), instance.field1[0]);

    arrayModel.setArraySize(3);
    Assert.assertEquals(3, instance.field1.length);
    Assert.assertEquals(new Integer(100), instance.field1[0]);
    Assert.assertEquals(null, instance.field1[1]);
    Assert.assertEquals(null, instance.field1[2]);

    arrayModel.setArraySize(0);
    Assert.assertEquals(0, instance.field1.length);
  }

}
