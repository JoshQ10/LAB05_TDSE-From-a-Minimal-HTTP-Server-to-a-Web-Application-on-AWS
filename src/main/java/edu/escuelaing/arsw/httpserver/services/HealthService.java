package edu.escuelaing.arsw.httpserver.services;

import edu.escuelaing.arsw.httpserver.HttpResult;

/** GET /app/health -> {"status":"UP"}. Confirms the process can serve requests. */
public final class HealthService {

    private HealthService() {
    }

    public static HttpResult handle() {
        return HttpResult.json(200, "{\"status\":\"UP\"}");
    }
}
