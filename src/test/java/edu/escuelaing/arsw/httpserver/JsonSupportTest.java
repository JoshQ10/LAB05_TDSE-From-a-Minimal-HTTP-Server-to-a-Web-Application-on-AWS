package edu.escuelaing.arsw.httpserver;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonSupportTest {

    @Test
    void escapesQuotesAndBackslashes() {
        assertEquals("a\\\"b\\\\c", JsonSupport.escape("a\"b\\c"));
    }

    @Test
    void escapesControlCharacters() {
        assertEquals("line1\\nline2", JsonSupport.escape("line1\nline2"));
    }

    @Test
    void leavesPlainTextUntouched() {
        assertEquals("Ada Lovelace", JsonSupport.escape("Ada Lovelace"));
    }

    @Test
    void neutralizesJsonInjectionAttempt() {
        String malicious = "Ada\",\"admin\":true,\"x\":\"";
        String escaped = JsonSupport.escape(malicious);
        assertEquals("Ada\\\",\\\"admin\\\":true,\\\"x\\\":\\\"", escaped);
    }
}
