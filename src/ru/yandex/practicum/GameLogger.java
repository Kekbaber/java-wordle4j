package ru.yandex.practicum;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GameLogger extends PrintWriter {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public GameLogger(String fileName, boolean append) throws IOException {
        super(new FileWriter(fileName, append), true);
    }

    public GameLogger(OutputStream out) {
        super(new OutputStreamWriter(out), true);
    }

    private void log(String level, String message) {
        println(String.format("[%s] %s: %s", LocalDateTime.now().format(formatter), level, message));
    }

    public void info(String message) {
        log("INFO", message);
    }

    public void warn(String message) {
        log("WARN", message);
    }

    public void error(String message) {
        log("ERROR", message);
    }

    public void info(String format, Object... args) {
        info(String.format(format, args));
    }

    public void warn(String format, Object... args) {
        warn(String.format(format, args));
    }

    public void error(String format, Object... args) {
        error(String.format(format, args));
    }
}
