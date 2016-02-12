package org.pennyledger.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AbstractPageServlet extends OSGiServlet {

  private static final long serialVersionUID = 1L;

  private final boolean embedLinks;
  
  protected AbstractPageServlet () {
    this(false);
  }
  
  
  protected AbstractPageServlet (boolean embedLinks) {
    this.embedLinks = embedLinks;
  }
  
  
  private String readAll (File file) throws IOException {
    int n = (int)file.length();
    char[] buffer = new char[n];
    BufferedReader reader = new BufferedReader(new FileReader(file));
    reader.read(buffer);
    reader.close();
    return new String(buffer);
  }

  
  protected abstract void doGet(HttpServletRequest request, HtmlPage webPage) throws IOException;
  
  private static final String NL = "\r\n";
  
  
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    HtmlPage webPage = new HtmlPage(response);
    doGet (request, webPage);
    if (webPage.isClosed()) {
      return;
    }
    
    PrintWriter writer = response.getWriter();
    // HTML 5 doctype
    writer.append("<!DOCTYPE html>");
    writer.append(NL);
    writer.append("<html>");
    writer.append(NL);
    writer.append("<head>");
    writer.append(NL);
    writer.append("<meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />");
    writer.append(NL);
    writer.append("<title>");
    writer.append(webPage.getTitle());
    writer.append("</title>");
    writer.append(NL);

    for (LinkReference linkRef : webPage.getLinkRefs()) {
      if (embedLinks) {
        String content = readAll(linkRef.getHref());
        writer.append("<style type='text/css'>");
        writer.append(NL);
        writer.append(content);
        writer.append("</style>");
        writer.append(NL);
      } else {
        linkRef.output(writer);
        writer.append(NL);
      }
    }
 
    for (String styleContent : webPage.getStyleContents()) {
      writer.append("<style type='text/css'>");
      writer.append(NL);
      writer.append(styleContent);
      writer.append("</style>");
      writer.append(NL);
    }
    writer.append("</head>");
    writer.append(NL);
    writer.append(NL);
    
    writer.append("<body>");
    writer.append(NL);
    writer.append(webPage.getBodyContent());
    writer.append("</body>");
    writer.append(NL);
    
    writer.append("</html>");
    writer.append(NL);
  }

}
