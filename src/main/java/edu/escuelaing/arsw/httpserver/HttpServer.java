package edu.escuelaing.arsw.httpserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * A sequential, socket-based HTTP server. It accepts one TCP connection,
 * fully reads the request, writes the response, closes the connection --
 * then goes back to accept() for the next one. There is no thread pool and
 * no queue: a second client always waits for the first one to finish.
 *
 * That limitation is intentional (see the lab README, section "Design
 * decisions"): the goal here is a correct baseline to observe, not a
 * production server.
 */
public final class HttpServer {

    private static final int DEFAULT_PORT = 35000;

    private HttpServer() {
    }

    public static void main(String[] args) throws IOException {
        int port = resolvePort(args);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("HTTP server ready on port " + port + " (all network interfaces).");
            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    handleClient(clientSocket);
                } catch (IOException e) {
                    // One bad connection must not stop the server: log and keep accepting.
                    System.err.println("Error handling connection: " + e.getMessage());
                }
            }
        }
    }

    private static void handleClient(Socket clientSocket) throws IOException {
        BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
        OutputStream out = clientSocket.getOutputStream();

        String requestLine = in.readLine();
        if (requestLine == null || requestLine.isBlank()) {
            return; // client opened and closed the connection without sending a request
        }
        consumeHeaders(in);

        System.out.println("Request: " + requestLine);

        HttpResult result = dispatch(requestLine);
        writeResponse(out, result);
    }

    private static HttpResult dispatch(String requestLine) {
        String[] parts = requestLine.split("\\s+");
        if (parts.length < 2) {
            return HttpResult.text(400, "text/plain; charset=UTF-8", "Bad Request: malformed request line");
        }
        String method = parts[0];
        String target = parts[1];
        return Router.route(method, target);
    }

    /** Reads and discards header lines up to the blank line that ends them. */
    private static void consumeHeaders(BufferedReader in) throws IOException {
        String line;
        while ((line = in.readLine()) != null && !line.isEmpty()) {
            // Headers are not needed for this lab's routing decisions.
        }
    }

    private static void writeResponse(OutputStream out, HttpResult result) throws IOException {
        byte[] body = result.body();
        String statusLine = "HTTP/1.1 " + result.status() + " " + reasonPhrase(result.status());

        StringBuilder headers = new StringBuilder();
        headers.append(statusLine).append("\r\n");
        headers.append("Content-Type: ").append(result.contentType()).append("\r\n");
        headers.append("Content-Length: ").append(body.length).append("\r\n");
        headers.append("Connection: close\r\n");
        headers.append("\r\n");

        out.write(headers.toString().getBytes(StandardCharsets.US_ASCII));
        out.write(body);
        out.flush();
    }

    private static String reasonPhrase(int status) {
        return switch (status) {
            case 200 -> "OK";
            case 400 -> "Bad Request";
            case 404 -> "Not Found";
            case 405 -> "Method Not Allowed";
            default -> "Unknown";
        };
    }

    private static int resolvePort(String[] args) {
        if (args.length > 0) {
            return Integer.parseInt(args[0]);
        }
        String fromEnv = System.getenv("SERVER_PORT");
        if (fromEnv != null && !fromEnv.isBlank()) {
            return Integer.parseInt(fromEnv.trim());
        }
        String fromProperty = System.getProperty("server.port");
        if (fromProperty != null && !fromProperty.isBlank()) {
            return Integer.parseInt(fromProperty.trim());
        }
        return DEFAULT_PORT;
    }
}
