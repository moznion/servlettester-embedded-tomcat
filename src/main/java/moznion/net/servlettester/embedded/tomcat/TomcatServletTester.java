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
  public interface URIConsumer {
    void accept(URI body) throws Exception;
  }

  public static void runServlet(String servletClass, URIConsumer body) throws Exception {
    try (TomcatServletRunner tomcatServletRunner =
        new TomcatServletRunner(servletClass, servletClass)) {
      body.accept(tomcatServletRunner.getBaseUri());
    }
  }

  public static void runServlet(Servlet servlet, URIConsumer body) throws Exception {
    try (TomcatServletRunner tomcatServletRunner =
        new TomcatServletRunner(servlet, servlet.getClass().toString())) {
      body.accept(tomcatServletRunner.getBaseUri());
    }
  }

  public static void runServlet(ServletCallback callback, URIConsumer body) throws Exception {
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
