package org.pennyledger.entitydb.impl;

import java.sql.Connection;
import java.time.LocalDate;

import org.gyfor.report.IReportDetail;
import org.gyfor.report.IReportGrouping;
import org.gyfor.report.PaperSize;
import org.gyfor.report.ReportGrouping;
import org.pennyledger.db.SpecifiedDatabase;
import org.pennyledger.math.Decimal;
import org.pennyledger.object.EntityPlanFactory;
import org.pennyledger.object.plan.IEntityPlan;
import org.pennyledger.sql.dialect.IDialect;
import org.pennyledger.sql.dialect.derby.DerbyEmbeddedDialect;

public class TestStream3 {

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
  }
  
  private static String sql = "SELECT id,version,eventDate,distributionNo,companyYear,"
      + "accountingYearEnd,dividendType,dividendPerShare,shares,dividendPayment,taxCredit,"
      + "totalDividend,name,address,town,postcode,shareholderCode "
      + " FROM DividendVoucher "
      + "ORDER BY shareholderCode, eventDate DESC";

  private static String fromSql = " FROM DividendVoucher "
      + "ORDER BY shareholderCode, eventDate DESC";

  public static void main (String[] args) throws Exception {
    IEntityPlan<DividendVoucher> entityPlan = EntityPlanFactory.getEntityPlan(DividendVoucher.class);
    
    IDialect dialect = new DerbyEmbeddedDialect();
    SpecifiedDatabase connFactory = new SpecifiedDatabase(dialect, "localhost", "myDB", "kevin", "kevin");
    Connection conn = connFactory.getConnection();

    ResultSetSupplier<Selection> supplier = new ResultSetSupplier<>(conn, Selection.class, entityPlan, fromSql);

//    IReportLevel headingLevel1 = supplier.getPageHeader("shareholderCode", (t, p) -> {
//      t.totalDividendPayment += p.getDividendPayment();
//      t.totalTaxCredit += p.getTaxCredit();
//    });
    
    IReportGrouping<Selection> group1 = new ReportGrouping<Selection>(p -> p.shareholderCode, 
        supplier.simpleHeading("shareholderCode", "name"),
        null);  //supplier.simpleDetail("eventDate", "distributionNo", "companyYear", "dividendType", "dividendPerShare", 
                  //            "shares", "dividendPayment", "taxCredit"));

    IReportGrouping<Selection> headings1 = supplier.simpleColumnHeadings();
    
    IReportDetail detail1 = supplier.simpleDetail("eventDate", "distributionNo", "companyYear",
        "dividendType", "dividendPerShare", "shares", "dividendPayment", "taxCredit");
    
    Report<Selection> report = new Report<Selection>(supplier);
    report.title("Shareholder dividends");
    report.group(group1);
    report.group(headings1);
    report.detail(detail1);
    report.generate("test3.pdf", PaperSize.A4);
    
    supplier.close();
  }
  
  
}
