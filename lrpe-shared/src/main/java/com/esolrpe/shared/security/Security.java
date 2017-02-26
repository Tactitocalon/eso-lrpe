package com.esolrpe.shared.security;

public class Security {
    public static String escapeLuaString(String string) {
        if (string == null) {
            return "";
        }
        return string
                .replace("\\", "\\\\")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\"", "\\\"")
                .replace("\'", "\\'");
    }
}
