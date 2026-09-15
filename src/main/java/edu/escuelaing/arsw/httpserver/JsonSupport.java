package edu.escuelaing.arsw.httpserver;

/**
 * Minimal JSON helpers. There are exactly four hardcoded services in this
 * lab, so a full JSON library would be more machinery than the problem
 * needs; escaping is still mandatory because the greeting service embeds a
 * value the client controls.
 */
public final class JsonSupport {

    private JsonSupport() {
    }

    public static String escape(String value) {
        StringBuilder out = new StringBuilder(value.length() + 8);
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            switch (c) {
                case '"' -> out.append("\\\"");
                case '\\' -> out.append("\\\\");
                case '\n' -> out.append("\\n");
                case '\r' -> out.append("\\r");
                case '\t' -> out.append("\\t");
                default -> {
                    if (c < 0x20) {
                        out.append(String.format("\\u%04x", (int) c));
                    } else {
                        out.append(c);
                    }
                }
            }
        }
        return out.toString();
    }

    public static String errorJson(String message) {
        return "{\"error\":\"" + escape(message) + "\"}";
    }
}
