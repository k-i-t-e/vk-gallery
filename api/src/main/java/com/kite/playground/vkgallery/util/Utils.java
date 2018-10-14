package com.kite.playground.vkgallery.util;

import java.time.Clock;
import java.time.LocalDateTime;

public final class Utils {
    private Utils() {
    }

    public static LocalDateTime nowUTC() {
        return LocalDateTime.now(Clock.systemUTC());
    }
}
