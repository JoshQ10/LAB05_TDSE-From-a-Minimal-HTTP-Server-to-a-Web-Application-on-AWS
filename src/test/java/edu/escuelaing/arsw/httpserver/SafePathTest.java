package edu.escuelaing.arsw.httpserver;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class SafePathTest {

    @Test
    void rootMapsToIndexHtml() {
        assertEquals("public/index.html", SafePath.resolve("/"));
    }

    @Test
    void normalRequestResolvesUnderPublicRoot() {
        assertEquals("public/app.js", SafePath.resolve("/app.js"));
        assertEquals("public/images/banner.png", SafePath.resolve("/images/banner.png"));
    }

    @Test
    void plainTraversalIsRejected() {
        assertNull(SafePath.resolve("/../secret.txt"));
        assertNull(SafePath.resolve("/../../etc/passwd"));
    }

    @Test
    void traversalThatNetsOutInsideRootIsAllowedButStaysInsideRoot() {
        // "/images/../app.js" legitimately resolves back to a file inside public/.
        assertEquals("public/app.js", SafePath.resolve("/images/../app.js"));
    }

    @Test
    void encodedTraversalIsRejected() {
        assertNull(SafePath.resolve("/%2e%2e/secret.txt"));
    }
}
