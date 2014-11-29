package moznion.net.servlettester.embedded.tomcat;

import java.io.IOException;
import java.net.Socket;

class TcpPortScanner {
  public static int getEmptyPort() throws IOException {
    try (Socket socket = new Socket()) {
      socket.bind(null);
      return socket.getLocalPort();
    }
  }
}
