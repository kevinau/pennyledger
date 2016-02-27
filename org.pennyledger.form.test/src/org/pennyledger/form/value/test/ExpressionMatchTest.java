package org.pennyledger.form.value.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpressionMatchTest implements Iterator<String> {

  private static final Pattern pathPattern = Pattern.compile("^(\\*|([a-zA-Z]\\w*)|\\[(\\d+)\\])");
  private static final Pattern pathPattern1 = Pattern.compile("(/\\*|/([a-zA-Z]\\w*)|//|/|\\[(\\d+)\\])");

  private final List<String> segments;
  
  public ExpressionMatchTest (String path) {
    this.segments = new ArrayList<>();
    Matcher matcher = pathPattern.matcher(path);
    boolean found = matcher.find();
    if (found) {
      String s = matcher.group(1);
      if (s.equals("*")) {
        segments.add(s);
      } else {
        s = matcher.group(2);
        if (s != null) {
          segments.add(s);
        } else {
          s = matcher.group(3);
          segments.add(s);
        }
      }
      Matcher matcher1 = pathPattern1.matcher(path);
      found = matcher1.find(matcher.end());
      while (found) {
        String sx = matcher.group(1);
 
        for (int i = 1; i < matcher1.groupCount() + 1; i++) {
          System.out.println(i + ":      " + matcher1.group(i));
        }
        found = matcher1.find();          
      }
    }
  }
  
  
  
  
  public static void main (String[] args) {
    String[] test = {
        "abc",
        "[123]",
        "*",
        "*//",
        "abc/def",
        "abc/def/ghi",
        "abc[123]",
        "abc/*[234]//",
    };
    
    for (String t : test) {
      System.out.println(t);
      Matcher matcher = pathPattern.matcher(t);
      boolean found = matcher.find();
      if (found) {
        for (int i = 1; i < matcher.groupCount() + 1; i++) {
          System.out.println(i + ":      " + matcher.group(i));
        }
        Matcher matcher1 = pathPattern1.matcher(t);
        found = matcher1.find(matcher.end());
        while (found) {
          for (int i = 1; i < matcher1.groupCount() + 1; i++) {
            System.out.println(i + ":      " + matcher1.group(i));
          }
          found = matcher1.find();          
        }
      }
      System.out.println();
    }
    
  }




  @Override
  public boolean hasNext() {
    // TODO Auto-generated method stub
    return false;
  }




  @Override
  public String next() {
    // TODO Auto-generated method stub
    return null;
  }
}
