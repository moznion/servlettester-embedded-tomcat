package moznion.net.servlettester.embedded.tomcat;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import java.io.File;
import java.net.URI;

import javax.servlet.Servlet;

/**
 * Runner class for embedded tomcat.
 * 
 * @author moznion
 *
 */
public class TomcatServletRunner implements AutoCloseable {
  private final Tomcat tomcat;
  private final URI baseUri;
  private final String servletName;

  /**
   * Starts tomcat with servlet class name.
   * 
   * TCP port is automatically selected from among the free port. And servlet name will be used
   * servlet class name.
   * 
   * @param servletClassName
   * @throws Exception
   */
  public TomcatServletRunner(String servletClassName) throws Exception {
    this(servletClassName, null, null);
  }

  /**
   * Starts tomcat with servlet class name.
   * 
   * TCP port is automatically selected from among the free port.
   * 
   * @param servletClassName servlet class name
   * @param servletName name of servlet
   * @throws Exception
   */
  public TomcatServletRunner(String servletClassName, String servletName) throws Exception {
    this(servletClassName, servletName, null);
  }

  /**
   * Starts tomcat with servlet class name.
   * 
   * Servlet name will be used servlet class name.
   * 
   * @param servletClassName servlet class name
   * @param port TCP port number for tomcat
   * @throws Exception
   */
  public TomcatServletRunner(String servletClassName, Integer port) throws Exception {
    this(servletClassName, null, port);
  }

  /**
   * Starts tomcat with servlet class name.
   * 
   * @param servletClassName servlet class name
   * @param servletName name of servlet
   * @param port TCP port number for tomcat
   * @throws Exception
   */
  public TomcatServletRunner(String servletClassName, String servletName, Integer port)
      throws Exception {
    if (port == null) {
      port = TcpPortScanner.getEmptyPort();
    }

    if (servletName == null) {
      servletName = servletClassName;
    }
    this.servletName = servletName;

    tomcat = new Tomcat();
    tomcat.setPort(port);

    File projectRoot = new File(".");
    Context ctx = tomcat.addContext("/", projectRoot.getAbsolutePath());
    Tomcat.addServlet(ctx, this.servletName, servletClassName);
    ctx.addServletMapping("/*", this.servletName);

    tomcat.start();

    StringBuilder builder = new StringBuilder();
    baseUri = new URI(builder.append("http://127.0.0.1:").append(port).toString());
  }

  /**
   * Starts tomcat with servlet instance.
   * 
   * TCP port is automatically selected from among the free port. And servlet name will be used
   * servlet class name.
   * 
   * @param servlet instance of servlet
   * @throws Exception
   */
  public TomcatServletRunner(Servlet servlet) throws Exception {
    this(servlet, null, null);
  }

  /**
   * Starts tomcat with servlet instance.
   * 
   * TCP port is automatically selected from among the free port.
   * 
   * @param servlet instance of servlet
   * @param servletName name of servlet
   * @throws Exception
   */
  public TomcatServletRunner(Servlet servlet, String servletName) throws Exception {
    this(servlet, servletName, null);
  }

  /**
   * Starts tomcat with servlet instance.
   * 
   * Servlet name will be used servlet class name.
   * 
   * @param servlet instance of servlet
   * @param port TCP port number for tomcat
   * @throws Exception
   */
  public TomcatServletRunner(Servlet servlet, Integer port) throws Exception {
    this(servlet, null, port);
  }

  /**
   * Starts tomcat with servlet instance.
   * 
   * @param servlet instance of servlet
   * @param servletName name of servlet
   * @param port TCP port number for tomcat
   * @throws Exception
   */
  public TomcatServletRunner(Servlet servlet, String servletName, Integer port) throws Exception {
    if (port == null) {
      port = TcpPortScanner.getEmptyPort();
    }

    if (servletName == null) {
      servletName = servlet.getClass().getName();
    }
    this.servletName = servletName;

    tomcat = new Tomcat();
    tomcat.setPort(port);

    File projectRoot = new File(".");
    Context ctx = tomcat.addContext("/", projectRoot.getAbsolutePath());
    Tomcat.addServlet(ctx, this.servletName, servlet);
    ctx.addServletMapping("/*", this.servletName);

    tomcat.start();

    StringBuilder builder = new StringBuilder();
    baseUri = new URI(builder.append("http://127.0.0.1:").append(port).toString());
  }


  /**
   * Returns base URL.
   * 
   * @return Base URL
   */
  public URI getBaseUri() {
    return baseUri;
  }

  /**
   * Returns servlet name.
   * 
   * @return servlet name
   */
  public String getServletName() {
    return servletName;
  }

  /**
   * Stop and destroy launched tomcat.
   * 
   * @throws LifecycleException
   */
  public void destroy() throws LifecycleException {
    tomcat.stop();
    tomcat.destroy();
  }

  public void close() throws Exception {
    destroy();
  }
}
