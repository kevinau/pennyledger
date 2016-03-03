package org.pennyledger.form.value.test;

import org.junit.Test;
import org.pennyledger.form.path.parser.ParseException;
import org.pennyledger.form.path.parser.SimplePathParser;

public class SimplePathExpressionTest {

  @Test
  public void text () throws ParseException {
    String[] test = {
        "abc",
        "[123]",
        "*",
        "//",
        "abc/def",
        "abc//def",
        "abc/def/ghi",
        "abc[123]",
        "abc/*[234]//",
    };
    
    for (String t : test) {
      //System.out.println(":" + t);
      //IPathExpression expr = parser.parse(t);
      //expr.dump();
      //System.out.println();
      SimplePathParser parser = new SimplePathParser(t);
      parser.parse();
    }
    
  }

}
