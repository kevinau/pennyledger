package org.pennyledger.docimport.test;

import java.io.Serializable;
import java.time.LocalDate;

import org.pennyledger.docstore.parser.ITrainingData;
import org.pennyledger.docstore.parser.SegmentType;
import org.pennyledger.math.Decimal;

public class DividendStatement implements Serializable, ITrainingData {

  private static final long serialVersionUID = 1L;
  
  private LocalDate declaredDate;
  private LocalDate paymentDate;
  private Decimal dividendAmount;
  private Decimal frankingCredit;
  
  public DividendStatement (LocalDate declaredDate, LocalDate paymentDate, Decimal dividendAmount, Decimal frankingCredit) {
    this.declaredDate = declaredDate;
    this.paymentDate = paymentDate;
    this.dividendAmount = dividendAmount;
    this.frankingCredit = frankingCredit;
  }
  
  
  @Override 
  public String[] getTargetValues (SegmentType type) {
    switch (type) {
    case DATE :
      return new String[] {
          "?DeclaredDate",
          "?PaymentDate",
      };
    case CURRENCY :
      return new String[] {
          "¤DividendAmount",
          "¤rankingCredit",
      };
    default :
      return null;
    }
  }
  
  
  @Override 
  public String resolveValue (SegmentType type, Object value) {
    switch (type) {
    case DATE :
      LocalDate d = (LocalDate)value;
      if (d.equals(declaredDate)) {
        return "?DeclaredDate";
      } else if (d.equals(paymentDate)) {
        return "?PaymentDate";
      } else {
        return null;
      }
    case CURRENCY :
      Decimal m = (Decimal)value;
      if (m.equals(dividendAmount)) {
        return "#DividendAmount";
      } else if (m.equals(frankingCredit)) {
        return "#FrankingCredit";
      } else {
        return null;
      }
    default :
      return null;
    }
  }
}
