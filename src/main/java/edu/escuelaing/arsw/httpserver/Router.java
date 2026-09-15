package edu.escuelaing.arsw.httpserver;

import edu.escuelaing.arsw.httpserver.services.GreetingService;
import edu.escuelaing.arsw.httpserver.services.HealthService;
import edu.escuelaing.arsw.httpserver.services.SlowService;
import edu.escuelaing.arsw.httpserver.services.SquareService;
import edu.escuelaing.arsw.httpserver.services.TimeService;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Decides what a request gets back. Deliberately a chain of explicit
 * if/else checks on exact paths, not a general routing framework: the
 * point of this lab is to make visible which URLs trigger which behavior,
 * not to hide that mechanism behind reflection or annotations.
 */
public final class Router {

    private Router() {
    }

    public static HttpResult route(String method, String rawTarget) {
        if (rawTarget == null || rawTarget.isEmpty()) {
            return HttpResult.text(400, "text/plain; charset=UTF-8", "Bad Request: empty target");
        }
        if (!"GET".equalsIgnoreCase(method)) {
            return HttpResult.text(405, "text/plain; charset=UTF-8", "Method Not Allowed: only GET is supported");
        }

        int qIndex = rawTarget.indexOf('?');
        String path = qIndex >= 0 ? rawTarget.substring(0, qIndex) : rawTarget;
        String rawQuery = qIndex >= 0 ? rawTarget.substring(qIndex + 1) : "";
        Map<String, String> params = QueryParams.parse(rawQuery);

        return switch (path) {
            case "/app/greeting" -> GreetingService.handle(params);
            case "/app/square" -> SquareService.handle(params);
            case "/app/time" -> TimeService.handle();
            case "/app/health" -> HealthService.handle();
            case "/app/slow" -> SlowService.handle();
            default -> serveStatic(path);
        };
    }

    private static HttpResult serveStatic(String path) {
        String resourcePath = SafePath.resolve(path);
        if (resourcePath == null) {
            return HttpResult.text(400, "text/plain; charset=UTF-8", "Bad Request: invalid path");
        }

        byte[] bytes = readClasspathResource(resourcePath);
        if (bytes == null) {
            return HttpResult.text(404, "text/plain; charset=UTF-8", "Not Found: " + path);
        }

        return new HttpResult(200, ContentTypes.forPath(resourcePath), bytes);
    }

    private static byte[] readClasspathResource(String resourcePath) {
        try (InputStream in = Router.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (in == null) {
                return null;
            }
            return in.readAllBytes();
        } catch (IOException e) {
            return null;
        }
    }
}
