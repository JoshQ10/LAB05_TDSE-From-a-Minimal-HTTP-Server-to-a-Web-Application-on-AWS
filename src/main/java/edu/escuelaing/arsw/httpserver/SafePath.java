package edu.escuelaing.arsw.httpserver;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Resolves a raw request path into a location inside the public-resources
 * area, or refuses it. Requests are decoded and walked segment by segment
 * so a "../" (encoded or not) can never climb above the public root.
 */
public final class SafePath {

    private static final String PUBLIC_ROOT = "public";

    private SafePath() {
    }

    /**
     * @return the classpath resource path to load (e.g. "public/index.html"),
     *         or {@code null} if the request path is malformed or tries to
     *         escape the public-resources area.
     */
    public static String resolve(String rawPath) {
        String decoded;
        try {
            decoded = URLDecoder.decode(rawPath, StandardCharsets.UTF_8.name());
        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
            return null;
        }

        Deque<String> segments = new ArrayDeque<>();
        for (String segment : decoded.split("/")) {
            if (segment.isEmpty() || segment.equals(".")) {
                continue;
            }
            if (segment.equals("..")) {
                if (segments.isEmpty()) {
                    return null; // attempt to climb above the public root
                }
                segments.removeLast();
            } else {
                segments.addLast(segment);
            }
        }

        if (segments.isEmpty()) {
            segments.addLast("index.html");
        }

        return PUBLIC_ROOT + "/" + String.join("/", segments);
    }
}
