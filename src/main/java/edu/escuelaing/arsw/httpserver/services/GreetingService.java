package edu.escuelaing.arsw.httpserver.services;

import edu.escuelaing.arsw.httpserver.HttpResult;
import edu.escuelaing.arsw.httpserver.JsonSupport;

import java.util.Map;

/** GET /app/greeting?name=... -> {"message":"Hello, <name>!"} */
public final class GreetingService {

    private GreetingService() {
    }

    public static HttpResult handle(Map<String, String> params) {
        String name = params.get("name");
        if (name == null || name.isBlank()) {
            return HttpResult.json(400, JsonSupport.errorJson("Missing required query parameter 'name'."));
        }
        String body = "{\"message\":\"Hello, " + JsonSupport.escape(name.trim()) + "!\"}";
        return HttpResult.json(200, body);
    }
}
