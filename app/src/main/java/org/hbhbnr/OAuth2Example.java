package org.hbhbnr;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class OAuth2Example implements HttpHandler {

    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) throws IOException {
        final OAuth2Example oAuth2Example = new OAuth2Example();
        final HttpServer httpServer = HttpServer.create(new InetSocketAddress(8080), 0);
        httpServer.createContext("/", oAuth2Example);
        httpServer.setExecutor(null);
        httpServer.start();
        // httpServer.stop(0);
    }

    @Override
    public void handle(final HttpExchange httpExchange) throws IOException {
        System.out.println(httpExchange.getRequestURI());
        final String okay = "Now: " + Instant.now().toString();
        final byte[] okayBytes = okay.getBytes(StandardCharsets.UTF_8);

        @SuppressWarnings("unused")
        InputStream is = httpExchange.getRequestBody();
        // read(is); // .. read the request body

        httpExchange.sendResponseHeaders(200, okayBytes.length);
        final OutputStream os = httpExchange.getResponseBody();
        os.write(okayBytes);
        os.close();
    }

}
