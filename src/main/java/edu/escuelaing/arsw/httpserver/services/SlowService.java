package edu.escuelaing.arsw.httpserver.services;

import edu.escuelaing.arsw.httpserver.HttpResult;

/**
 * GET /app/slow -> sleeps a few seconds before answering.
 * Exists only to demonstrate, from two browser windows, that this server
 * handles one connection at a time (lab section 6.2): while this request
 * sleeps, every other request -static or dynamic- waits for it to finish.
 */
public final class SlowService {

    private static final long DELAY_MILLIS = 5000L;

    private SlowService() {
    }

    public static HttpResult handle() {
        try {
            Thread.sleep(DELAY_MILLIS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return HttpResult.json(200, "{\"message\":\"Finished after " + DELAY_MILLIS + "ms.\"}");
    }
}
