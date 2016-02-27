package org.pennyledger.form.type.test;

import org.j2form.type.IType;
import org.j2form.type.builtin.IntegerType;
import org.junit.Assert;
import org.junit.Test;
import org.pennyledger.util.UserEntryException;


public class IntegerTypeTest {

  @Test
  public void createFromStringTest() throws UserEntryException {
    IType<Integer> type = new IntegerType();
    int i = type.createFromString("123");
    Assert.assertEquals(123, i);
  }

  @Test
  public void createFromStringTest2a() throws UserEntryException {
    IType<Integer> type = new IntegerType(-9999, 9999);
    try {
      type.createFromString("-");
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
      type.createFromString("-123");
      Assert.fail("Exception expected");
    } catch (UserEntryException ex) {
      Assert.assertEquals(true, ex.isError());
    }
  }

  
  @Test
  public void createFromStringTest3() throws UserEntryException {
    IType<Integer> type = new IntegerType();
    try {
      type.createFromString("");
      Assert.fail("Exception expected");
    } catch (UserEntryException ex) {
      Assert.assertEquals(true, ex.isRequired());
    }
  }

}
