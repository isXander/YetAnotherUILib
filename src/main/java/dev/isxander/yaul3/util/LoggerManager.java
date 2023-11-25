package dev.isxander.yaul3.util;

import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApiStatus.Internal
public class LoggerManager {
    @ApiStatus.Internal
    public static Logger createLogger(String name) {
        return LoggerFactory.getLogger("YetAnotherUILibrary/" + name);
    }
}
