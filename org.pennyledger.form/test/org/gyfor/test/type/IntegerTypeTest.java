package org.gyfor.test.type;

import org.junit.Assert;
import org.junit.Test;
import org.pennyledger.form.type.IType;
import org.pennyledger.form.type.IntegerType;
import org.pennyledger.util.UserEntryException;


public class IntegerTypeTest {

  @Test
  public void createFromStringTest() throws UserEntryException {
    IType<Integer> type = new IntegerType();
    int i = type.createFromString("123", null, false, false);
    Assert.assertEquals(123, i);
  }

  @Test
  public void createFromStringTest2a() throws UserEntryException {
    IType<Integer> type = new IntegerType(-9999, 9999);
    try {
      type.createFromString("-", null, false, false);
      Assert.fail("Exception expected");
    } catch (UserEntryException ex) {
      System.out.println(ex);
      Assert.assertEquals(false, ex.isRequired());
      Assert.assertEquals(true, ex.isIncomplete());
    }
  }

  
  @Test
  public void createFromStringTest2() throws UserEntryException {
    IType<Integer> type = new IntegerType(0, Integer.MAX_VALUE);
    try {
      type.createFromString("-123", null, false, false);
      Assert.fail("Exception expected");
    } catch (UserEntryException ex) {
      Assert.assertEquals(true, ex.isError());
    }
  }

  
  @Test
  public void createFromStringTest3() throws UserEntryException {
    IType<Integer> type = new IntegerType();
    Integer i = type.createFromString("", null, true, false);
    Assert.assertEquals(null, i);
  }

  
  @Test
  public void createFromStringTest4() throws UserEntryException {
    IType<Integer> type = new IntegerType();
    try {
      type.createFromString("", null, false, false);
      Assert.fail("Exception expected");
    } catch (UserEntryException ex) {
      Assert.assertEquals(true, ex.isRequired());
    }
  }

}
