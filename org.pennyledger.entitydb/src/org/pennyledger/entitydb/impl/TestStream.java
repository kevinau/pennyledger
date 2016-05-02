package org.pennyledger.entitydb.impl;

import java.sql.Connection;
import java.util.List;
import java.util.Spliterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.pennyledger.db.SpecifiedDatabase;
import org.pennyledger.entitydb.SelectObjectSpliterator;
import org.pennyledger.sql.dialect.IDialect;
import org.pennyledger.sql.dialect.derby.DerbyEmbeddedDialect;

public class TestStream {

  public static void main (String[] args) {
    IDialect dialect = new DerbyEmbeddedDialect();
    SpecifiedDatabase connFactory = new SpecifiedDatabase(dialect, "localhost", "myDB", "kevin", "kevin");
    
    Connection conn = connFactory.getConnection();
    String fromSQL = "FROM DividendVoucher ORDER BY shareholderCode, eventDate";
    Spliterator<DividendVoucher> spliterator = new SelectObjectSpliterator<DividendVoucher>(conn, DividendVoucher.class, fromSQL);
    Stream<DividendVoucher> stream = StreamSupport.stream(spliterator, false);
    
    List<DividendVoucher> results = stream.collect(Collectors.toList());
    for (DividendVoucher dv : results) {
      System.out.println(dv);
    }
  }
  
  
}
