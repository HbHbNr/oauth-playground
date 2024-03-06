package org.hbhbnr;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Router implements HttpHandler {

    private static final String SHUTDOWN_PAGE = "/shutdown";
    private static final String LOGIN_PAGE = "/login";
    private static final Set<String> PATHS_WITHOUT_LOGIN;
    static {
        final Set<String> pathsWithoutLoginTmp = new HashSet<String>();
        pathsWithoutLoginTmp.add(LOGIN_PAGE);
        pathsWithoutLoginTmp.add(SHUTDOWN_PAGE);
        pathsWithoutLoginTmp.add("/favicon");
        pathsWithoutLoginTmp.add("/favicon.ico");
        PATHS_WITHOUT_LOGIN = Collections.unmodifiableSet(pathsWithoutLoginTmp);
    }
    private static final byte[] FAVICON_BYTES = {0,0,1,0,1,0,16,16,2,0,1,0,1,0,-80,0,0,0,22,0,0,0,40,0,0,0,16,0,0,0,32,0,0,0,1,0,1,0,0,0,0,0,0,0,0,0,96,0,0,0,96,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,-128,-1,0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    private static final String SESSION_ID_COOKIE = "SESSION_ID";

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

    private boolean loggedIn(final Headers requestHeaders) {
        return false;
    }

    @Override
    public void handle(final HttpExchange httpExchange) throws IOException {
        final URI requestURI = httpExchange.getRequestURI();
        System.out.println(requestURI);
        final String path = requestURI.getPath();
        final Headers requestHeaders = httpExchange.getRequestHeaders();

        // redirect to login page if path is not on the allowlist
        if (!loggedIn(requestHeaders)) {
            if (!PATHS_WITHOUT_LOGIN.contains(path)) {
                redirectToLoginPage(path, httpExchange);
                return;
            }
        }

        final int statusCode;
        final byte[] responseBytes;
        final String contentType;
        switch (path) {
            case SHUTDOWN_PAGE:
                statusCode = 200;
                responseBytes = "exit".getBytes(StandardCharsets.UTF_8);
                contentType = "text/plain";
                this.run = false;
                break;
            case "/favicon",
                 "/favicon.ico":
//                statusCode = 204;
//                responseBytes = "".getBytes(StandardCharsets.UTF_8);
                statusCode = 200;
                responseBytes = FAVICON_BYTES;
                contentType = "image/x-icon";
                break;
            case "/css/style.css":
                statusCode = 204;
                responseBytes = "".getBytes(StandardCharsets.UTF_8);
                contentType = "text/css";
                break;
            case "/main.html":
                statusCode = 200;
                responseBytes = "Welcome!".getBytes(StandardCharsets.UTF_8);
                contentType = "text/html";
                break;
            case LOGIN_PAGE:
                statusCode = 200;
                responseBytes = "Login page...".getBytes(StandardCharsets.UTF_8);
                contentType = "text/html";
                break;
            default:
                statusCode = 404;
                responseBytes = "Not Found".getBytes(StandardCharsets.UTF_8);
                contentType = "text/plain";
                break;
        }

//        @SuppressWarnings("unused")
//        InputStream is = httpExchange.getRequestBody();
//        // read(is); // .. read the request body

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

    private void redirectToLoginPage(final String path, final HttpExchange httpExchange)
            throws IOException {
        httpExchange.getResponseHeaders().set("Location", LOGIN_PAGE);
        httpExchange.sendResponseHeaders(307, -1);
        System.out.println("Redirection from " + path + " to " + LOGIN_PAGE);
    }

}
