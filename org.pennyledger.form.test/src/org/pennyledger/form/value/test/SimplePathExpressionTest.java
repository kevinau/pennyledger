package org.pennyledger.form.value.test;

import org.junit.Test;
import org.pennyledger.form.path.IPathExpression;
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
    
    SimplePathParser parser = new SimplePathParser();
    
    for (String t : test) {
      //System.out.println(":" + t);
      //IPathExpression expr = parser.parse(t);
      //expr.dump();
      //System.out.println();
      parser.parse(t);
    }
    
  }

}
