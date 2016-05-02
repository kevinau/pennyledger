package org.pennyledger.entitydb.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.pennyledger.math.Decimal;

public class GLEntry {

  private String reference;
  
  private LocalDate date;
  
  private String narrative;
  
  private List<GLEntryDetail> entryDetails;
  
  public GLEntry () {
  }
  
  
  public GLEntry (String reference, LocalDate date, String narrative) {
    this(reference, date, narrative, new ArrayList<>());
  }
  
  
  public GLEntry (String reference, LocalDate date, String narrative, List<GLEntryDetail> entryDetails) {
    this.reference = reference;
    this.date = date;
    this.narrative = narrative;
    this.entryDetails = entryDetails;
  }
  
  
  public void addDetail (GLEntryDetail detail) {
    this.entryDetails.add(detail);
  }
  
  
  public void addDetail (GLAccount account, Decimal amount) {
    GLEntryDetail detail = new GLEntryDetail(account, amount);
    this.entryDetails.add(detail);
  }
  
  
  public void addBalancingDetail (GLAccount account) {
    Decimal sum = Decimal.ZERO;
    for (GLEntryDetail detail : getDetails()) {
      sum = sum.add(detail.getAmount());
    }
    GLEntryDetail detail = new GLEntryDetail(account, sum.negate());
    this.entryDetails.add(detail);
  }
  
  
  public String getReference() {
    return reference;
  }

  
  public LocalDate getDate() {
    return date;
  }


  public String getNarrative() {
    return narrative;
  }


  public List<GLEntryDetail> getDetails() {
    return entryDetails;
  }
  
  
  public Decimal getBalancingAmount() {
    Decimal sum = Decimal.ZERO;
    for (GLEntryDetail detail : getDetails()) {
      sum = sum.add(detail.getAmount());
    }
    return sum.negate();
  }


  @Override
  public String toString() {
    return "GLEntry [reference=" + reference + ", date=" + date + ", narrative=" + narrative
        + ", entryDetails=" + entryDetails + "]";
  }

}
