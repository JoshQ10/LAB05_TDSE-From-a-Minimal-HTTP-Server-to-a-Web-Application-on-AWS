package edu.escuelaing.arsw.httpserver;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RouterTest {

    private String bodyAsString(HttpResult result) {
        return new String(result.body(), StandardCharsets.UTF_8);
    }

    @Test
    void homePageIsServed() {
        HttpResult result = Router.route("GET", "/");
        assertEquals(200, result.status());
        assertTrue(result.contentType().startsWith("text/html"));
        assertTrue(bodyAsString(result).contains("<title>"));
    }

    @Test
    void missingStaticFileReturns404() {
        HttpResult result = Router.route("GET", "/does-not-exist.html");
        assertEquals(404, result.status());
    }

    @Test
    void unsupportedMethodReturns405() {
        HttpResult result = Router.route("POST", "/");
        assertEquals(405, result.status());
    }

    @Test
    void pathTraversalAttemptIsRejected() {
        HttpResult result = Router.route("GET", "/../pom.xml");
        assertEquals(400, result.status());
    }

    @Test
    void greetingServiceReturnsJsonMessage() {
        HttpResult result = Router.route("GET", "/app/greeting?name=Ada");
        assertEquals(200, result.status());
        assertTrue(result.contentType().startsWith("application/json"));
        assertTrue(bodyAsString(result).contains("Hello, Ada!"));
    }

    @Test
    void greetingServiceWithoutNameReturns400() {
        HttpResult result = Router.route("GET", "/app/greeting");
        assertEquals(400, result.status());
    }

    @Test
    void greetingServiceEscapesUntrustedInput() {
        HttpResult result = Router.route("GET", "/app/greeting?name=" + java.net.URLEncoder.encode("\"};alert(1)", StandardCharsets.UTF_8));
        assertEquals(200, result.status());
        assertTrue(bodyAsString(result).contains("\\\""));
    }

    @Test
    void squareServiceComputesResult() {
        HttpResult result = Router.route("GET", "/app/square?value=7");
        assertEquals(200, result.status());
        assertTrue(bodyAsString(result).contains("\"square\":49"));
    }

    @Test
    void squareServiceWithInvalidNumberReturns400() {
        HttpResult result = Router.route("GET", "/app/square?value=notanumber");
        assertEquals(400, result.status());
    }

    @Test
    void serverTimeServiceReturnsJson() {
        HttpResult result = Router.route("GET", "/app/time");
        assertEquals(200, result.status());
        assertTrue(bodyAsString(result).contains("serverTime"));
    }

    @Test
    void healthServiceReturns200() {
        HttpResult result = Router.route("GET", "/app/health");
        assertEquals(200, result.status());
        assertTrue(bodyAsString(result).contains("UP"));
    }
}
