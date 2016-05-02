package org.pennyledger.entitydb.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.gyfor.report.PaperSize;
import org.pennyledger.db.SpecifiedDatabase;
import org.pennyledger.entitydb.ResultSetSupplier;
import org.pennyledger.sql.dialect.IDialect;
import org.pennyledger.sql.dialect.derby.DerbyEmbeddedDialect;

public class TestStream2 {

  private static String sql = "SELECT id,version,eventDate,distributionNo,companyYear,"
      + "accountingYearEnd,dividendType,dividendPerShare,shares,dividendPayment,taxCredit,"
      + "totalDividend,name,address,town,postcode,shareholderCode "
      + " FROM DividendVoucher "
      + "ORDER BY shareholderCode, eventDate";

  public static void main (String[] args) throws Exception {
    IDialect dialect = new DerbyEmbeddedDialect();
    SpecifiedDatabase connFactory = new SpecifiedDatabase(dialect, "localhost", "myDB", "kevin", "kevin");
    
    Connection conn = connFactory.getConnection();
    PreparedStatement ps = conn.prepareStatement(sql);
    ResultSet rs = ps.executeQuery();
    ResultSetSupplier<DividendVoucher> supplier = new ResultSetSupplier<>(DividendVoucher.class, rs);

    StreamReport sreport = new StreamReport("Ace Accounting");
    sreport.generate(supplier, "test.pdf", PaperSize.A4);

    supplier.close();
  }
  
  
}
