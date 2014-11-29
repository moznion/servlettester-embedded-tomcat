package moznion.net.servlettester.embedded.tomcat;

import static org.junit.Assert.assertEquals;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Tester class for embedded tomcat.
 * 
 * @author moznion
 *
 */
public class TomcatServletRunnerTest {
  public static class MyServletClass extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
      resp.getWriter().print("Hey");
    }
  }

  @Test
  public void tomcatShouldBeRunningWithServletInstance() throws Exception {
    try (TomcatServletRunner runner = new TomcatServletRunner(new MyServletClass())) {
      checkResponce(runner);
      assertEquals(MyServletClass.class.getName(), runner.getServletName());
    }
  }

  @Test
  public void tomcatShouldBeRunningWithServletInstanceAndServletName() throws Exception {
    String servletName = "__MyServlet__";
    try (TomcatServletRunner runner = new TomcatServletRunner(new MyServletClass(), servletName)) {
      checkResponce(runner);
      assertEquals(servletName, runner.getServletName());
    }
  }

  @Test
  public void tomcatShouldBeRunningWithServletInstanceAndPort() throws Exception {
    int port = TcpPortScanner.getEmptyPort();
    try (TomcatServletRunner runner = new TomcatServletRunner(new MyServletClass(), port)) {
      checkResponce(runner);
      assertEquals("http://127.0.0.1:" + port, runner.getBaseUri().toString());
      assertEquals(MyServletClass.class.getName(), runner.getServletName());
    }
  }

  @Test
  public void tomcatShouldBeRunningWithServletInstanceAndServletNameAndPort() throws Exception {
    int port = TcpPortScanner.getEmptyPort();
    String servletName = "__MyServlet__";
    try (TomcatServletRunner runner =
        new TomcatServletRunner(new MyServletClass(), servletName, port)) {
      checkResponce(runner);
      assertEquals("http://127.0.0.1:" + port, runner.getBaseUri().toString());
      assertEquals(servletName, runner.getServletName());
    }
  }

  @Test
  public void tomcatShouldBeRunningWithServletClassName() throws Exception {
    try (TomcatServletRunner runner = new TomcatServletRunner(MyServletClass.class.getName())) {
      checkResponce(runner);
      assertEquals(MyServletClass.class.getName(), runner.getServletName());
    }
  }

  @Test
  public void tomcatShouldBeRunningWithServletClassNameAndServletName() throws Exception {
    String servletName = "__MyServlet__";
    try (TomcatServletRunner runner =
        new TomcatServletRunner(MyServletClass.class.getName(), servletName)) {
      checkResponce(runner);
      assertEquals(servletName, runner.getServletName());
    }
  }

  @Test
  public void tomcatShouldBeRunningWithServletClassNameAndPort() throws Exception {
    int port = TcpPortScanner.getEmptyPort();
    try (TomcatServletRunner runner = new TomcatServletRunner(MyServletClass.class.getName(), port)) {
      checkResponce(runner);
      assertEquals("http://127.0.0.1:" + port, runner.getBaseUri().toString());
      assertEquals(MyServletClass.class.getName(), runner.getServletName());
    }
  }

  @Test
  public void tomcatShouldBeRunningWithServletClassNameAndServletNameAndPort() throws Exception {
    int port = TcpPortScanner.getEmptyPort();
    String servletName = "__MyServlet__";
    try (TomcatServletRunner runner =
        new TomcatServletRunner(MyServletClass.class.getName(), servletName, port)) {
      checkResponce(runner);
      assertEquals("http://127.0.0.1:" + port, runner.getBaseUri().toString());
      assertEquals(servletName, runner.getServletName());
    }
  }

  private void checkResponce(TomcatServletRunner runner) throws Exception {
    URI baseUri = runner.getBaseUri();
    try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
      HttpGet request = new HttpGet(baseUri);
      try (CloseableHttpResponse resp = client.execute(request)) {
        String body = EntityUtils.toString(resp.getEntity(), StandardCharsets.UTF_8);
        assertEquals("Hey", body);
      }
    }
  }
}
