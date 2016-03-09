package org.pennyledger.about;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;

public interface IAboutLineAction {

  public static void getAboutFile (Class<?> klass, IAboutLineAction action) {
    try (InputStream is = klass.getResourceAsStream("/resources/about.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
     String line = reader.readLine();
     while (line != null) {
       action.doLine(line);
       line = reader.readLine();
     }
   } catch (IOException ex) {
     throw new UncheckedIOException(ex);
   }
  }

  public void doLine (String line);
  
}
