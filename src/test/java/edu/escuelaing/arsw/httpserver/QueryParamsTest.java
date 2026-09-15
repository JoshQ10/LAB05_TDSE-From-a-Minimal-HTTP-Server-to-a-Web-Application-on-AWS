package edu.escuelaing.arsw.httpserver;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QueryParamsTest {

    @Test
    void parsesSingleParameter() {
        Map<String, String> params = QueryParams.parse("name=Ada");
        assertEquals("Ada", params.get("name"));
    }

    @Test
    void decodesUrlEncodedValues() {
        Map<String, String> params = QueryParams.parse("name=Ada%20Lovelace");
        assertEquals("Ada Lovelace", params.get("name"));
    }

    @Test
    void parsesMultipleParameters() {
        Map<String, String> params = QueryParams.parse("a=1&b=2");
        assertEquals("1", params.get("a"));
        assertEquals("2", params.get("b"));
    }

    @Test
    void emptyQueryYieldsEmptyMap() {
        assertTrue(QueryParams.parse("").isEmpty());
        assertTrue(QueryParams.parse(null).isEmpty());
    }
}
