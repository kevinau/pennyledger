package org.pennyledger.web.about;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/about2")
public class AboutServlet2 extends HttpServlet {

  private static final long serialVersionUID = 1L;

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

  PrintWriter writer = response.getWriter();
  writer.println("<html><body>Hello 2</body></html>");
  writer.flush();
}

}
