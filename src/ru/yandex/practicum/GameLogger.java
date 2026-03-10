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

    public void info(String message) {
        println(String.format("[%s] INFO: %s", LocalDateTime.now().format(formatter), message));
    }

    public void error(String message) {
        println(String.format("[%s] ERROR: %s", LocalDateTime.now().format(formatter), message));
    }

    public void warn(String message) {
        println(String.format("[%s] WARN: %s", LocalDateTime.now().format(formatter), message));
    }
}
