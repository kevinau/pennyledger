package org.pennyledger.entitydb.impl;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.time.LocalDate;

import org.pennyledger.db.SpecifiedDatabase;
import org.pennyledger.math.Decimal;
import org.pennyledger.object.EntityPlanFactory;
import org.pennyledger.object.plan.IEntityPlan;
import org.pennyledger.sql.dialect.IDialect;
import org.pennyledger.sql.dialect.derby.DerbyEmbeddedDialect;

public class TestStream4 {

  public static class Selection {
    String shareholderCode;
    String name;
    LocalDate eventDate;
    int distributionNo;
    int companyYear;
    String dividendType;
    Decimal dividendPerShare;
    int shares;
    Decimal dividendPayment;
    Decimal taxCredit;
    
    @Override
    public String toString() {
      return "Selection [shareholderCode=" + shareholderCode + ", name=" + name + ", eventDate=" + eventDate
          + ", distributionNo=" + distributionNo + ", companyYear=" + companyYear + ", dividendType=" + dividendType
          + ", dividendPerShare=" + dividendPerShare + ", shares=" + shares + ", dividendPayment=" + dividendPayment
          + ", taxCredit=" + taxCredit + "]";
    }
  }
  
//  private static String sql = "SELECT id,version,eventDate,distributionNo,companyYear,"
//      + "accountingYearEnd,dividendType,dividendPerShare,shares,dividendPayment,taxCredit,"
//      + "totalDividend,name,address,town,postcode,shareholderCode "
//      + " FROM DividendVoucher "
//      + "ORDER BY shareholderCode, eventDate";

  private static String fromSql = " FROM DividendVoucher "
      + "ORDER BY shareholderCode, eventDate";

  public static void main (String[] args) throws Exception {
    IDialect dialect = new DerbyEmbeddedDialect();
    SpecifiedDatabase connFactory = new SpecifiedDatabase(dialect, "localhost", "myDB", "kevin", "kevin");
    
    IEntityPlan<DividendVoucher> refPlan = EntityPlanFactory.getEntityPlan(DividendVoucher.class);
    
    Connection conn = connFactory.getConnection();
    try (ResultSetSupplier<Selection> supplier = new ResultSetSupplier<>(conn, Selection.class, refPlan, fromSql)) {
      Selection data = supplier.get();
      while (data != null) {
        System.out.println(data);
        data = supplier.get();
      }
    }
  }
}
