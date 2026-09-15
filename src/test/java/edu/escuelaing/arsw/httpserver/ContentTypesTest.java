package edu.escuelaing.arsw.httpserver;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ContentTypesTest {

    @Test
    void htmlIsTextHtml() {
        assertTrue(ContentTypes.forPath("public/index.html").startsWith("text/html"));
    }

    @Test
    void jsIsApplicationJavascript() {
        assertTrue(ContentTypes.forPath("public/app.js").startsWith("application/javascript"));
    }

    @Test
    void pngIsImagePng() {
        assertEquals("image/png", ContentTypes.forPath("public/images/banner.png"));
    }

    @Test
    void jpgAndJpegAreImageJpeg() {
        assertEquals("image/jpeg", ContentTypes.forPath("public/images/photo.jpg"));
        assertEquals("image/jpeg", ContentTypes.forPath("public/images/photo.jpeg"));
    }

    @Test
    void unknownExtensionFallsBackToOctetStream() {
        assertEquals("application/octet-stream", ContentTypes.forPath("public/data.bin"));
    }
}
