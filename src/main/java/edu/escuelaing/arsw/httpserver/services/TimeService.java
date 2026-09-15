package edu.escuelaing.arsw.httpserver.services;

import edu.escuelaing.arsw.httpserver.HttpResult;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/** GET /app/time -> {"serverTime":"<ISO-8601 timestamp>"} */
public final class TimeService {

    private TimeService() {
    }

    public static HttpResult handle() {
        String now = ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        return HttpResult.json(200, "{\"serverTime\":\"" + now + "\"}");
    }
}
