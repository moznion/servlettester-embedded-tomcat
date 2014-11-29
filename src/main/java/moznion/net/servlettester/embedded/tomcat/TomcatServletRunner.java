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

  /**
   * Starts tomcat with servlet class name.
   * 
   * @param servletClass servlet class name
   * @param servletName name of servlet
   * @throws Exception
   */
  public TomcatServletRunner(String servletClass, String servletName) throws Exception {
    tomcat = new Tomcat();

    int port = TcpPortScanner.getEmptyPort();
    tomcat.setPort(port);

    File projectRoot = new File(".");
    Context ctx = tomcat.addContext("/", projectRoot.getAbsolutePath());
    Tomcat.addServlet(ctx, servletName, servletClass);
    ctx.addServletMapping("/*", servletName);
    tomcat.start();

    StringBuilder builder = new StringBuilder();
    baseUri = new URI(builder.append("http://127.0.0.1:").append(port).toString());
  }

  /**
   * Starts tomcat with servlet instance.
   * 
   * @param servlet instance of servlet
   * @param servletName name of servlet
   * @throws Exception
   */
  public TomcatServletRunner(Servlet servlet, String servletName) throws Exception {
    tomcat = new Tomcat();

    int port = TcpPortScanner.getEmptyPort();
    tomcat.setPort(port);

    File projectRoot = new File(".");
    Context ctx = tomcat.addContext("/", projectRoot.getAbsolutePath());
    Tomcat.addServlet(ctx, servletName, servlet);
    ctx.addServletMapping("/*", servletName);
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
   * Stop and destroy lauched tomcat.
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
