servlettester-embedded-tomcat [![Build Status](https://travis-ci.org/moznion/servlettester-embedded-tomcat.svg)](https://travis-ci.org/moznion/servlettester-embedded-tomcat)
==

HTTP servlet runner and tester with embedded tomcat. It uses Java8's lambda.

Testing code sample
--

### with callback

```java
TomcatServletTester.runServlet((req, resp) -> {
    resp.getWriter().print("Hey");
}, (uri) -> {
    try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
        HttpGet request = new HttpGet(uri);
        try (CloseableHttpResponse resp = client.execute(request)) {
            String body = EntityUtils.toString(resp.getEntity(), StandardCharsets.UTF_8);
            assertEquals("Hey", body);
        }
    }
});
```

### with servlet

```java
public static class MyServletClass extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
      resp.getWriter().print("Hey");
    }
}

TomcatServletTester.runServlet(new MyServletClass(), (uri) -> {
    // your testing code
});
```

### with servlet class name

```java
TomcatServletTester.runServlet(MyServletClass.class.getName(), (uri) -> {
    // your testing code
});
```

Tomcat resource is [AutoCloseable](https://docs.oracle.com/javase/8/docs/api/java/lang/AutoCloseable.html),
it means it will be released automatically when finish `runServlet` method.

Sample code to start embedded Tomcat
--

### with servlet

```java
try (TomcatServletRunner runner = new TomcatServletRunner(new MyServletClass())) {
    // your code
}
```

### with servlet class name

```java
try (TomcatServletRunner runner = new TomcatServletRunner(MyServletClass.class.getName())) {
    // your code
}
```

TomcatServletRunner implements [AutoCloseable](https://docs.oracle.com/javase/8/docs/api/java/lang/AutoCloseable.html),
so its resource will be released when it is finished try-with-resources block.

### When you want to release the launched tomcat resource

```java
runner.destroy();
```

See Also
--

[servlettester-jetty](https://github.com/tokuhirom/servlettester-jetty)

Lots of code are taken from here.

License
--

```
The MIT License (MIT)
Copyright © 2014 moznion, http://moznion.net/ <moznion@gmail.com>

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the “Software”), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
```

