package edu.escuelaing.arsw.httpserver;

/**
 * Maps a file extension to the HTTP content type a browser expects, so it
 * knows whether to render markup, execute a script or decode an image.
 */
public final class ContentTypes {

    private ContentTypes() {
    }

    public static String forPath(String path) {
        String lower = path.toLowerCase();
        int dot = lower.lastIndexOf('.');
        String extension = dot >= 0 ? lower.substring(dot + 1) : "";

        return switch (extension) {
            case "html", "htm" -> "text/html; charset=UTF-8";
            case "js" -> "application/javascript; charset=UTF-8";
            case "css" -> "text/css; charset=UTF-8";
            case "png" -> "image/png";
            case "jpg", "jpeg" -> "image/jpeg";
            case "ico" -> "image/x-icon";
            case "txt" -> "text/plain; charset=UTF-8";
            default -> "application/octet-stream";
        };
    }
}
