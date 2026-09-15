package edu.escuelaing.arsw.httpserver;

import java.nio.charset.StandardCharsets;

/**
 * Immutable outcome of routing a request: status code, content type and
 * the exact bytes to send back. Bytes (not characters) so the same path
 * serves text and binary resources identically.
 */
public final class HttpResult {

    private final int status;
    private final String contentType;
    private final byte[] body;

    public HttpResult(int status, String contentType, byte[] body) {
        this.status = status;
        this.contentType = contentType;
        this.body = body;
    }

    public static HttpResult text(int status, String contentType, String body) {
        return new HttpResult(status, contentType, body.getBytes(StandardCharsets.UTF_8));
    }

    public static HttpResult json(int status, String json) {
        return text(status, "application/json; charset=UTF-8", json);
    }

    public int status() {
        return status;
    }

    public String contentType() {
        return contentType;
    }

    public byte[] body() {
        return body;
    }
}
