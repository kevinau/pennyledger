package org.pennyledger.docstore.segment;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.pennyledger.docstore.SegmentType;
import org.pennyledger.docstore.parser.ISegmentMatchResult;
import org.pennyledger.docstore.parser.ISegmentMatcher;
import org.pennyledger.math.Decimal;

class CentsPenceMatcher implements ISegmentMatcher {

  private final static Pattern centsPence = Pattern.compile("([1-9]\\d*)\\s+([Cc]ents|[Pp]ence)");

  @Override
  public ISegmentMatchResult find(String input, int start, int end) {
    Matcher matcher = centsPence.matcher(input);
    matcher.region(start, end);
    
    if (matcher.find()) {
      String nn = matcher.group(1);
      Decimal value = new Decimal(nn).divide(100);
      return new SegmentMatchResult(matcher, SegmentType.CURRENCY, value);
    } else {
      return null;
    }
  }

}
