package com.example.task01;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class Task01Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        System.out.println("1 трек — " + extractSoundName(new File("task01/src/main/resources/3724.mp3")));
        System.out.println("2 трек — " + extractSoundName(new File("task01/src/main/resources/3726.mp3")));
        System.out.println("3 трек — " + extractSoundName(new File("task01/src/main/resources/3727.mp3")));
    }

    public static String extractSoundName(File file) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder(
                "ffprobe", "-v", "error", "-of", "flat", "-show_format", file.getAbsolutePath()
        );

        Process process = processBuilder.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        String title = null;

        while ((line = reader.readLine()) != null) {
            if (line.contains("format.tags.title")) {
                int equalsIndex = line.indexOf('=');
                if (equalsIndex != -1) {
                    title = line.substring(equalsIndex + 1).replaceAll("\"", "");
                    break;
                }
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new IOException("ffprobe завершился с ошибкой. Код: " + exitCode);
        }

        return title != null ? title : "название трека не найдено";
    }
}
