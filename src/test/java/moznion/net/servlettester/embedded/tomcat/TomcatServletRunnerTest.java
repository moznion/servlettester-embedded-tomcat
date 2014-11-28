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
    try (TomcatServletRunner runner = new TomcatServletRunner(new MyServletClass(), "MyServlet")) {
      checkResponce(runner);
    }
  }

  @Test
  public void tomcatShouldBeRunningWithServletClassName() throws Exception {
    try (TomcatServletRunner runner =
        new TomcatServletRunner(MyServletClass.class.getName(), "MyServlet")) {
      checkResponce(runner);
    }
  }

  private void checkResponce(TomcatServletRunner runner) throws Exception {
    URI baseURI = runner.getBaseUri();
    try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
      HttpGet request = new HttpGet(baseURI);
      try (CloseableHttpResponse resp = client.execute(request)) {
        String body = EntityUtils.toString(resp.getEntity(), StandardCharsets.UTF_8);
        assertEquals("Hey", body);
      }
    }
  }
}
