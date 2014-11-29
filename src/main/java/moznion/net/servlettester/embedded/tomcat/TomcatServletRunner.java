package moznion.net.servlettester.embedded.tomcat;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import java.io.File;
import java.net.URI;

import javax.servlet.Servlet;
import java.util.Optional;

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
    this(of(servletClassName, servletName, port));
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
    this(of(servlet, servletName, port));
  }

  private TomcatServletRunner(Tomcat tomcat, URI baseUri, String servletName) {
    this.tomcat = tomcat;
    this.baseUri = baseUri;
    this.servletName = servletName;
  }

  private TomcatServletRunner(TomcatServletRunner runner) {
    this(runner.tomcat, runner.baseUri, runner.servletName);
  }

  private static TomcatServletRunner of(Servlet servlet, String inputServletName, Integer inputPort) throws Exception {
    String servletClassName = servlet.getClass().getName();
    String servletName = Optional.ofNullable(inputServletName).orElse(servletClassName);
    int port = Optional.ofNullable(inputPort).orElse(TcpPortScanner.getEmptyPort());
    URI uri = new URI("http://127.0.0.1:" + port);
    return new TomcatServletRunner(prepared(servlet, servletName, port), uri, servletName);
  }

  private static TomcatServletRunner of(String servletClassName, String inputServletName, Integer inputPort) throws Exception {
    String servletName = Optional.ofNullable(inputServletName).orElse(servletClassName);
    int port = Optional.ofNullable(inputPort).orElse(TcpPortScanner.getEmptyPort());
    URI uri = new URI("http://127.0.0.1:" + port);
    return new TomcatServletRunner(prepared(servletClassName, servletName, port), uri, servletName);
  }

  private static Tomcat prepared(Servlet servlet, String servletName, int port) throws LifecycleException {
    Tomcat tomcat = new Tomcat();
    tomcat.setPort(port);
    File projectRoot = new File(".");
    Context ctx = tomcat.addContext("/", projectRoot.getAbsolutePath());
    Tomcat.addServlet(ctx, servletName, servlet);
    ctx.addServletMapping("/*", servletName);

    tomcat.start();
    return tomcat;
  }

  private static Tomcat prepared(String servletClassName, String servletName, int port) throws LifecycleException {
    Tomcat tomcat = new Tomcat();
    tomcat.setPort(port);
    File projectRoot = new File(".");
    Context ctx = tomcat.addContext("/", projectRoot.getAbsolutePath());
    Tomcat.addServlet(ctx, servletName, servletClassName);
    ctx.addServletMapping("/*", servletName);

    tomcat.start();
    return tomcat;
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
