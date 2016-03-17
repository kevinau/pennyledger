package org.pennyledger.accounts.gl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.pennyledger.math.Decimal;

public class GLEntry {

  private final String reference;
  private final LocalDate date;
  private final String narrative;
  private final List<GLEntryDetail> entryDetails;
  
  
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

}
