package moznion.net.servlettester.embedded.tomcat;

import java.io.IOException;
import java.net.URI;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Tester for servlet on embedded tomcat
 * 
 * @author moznion
 *
 */
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
   * TCP port is automatically selected from among the free port. And servlet name will be used
   * servlet class name.
   * 
   * @param servletClass servlet class name
   * @param body response body
   * @throws Exception
   */
  public static void runServlet(String servletClass, UriConsumer body) throws Exception {
    try (TomcatServletRunner tomcatServletRunner = new TomcatServletRunner(servletClass)) {
      body.accept(tomcatServletRunner.getBaseUri());
    }
  }

  /**
   * Run servlet on tomcat with servlet class name and response body.
   * 
   * Servlet name will be used servlet class name.
   * 
   * @param servletClass servlet class name
   * @param body response body
   * @param port TCP port number for tomcat
   * @throws Exception
   */
  public static void runServlet(String servletClass, UriConsumer body, int port) throws Exception {
    try (TomcatServletRunner tomcatServletRunner = new TomcatServletRunner(servletClass, port)) {
      body.accept(tomcatServletRunner.getBaseUri());
    }
  }

  /**
   * Run servlet on tomcat with servlet class name and response body.
   * 
   * TCP port is automatically selected from among the free port.
   * 
   * @param servletClass servlet class name
   * @param body response body
   * @param servletName name of servlet
   * @throws Exception
   */
  public static void runServlet(String servletClass, UriConsumer body, String servletName)
      throws Exception {
    try (TomcatServletRunner tomcatServletRunner =
        new TomcatServletRunner(servletClass, servletName)) {
      body.accept(tomcatServletRunner.getBaseUri());
    }
  }

  /**
   * Run servlet on tomcat with servlet class name and response body.
   * 
   * @param servletClass servlet class name
   * @param body response body
   * @param servletName name of servlet
   * @param port TCP port number for tomcat
   * @throws Exception
   */
  public static void runServlet(String servletClass, UriConsumer body, String servletName, int port)
      throws Exception {
    try (TomcatServletRunner tomcatServletRunner =
        new TomcatServletRunner(servletClass, servletName, port)) {
      body.accept(tomcatServletRunner.getBaseUri());
    }
  }

  /**
   * Run servlet on tomcat with servlet instance and response body.
   * 
   * TCP port is automatically selected from among the free port. And servlet name will be used
   * servlet class name.
   * 
   * @param servlet servlet instance
   * @param body response body
   * @throws Exception
   */
  public static void runServlet(Servlet servlet, UriConsumer body) throws Exception {
    try (TomcatServletRunner tomcatServletRunner = new TomcatServletRunner(servlet)) {
      body.accept(tomcatServletRunner.getBaseUri());
    }
  }

  /**
   * Run servlet on tomcat with servlet instance and response body.
   * 
   * Servlet name will be used servlet class name
   * 
   * @param servlet servlet instance
   * @param body response body
   * @param port TCP port number for tomcat
   * @throws Exception
   */
  public static void runServlet(Servlet servlet, UriConsumer body, int port) throws Exception {
    try (TomcatServletRunner tomcatServletRunner = new TomcatServletRunner(servlet, port)) {
      body.accept(tomcatServletRunner.getBaseUri());
    }
  }

  /**
   * Run servlet on tomcat with servlet instance and response body.
   * 
   * TCP port is automatically selected from among the free port
   * 
   * @param servlet servlet instance
   * @param body response body
   * @param servletName name of servlet
   * @throws Exception
   */
  public static void runServlet(Servlet servlet, UriConsumer body, String servletName)
      throws Exception {
    try (TomcatServletRunner tomcatServletRunner = new TomcatServletRunner(servlet, servletName)) {
      body.accept(tomcatServletRunner.getBaseUri());
    }
  }

  /**
   * Run servlet on tomcat with servlet instance and response body.
   * 
   * @param servlet servlet instance
   * @param body response body
   * @param servletName name of servlet
   * @param port TCP port number for tomcat
   * @throws Exception
   */
  public static void runServlet(Servlet servlet, UriConsumer body, String servletName, int port)
      throws Exception {
    try (TomcatServletRunner tomcatServletRunner =
        new TomcatServletRunner(servlet, servletName, port)) {
      body.accept(tomcatServletRunner.getBaseUri());
    }
  }

  /**
   * Run servlet on tomcat with servlet callback and response body.
   * 
   * TCP port is automatically selected from among the free port. And servlet name will be used
   * servlet class name.
   * 
   * @param callback servlet callback
   * @param body response body
   * @throws Exception
   */
  public static void runServlet(ServletCallback callback, UriConsumer body) throws Exception {
    Servlet servlet = getServletByCallback(callback);
    runServlet(servlet, body);
  }

  /**
   * Run servlet on tomcat with servlet callback and response body.
   * 
   * Servlet name will be used servlet class name.
   * 
   * @param callback servlet callback
   * @param body response body
   * @param port TCP port number for tomcat
   * @throws Exception
   */
  public static void runServlet(ServletCallback callback, UriConsumer body, int port)
      throws Exception {
    Servlet servlet = getServletByCallback(callback);
    runServlet(servlet, body, port);
  }

  /**
   * Run servlet on tomcat with servlet callback and response body.
   * 
   * TCP port is automatically selected from among the free port.
   * 
   * @param callback servlet callback
   * @param body response body
   * @param servletName name of servlet
   * @throws Exception
   */
  public static void runServlet(ServletCallback callback, UriConsumer body, String servletName)
      throws Exception {
    Servlet servlet = getServletByCallback(callback);
    runServlet(servlet, body, servletName);
  }

  /**
   * Run servlet on tomcat with servlet callback and response body.
   * 
   * @param callback servlet callback
   * @param body response body
   * @param servletName name of servlet
   * @param port TCP port number for tomcat
   * @throws Exception
   */
  public static void runServlet(ServletCallback callback, UriConsumer body, String servletName,
      int port) throws Exception {
    Servlet servlet = getServletByCallback(callback);
    runServlet(servlet, body, servletName, port);
  }

  private static Servlet getServletByCallback(ServletCallback callback) {
    Servlet servlet = new HttpServlet() {
      private static final long serialVersionUID = 1L;

      @Override
      protected void service(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
        callback.service(req, resp);
      }
    };
    return servlet;
  }
}
