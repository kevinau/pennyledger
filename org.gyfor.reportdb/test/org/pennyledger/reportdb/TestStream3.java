package org.pennyledger.reportdb;

import java.sql.Connection;
import java.time.LocalDate;

import org.pennyledger.db.SpecifiedDatabase;
import org.pennyledger.math.Decimal;
import org.pennyledger.object.EntityPlanFactory;
import org.pennyledger.object.plan.IEntityPlan;
import org.pennyledger.report.IReportDetail;
import org.pennyledger.report.IReportGrouping;
import org.pennyledger.report.PaperSize;
import org.pennyledger.report.Report;
import org.pennyledger.reportdb.ResultSetSupplier;
import org.pennyledger.reportdb.TitleLevel;
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
  
//  private static String sql = "SELECT id,version,eventDate,distributionNo,companyYear,"
//      + "accountingYearEnd,dividendType,dividendPerShare,shares,dividendPayment,taxCredit,"
//      + "totalDividend,name,address,town,postcode,shareholderCode "
//      + " FROM DividendVoucher "
//      + "ORDER BY eventDate DESC, shareholderCode";

  private static String fromSql = " FROM DividendVoucher "
      + "ORDER BY shareholderCode, eventDate";

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
    
    IReportGrouping<Selection> title = new TitleLevel<>("Shareholder dividends");
    
    IReportGrouping<Selection> group1 = supplier.simpleGroup(p -> p.shareholderCode, "shareholderCode", "name", null, "dividendPayment", "taxCredit");
    
    IReportGrouping<Selection> headings1 = supplier.simpleColumnHeadings();
    
    IReportDetail detail1 = supplier.simpleDetail("eventDate", "distributionNo", "companyYear",
        "dividendType", "dividendPerShare", "shares", "dividendPayment", "taxCredit");
    
    Report<Selection> report = new Report<Selection>(supplier);
    report.group(title);
    report.group(group1);
    report.group(headings1);
    report.detail(detail1);
    report.generate("test3.pdf", PaperSize.A4);
    
    supplier.close();
  }
  
  
}
