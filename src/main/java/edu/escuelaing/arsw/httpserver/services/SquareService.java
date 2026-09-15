package edu.escuelaing.arsw.httpserver.services;

import edu.escuelaing.arsw.httpserver.HttpResult;
import edu.escuelaing.arsw.httpserver.JsonSupport;

import java.util.Map;

/** GET /app/square?value=... -> {"input":<n>,"square":<n*n>} */
public final class SquareService {

    private SquareService() {
    }

    public static HttpResult handle(Map<String, String> params) {
        String raw = params.get("value");
        if (raw == null || raw.isBlank()) {
            return HttpResult.json(400, JsonSupport.errorJson("Missing required query parameter 'value'."));
        }

        double value;
        try {
            value = Double.parseDouble(raw.trim());
        } catch (NumberFormatException e) {
            return HttpResult.json(400, JsonSupport.errorJson("'value' must be a number."));
        }
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            return HttpResult.json(400, JsonSupport.errorJson("'value' must be a finite number."));
        }

        double square = value * value;
        String body = "{\"input\":" + format(value) + ",\"square\":" + format(square) + "}";
        return HttpResult.json(200, body);
    }

    private static String format(double value) {
        if (value == Math.rint(value) && !Double.isInfinite(value)) {
            return Long.toString((long) value);
        }
        return Double.toString(value);
    }
}
