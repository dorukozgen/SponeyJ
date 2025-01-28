package com.dorukozgen.sponeyj.streamer;

import com.google.common.base.Charsets;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.springframework.util.FileSystemUtils;

public class Utils {
    public static List<String> getTxtLines(String fileName) throws IOException {
        ArrayList<String> lines = new ArrayList<>();
        File file = Paths.get("", new String[0]).resolve("./config/" + fileName + ".txt").toAbsolutePath().toFile();
        return Files.readAllLines(file.toPath(), Charsets.UTF_8);
    }

    public static void wait(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void clearProcesses() {
        try {
            Runtime.getRuntime().exec("taskkill /F /IM spotify.exe").waitFor();
            Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe").waitFor();
            Path appDataPath = Paths.get(System.getenv("APPDATA"), new String[0]);
            Arrays.<File>stream(Objects.<File[]>requireNonNull(appDataPath.toFile().listFiles())).filter(f -> (f.getName().startsWith("Spotify-") && f.isDirectory())).forEach(FileSystemUtils::deleteRecursively);
            Path localAppDataPath = Paths.get(System.getenv("LOCALAPPDATA"), new String[0]);
            Arrays.<File>stream(Objects.<File[]>requireNonNull(localAppDataPath.toFile().listFiles())).filter(f -> (f.getName().startsWith("Spotify-") && f.isDirectory())).forEach(FileSystemUtils::deleteRecursively);
        } catch (IOException|InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void removeSpotifyFolder(long mu) throws IOException {
        Path appDataPath = Paths.get(System.getenv("APPDATA"), new String[0]);
        Path localAppDataPath = Paths.get(System.getenv("LOCALAPPDATA"), new String[0]);
        if (Files.exists(appDataPath.resolve("Spotify-" + mu), new java.nio.file.LinkOption[0])) {
            FileSystemUtils.deleteRecursively(appDataPath.resolve("Spotify-" + mu));
            FileSystemUtils.deleteRecursively(localAppDataPath.resolve("Spotify-" + mu));
        }
    }

    public static void createSpotifyFolder(long mu) throws IOException {
        Path appDataPath = Paths.get(System.getenv("APPDATA"), new String[0]);
        Path localAppDataPath = Paths.get(System.getenv("LOCALAPPDATA"), new String[0]);
        if (Files.exists(appDataPath.resolve("Spotify-" + mu), new java.nio.file.LinkOption[0])) {
            FileSystemUtils.deleteRecursively(appDataPath.resolve("Spotify-" + mu));
            FileSystemUtils.deleteRecursively(localAppDataPath.resolve("Spotify-" + mu));
        }
        wait(1);
        Path appDataSpotifyPath = appDataPath.resolve("Spotify-" + mu);
        Files.createDirectory(appDataSpotifyPath, (FileAttribute<?>[])new FileAttribute[0]);
        Path prefsPath = appDataSpotifyPath.resolve("prefs");
        File prefsFile = Files.createFile(prefsPath, (FileAttribute<?>[])new FileAttribute[0]).toFile();
        BufferedWriter writer = new BufferedWriter(new FileWriter(prefsFile));
        writer.write("app.last-launched-version=\"1.2.4.912.g949d5fd0\"\napp.autostart-configured=false\ncampaign-id=\"organic\"\n"

                .trim());
        writer.close();
    }

    public static void createSpotifyFolder(long mu, Proxy proxy) throws IOException {
        Path appDataPath = Paths.get(System.getenv("APPDATA"), new String[0]);
        Path localAppDataPath = Paths.get(System.getenv("LOCALAPPDATA"), new String[0]);
        if (Files.exists(appDataPath.resolve("Spotify-" + mu), new java.nio.file.LinkOption[0])) {
            FileSystemUtils.deleteRecursively(appDataPath.resolve("Spotify-" + mu));
            FileSystemUtils.deleteRecursively(localAppDataPath.resolve("Spotify-" + mu));
        }
        wait(1);
        Path appDataSpotifyPath = appDataPath.resolve("Spotify-" + mu);
        Files.createDirectory(appDataSpotifyPath, (FileAttribute<?>[])new FileAttribute[0]);
        Path prefsPath = appDataSpotifyPath.resolve("prefs");
        File prefsFile = Files.createFile(prefsPath, (FileAttribute<?>[])new FileAttribute[0]).toFile();
        BufferedWriter writer = new BufferedWriter(new FileWriter(prefsFile));
        int proxyValue = proxy.getType().getValue();
        writer.write(String.format("app.last-launched-version=\"1.2.4.912.g949d5fd0\"\napp.autostart-configured=false\nnetwork.proxy.user=\"%s\"\nnetwork.proxy.addr=\"%s:%s:undefined\"\nnetwork.proxy.pass=\"%s\"\nnetwork.proxy.mode=%s\ncampaign-id=\"organic\"\n"

                .trim(), new Object[] { proxy.getUsername(), proxy.getHost(), Integer.valueOf(proxy.getPort()), proxy.getPassword(), Integer.valueOf(proxyValue) }));
        writer.close();
    }

    public static String getHWID() {
        boolean success = false;
        StringBuilder result = new StringBuilder();
        try {
            Process process = Runtime.getRuntime().exec("powershell.exe Get-CimInstance Win32_ComputerSystemProduct | select-object -expand uuid");
            process.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank())
                    result.append(line.trim());
            }
            success = true;
        } catch (IOException|InterruptedException e) {
            System.out.println("Error: " + e.getMessage());
            success = false;
        }
        try {
            if (!success) {
                Process process = Runtime.getRuntime().exec("wmic csproduct get uuid");
                process.waitFor();
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.isBlank())
                        result.append(line.trim());
                }
            }
        } catch (IOException|InterruptedException e) {
            throw new RuntimeException(e);
        }
        return result.toString();
    }
}
