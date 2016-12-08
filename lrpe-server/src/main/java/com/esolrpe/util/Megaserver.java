package com.esolrpe.util;

import java.util.stream.Stream;

public enum Megaserver {
    NORTH_AMERICA("NA", 1),
    EUROPE("EU", 2),
    PUBLIC_TEST_SERVER("PTS", 3),;

    private final String code;
    private final int databaseCode;

    Megaserver(String code, int databaseCode) {
        this.code = code;
        this.databaseCode = databaseCode;
    }

    public String getCode() {
        return code;
    }

    public int getDatabaseCode() {
        return databaseCode;
    }

    public static Megaserver fromCode(String code) {
        return Stream.of(Megaserver.values())
                .filter(ms -> ms.getCode().equals(code))
                .findFirst()
                .orElse(null);
    }
}
