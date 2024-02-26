package org.hbhbnr;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Router implements HttpHandler {

    private boolean run = true;

    void mainloop() throws IOException {
        final HttpServer httpServer = HttpServer.create(new InetSocketAddress(8080), 0);
        httpServer.createContext("/", this);
        httpServer.setExecutor(null);
        httpServer.start();
        while (run) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // ignore
            }
        }
        httpServer.stop(0);
    }

    @Override
    public void handle(final HttpExchange httpExchange) throws IOException {
        final URI requestURI = httpExchange.getRequestURI();
        final String path = requestURI.getPath();
        @SuppressWarnings("unused")
        final String query = requestURI.getQuery();

        final int statusCode;
        final String response;
        final String contentType;
        switch (path) {
            case "/end",
                 "/end/":
                statusCode = 200;
                response = "exit";
                contentType = "text/plain";
                this.run = false;
                break;
            case "/favicon",
                 "/favicon.ico":
                statusCode = 204;
                response = "";
                contentType = "image/x-icon";
                break;
            case "/css/style.css":
                statusCode = 204;
                response = "";
                contentType = "text/css";
                break;
            case "/main.html":
                statusCode = 200;
                response = "Welcome!";
                contentType = "text/html";
                break;
            default:
                statusCode = 404;
                response = "Not Found";
                contentType = "text/plain";
                break;
        }
        final byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);

        @SuppressWarnings("unused")
        InputStream is = httpExchange.getRequestBody();
        // read(is); // .. read the request body

        final Headers responesHeaders = httpExchange.getResponseHeaders();
        responesHeaders.set("Content-type", contentType);
        if (statusCode == 204) {
            httpExchange.sendResponseHeaders(statusCode, -1);
        } else {
            httpExchange.sendResponseHeaders(statusCode, responseBytes.length);
            final OutputStream os = httpExchange.getResponseBody();
            os.write(responseBytes);
            os.close();
        }
    }

}
