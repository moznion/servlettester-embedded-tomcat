package moznion.net.servlettester.embedded.tomcat;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import moznion.net.servlettester.embedded.tomcat.TomcatServletTester.UriConsumer;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

public class TomcatServletTesterTest {
  public static class MyServletClass extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
      resp.getWriter().print("Hey");
    }
  }

  private UriConsumer body = (uri) -> {
    try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
      HttpGet request = new HttpGet(uri);
      try (CloseableHttpResponse resp = client.execute(request)) {
        String body = EntityUtils.toString(resp.getEntity(), StandardCharsets.UTF_8);
        assertEquals("Hey", body);
      }
    }
  };

  @Test
  public void shouldBeEnableToTestWithServletClassName() throws Exception {
    TomcatServletTester.runServlet(MyServletClass.class.getName(), body);
  }

  @Test
  public void shouldBeEnableToTestWithServlet() throws Exception {
    TomcatServletTester.runServlet(new MyServletClass(), body);
  }

  @Test
  public void shouldBeEnableToTestWithServletCallback() throws Exception {
    TomcatServletTester.runServlet((req, resp) -> {
      resp.getWriter().print("Hey");
    }, body);
  }
}
