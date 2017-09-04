package io.monitorjbl

import org.springframework.context.ConfigurableApplicationContext

class IntegrationEnvironment {

  static ConfigurableApplicationContext app
  static int port = 8080
  static String rootUri

  static start() {
    // Get unused port
    def sock = new ServerSocket(0)
    port = sock.localPort
    rootUri = "http://localhost:${port}"
    sock.close()

    // Start app
    app = Main.start("--server.port=${port}")

    // Wait for app to start
    for (def i = 0; i < 15; i++) {
      try {
        new Socket("localhost", port).close()
        return
      } catch (e) {
        sleep(250)
      }
    }
    throw new RuntimeException("Application did not start up in time")
  }

  static stop() {
    app.close()
    for (def i = 0; i < 15; i++) {
      try {
        new Socket("localhost", port).close()
        sleep(1000)
      } catch (e) {
        return
      }
    }
    throw new RuntimeException("Application did not stop in time")
  }
}
