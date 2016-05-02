package org.pennyledger.entitydb.impl;

import org.pennyledger.math.Decimal;

public class GLEntryDetail {

  private final GLAccount account;
  
  private final Decimal amount;
  
  public GLEntryDetail (GLAccount account, Decimal amount) {
    this.account = account;
    this.amount = amount;
  }
  
  
  public GLAccount getAccount() {
    return account;
  }


  public Decimal getAmount() {
    return amount;
  }
  
}
