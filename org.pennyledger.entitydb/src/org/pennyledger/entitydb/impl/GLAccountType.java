package org.pennyledger.entitydb.impl;

public enum GLAccountType {

  ASSET(false),
  CONTRA_ASSET(true),
  LIABILITY(true),
  CONTRA_LIABILITY(false),
  EQUITY(true),                 // owner's or stockholders' equity
  DRAWING_DIVIDEND(false),      // owner's drawing or dividends account
  INCOME(true),                 // aka REVENUE
  EXPENSE(false),
  GAINS(true),
  LOSSES(false);

  final boolean normallyCredit;
  
  private GLAccountType (boolean normallyCredit) {
    this.normallyCredit = normallyCredit;
  }
  
  
}
