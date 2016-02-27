package org.pennyledger.form.type.test;

import org.junit.Assert;
import org.junit.Test;
import org.pennyledger.form.NumberSign;
import org.pennyledger.form.type.builtin.DecimalType;
import org.pennyledger.math.Decimal;
import org.pennyledger.util.UserEntryException;

import orgpennyledger.form.type.IType;

public class DecimalTypeTest {

  @Test
  public void createTest1 () throws UserEntryException {
    IType<Decimal> type = new DecimalType(NumberSign.UNSIGNED, 4, 0);
    Decimal value = type.createFromString("1234");
    Assert.assertEquals(new Decimal("1234"), value);
  }
  
  @Test(expected=UserEntryException.class)
  public void createTest2 () throws UserEntryException {
    IType<Decimal> type = new DecimalType(NumberSign.UNSIGNED, 4, 0);
    type.createFromString("12345");
  }
  
  @Test
  public void createTest3 () throws UserEntryException {
    IType<Decimal> type = new DecimalType(NumberSign.UNSIGNED, 6, 2);
    Decimal value = type.createFromString("1234.56");
    Assert.assertEquals(new Decimal("1234.56"), value);
  }
  
  @Test(expected=UserEntryException.class)
  public void createTest4 () throws UserEntryException {
    IType<Decimal> type = new DecimalType(NumberSign.UNSIGNED, 6, 2);
    type.createFromString("12345.56");
  }
  
  @Test(expected=UserEntryException.class)
  public void createTest5 () throws UserEntryException {
    IType<Decimal> type = new DecimalType(NumberSign.UNSIGNED, 6, 2);
    type.createFromString("1234.567");
  }
  
  @Test
  public void createTest11 () throws UserEntryException {
    IType<Decimal> type = new DecimalType(NumberSign.SIGNED, 4, 0);
    Decimal value = type.createFromString("1234");
    Assert.assertEquals(new Decimal("1234"), value);
  }
  
  @Test(expected=UserEntryException.class)
  public void createTest12 () throws UserEntryException {
    IType<Decimal> type = new DecimalType(NumberSign.SIGNED, 4, 0);
    type.createFromString("-12345");
  }
  
  @Test
  public void createTest13 () throws UserEntryException {
    IType<Decimal> type = new DecimalType(NumberSign.SIGNED, 6, 2);
    Decimal value = type.createFromString("-1234.56");
    Assert.assertEquals(new Decimal("-1234.56"), value);
  }
  
  @Test(expected=UserEntryException.class)
  public void createTest14 () throws UserEntryException {
    IType<Decimal> type = new DecimalType(NumberSign.SIGNED, 6, 2);
    type.createFromString("-12345.56");
  }
  
  @Test(expected=UserEntryException.class)
  public void createTest15 () throws UserEntryException {
    IType<Decimal> type = new DecimalType(NumberSign.SIGNED, 6, 2);
    type.createFromString("-1234.567");
  }
  
  @Test
  public void createTest21 () throws UserEntryException {
    IType<Decimal> type = new DecimalType(NumberSign.SIGNED, 4, 0);
    type.validate(new Decimal("-1234"), false);
  }
  
  @Test(expected=UserEntryException.class)
  public void createTest22 () throws UserEntryException {
    IType<Decimal> type = new DecimalType(NumberSign.SIGNED, 4, 0);
    type.validate(new Decimal("-12345"), false);
  }
  
  @Test
  public void createTest23 () throws UserEntryException {
    IType<Decimal> type = new DecimalType(NumberSign.SIGNED, 6, 2);
    type.validate(new Decimal("-1234.56"), false);
  }
  
  @Test(expected=UserEntryException.class)
  public void createTest24 () throws UserEntryException {
    IType<Decimal> type = new DecimalType(NumberSign.SIGNED, 6, 2);
    type.createFromString("-12345.56");
  }
  
  @Test(expected=UserEntryException.class)
  public void createTest25 () throws UserEntryException {
    IType<Decimal> type = new DecimalType(NumberSign.SIGNED, 6, 2);
    type.validate(new Decimal("-1234.567"), false);
  }
  
}
