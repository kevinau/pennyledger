package org.gyfor.report.page;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public class FontEncoding {

  private static Map<String, Integer> encoding;
  
  public int getCode (String name) {
    if (encoding == null) {
      try {
        encoding = new HashMap<String, Integer>(1024);
        Reader glyphReader = new InputStreamReader(getClass().getResourceAsStream("glyphlist.txt"));
        BufferedReader reader = new BufferedReader(glyphReader);
        String line = reader.readLine();
        while (line != null) {
          int n = line.indexOf(';');
          String nx = line.substring(0, n);
          int sx = Integer.parseInt(line.substring(n + 1), 16);
          encoding.put(nx, sx);
          line = reader.readLine();
        }
        reader.close();
      } catch (NumberFormatException e) {
        throw new RuntimeException(e);
      } catch (FileNotFoundException e) {
        throw new RuntimeException(e);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    Integer s = encoding.get(name);
    if (s == null) {
      return -1;
    } else {
      return s;
    }
  }
}
