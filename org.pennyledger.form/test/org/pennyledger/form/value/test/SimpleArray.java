package org.pennyledger.form.value.test;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.pennyledger.form.factory.Form;
import org.pennyledger.form.value.IFieldModel;
import org.pennyledger.form.value.IForm;
import org.pennyledger.form.value.IObjectModel;

public class SimpleArray {

  private static class Simple {
    @SuppressWarnings("unused")
    private String[] field1 = null;
  }

  private IForm<Simple> form;
  
  @Before
  public void setup() {
    form = new Form<>(Simple.class);
    Simple value0 = new Simple();
    form.setValue(value0);
//    
//    
//    String[] x = {
//        "aaa",
//        "bbb",
//        "ccc",
//    };
//    
//    for (int i = 0; i < 10; i++) {
//      
//    }
//    value.field1 = "abc";
//    value.field2 = "def";
//    form.setValue(value);
  }
  
  @Test
  public void simpleClassTest() {
    List<IFieldModel> fieldModels = form.getFieldModels("field1//");
    Assert.assertEquals(0, fieldModels.size());
    
    Simple value3 = new Simple();
    value3.field1 = new String[] {
        "aaa",
        "bbb",
        "ccc",
    };
    form.setValue(value3);
    
    fieldModels = form.getFieldModels("field1//");
    Assert.assertEquals(3, fieldModels.size());
    
    IFieldModel model1 = form.getFieldModel("field1[1]");
    Assert.assertEquals("aaa",  model1.getValue());
    
    IFieldModel model2 = form.getFieldModel("field1[2]");
    Assert.assertEquals("bbb",  model2.getValue());
    
    IFieldModel model3 = form.getFieldModel("field1[3]");
    Assert.assertEquals("ccc",  model3.getValue());
    
    Simple value5 = new Simple();
    value5.field1 = new String[] {
        "aaa",
        "bbb",
        "ccc",
        "ddd",
        "eee",
    };
    form.setValue(value5);

    fieldModels = form.getFieldModels("field1//");
    Assert.assertEquals(5, fieldModels.size());
    
    model1 = form.getFieldModel("field1[1]");
    Assert.assertEquals("aaa",  model1.getValue());
    
    IFieldModel model5 = form.getFieldModel("field1[5]");
    Assert.assertEquals("eee",  model5.getValue());
    
}

}
