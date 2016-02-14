package org.pennyledger.form.test.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.pennyledger.form.model.ComparisonBasis;
import org.pennyledger.form.model.FieldEventAdapter;
import org.pennyledger.form.model.FormModel;
import org.pennyledger.form.model.IFieldModel;
import org.pennyledger.form.model.IFormModel;

public class ReferenceValueTest {

  @SuppressWarnings("unused")
  private static class TestClass {
    String field0;

    String field1 = "Field one";

  }

  private IFormModel<TestClass> formModel;
  private ReferenceChangeCountListener x;

  private static class ReferenceChangeCountListener extends FieldEventAdapter {

    private int equalCount = 0;
    private int notEqualCount = 0;

    @Override
    public void compareEqualityChange(IFieldModel model) {
      if (model.isComparedValueEqual()) {
        equalCount++;
      } else {
        notEqualCount++;
      }
    }
  }

  @Before
  public void setup() {
    formModel = new FormModel<TestClass>(TestClass.class);
  }

  @Test
  public void fieldReferenceChange() {
    IFieldModel fieldModel = formModel.selectFieldModel("/form/field0");
    x = new ReferenceChangeCountListener();
    Assert.assertEquals(0, x.equalCount);
    Assert.assertEquals(0, x.notEqualCount);
    fieldModel.addFieldEventListener(x);
    Assert.assertEquals(1, x.equalCount);
    Assert.assertEquals(0, x.notEqualCount);
    fieldModel.setValue("Field value");
    Assert.assertEquals(1, x.equalCount);
    Assert.assertEquals(1, x.notEqualCount);
    fieldModel.setDefaultValue("Field value");
    Assert.assertEquals(2, x.equalCount);
    Assert.assertEquals(1, x.notEqualCount);
    
    Assert.assertEquals(2, x.equalCount);
    Assert.assertEquals(1, x.notEqualCount);
    Assert.assertEquals(true, fieldModel.isComparedValueEqual());
    
    fieldModel.setReferenceValue (null);
    Assert.assertEquals(2, x.equalCount);
    Assert.assertEquals(1, x.notEqualCount);
    Assert.assertEquals(true, fieldModel.isComparedValueEqual());
    
    fieldModel.setReferenceValue ("Some value");
    Assert.assertEquals(2, x.equalCount);
    Assert.assertEquals(1, x.notEqualCount);
    Assert.assertEquals(true, fieldModel.isComparedValueEqual());
    
    fieldModel.setCompareBasis(ComparisonBasis.REFERENCE);
    Assert.assertEquals(2, x.equalCount);
    Assert.assertEquals(2, x.notEqualCount);
    Assert.assertEquals(false, fieldModel.isComparedValueEqual());

    fieldModel.setReferenceValue (null);
    Assert.assertEquals(2, x.equalCount);
    Assert.assertEquals(2, x.notEqualCount);
    Assert.assertEquals(false, fieldModel.isComparedValueEqual());

    fieldModel.setReferenceValue ("Field zero");
    Assert.assertEquals(2, x.equalCount);
    Assert.assertEquals(2, x.notEqualCount);
    Assert.assertEquals(false, fieldModel.isComparedValueEqual());
    
    fieldModel.setReferenceValue ("Field value");
    Assert.assertEquals(3, x.equalCount);
    Assert.assertEquals(2, x.notEqualCount);
    Assert.assertEquals(true, fieldModel.isComparedValueEqual());
    
}  

}
