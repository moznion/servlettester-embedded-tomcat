package moznion.net.servlettester.embedded.tomcat;

import java.io.IOException;
import java.net.URI;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TomcatServletTester {
  @FunctionalInterface
  public interface ServletCallback {
    void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
        IOException;
  }

  @FunctionalInterface
  public interface UriConsumer {
    void accept(URI body) throws Exception;
  }

  /**
   * Run servlet on tomcat with servlet class name and response body.
   * 
   * @param servletClass servlet class name
   * @param body response body
   * @throws Exception
   */
  public static void runServlet(String servletClass, UriConsumer body) throws Exception {
    try (TomcatServletRunner tomcatServletRunner =
        new TomcatServletRunner(servletClass, servletClass)) {
      body.accept(tomcatServletRunner.getBaseUri());
    }
  }

  /**
   * Run servlet on tomcat with servlet instance and response body.
   * 
   * @param servlet servlet instance
   * @param body response body
   * @throws Exception
   */
  public static void runServlet(Servlet servlet, UriConsumer body) throws Exception {
    try (TomcatServletRunner tomcatServletRunner =
        new TomcatServletRunner(servlet, servlet.getClass().toString())) {
      body.accept(tomcatServletRunner.getBaseUri());
    }
  }

  /**
   * Run servlet on tomcat with servlet callback and response body.
   * 
   * @param callback servlet callback
   * @param body response body
   * @throws Exception
   */
  public static void runServlet(ServletCallback callback, UriConsumer body) throws Exception {
    Servlet servlet = new HttpServlet() {
      private static final long serialVersionUID = 1L;

      @Override
      protected void service(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
        callback.service(req, resp);
      }
    };
    runServlet(servlet, body);
  }
}
