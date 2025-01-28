package com.dorukozgen.sponeyj.streamer;

import com.github.javafaker.Faker;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Random;
import java.util.function.Function;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Streamer extends Thread {
    private final String[] songs;

    private final int port;

    private long mu;

    private String username = "";

    private String password = "";

    private boolean resetAccount = false;

    private final int[] repeat = new int[] { 2, 3 };

    private final int[] playtime = new int[] { 110, 120 };

    private Proxy proxy = null;

    private Faker faker = new Faker();

    private Process process;

    private ChromeDriverService driverService;

    private ChromeDriver driver;

    private WebDriverWait wait;

    public Streamer(String[] songs, int port, int mu) {
        this.songs = songs;
        this.port = port;
        this.mu = mu;
    }

    public Streamer(String[] songs, int port, int mu, Proxy proxy) {
        this.songs = songs;
        this.port = port;
        this.mu = mu;
        this.proxy = proxy;
    }

    public Streamer(String[] songs, int port, int mu, String username, String password) {
        this.songs = songs;
        this.port = port;
        this.mu = mu;
        this.username = username;
        this.password = password;
    }

    public Streamer(String[] songs, int port, int mu, String username, String password, Proxy proxy) {
        this.songs = songs;
        this.port = port;
        this.mu = mu;
        this.username = username;
        this.password = password;
        this.proxy = proxy;
    }

    public void run() {
        while (true) {
            try {
                stream();
            } catch (RuntimeException e) {
                Values.addFailedStreams();
            }
            Utils.wait(5);
            try {
                Utils.removeSpotifyFolder(this.mu);
            } catch (IOException iOException) {}
            Utils.wait(5);
        }
    }

    private void stream() {
        try {
            Random random = new Random();
            long min = 100000L;
            long max = 9999999999L;
            this.mu = min + (long)(random.nextDouble() * (max - min));
            try {
                if (this.proxy != null) {
                    Utils.createSpotifyFolder(this.mu, this.proxy);
                } else {
                    Utils.createSpotifyFolder(this.mu);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Utils.wait(1);
            ProcessBuilder processBuilder = new ProcessBuilder(new String[] { Paths.get("", new String[0]).resolve("./requirements/Spotify/spotify.exe").toAbsolutePath().toString(), "--remote-debugging-port=" + this.port, "--mu=" + this.mu, "--maximized" });
            try {
                this.process = processBuilder.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Utils.wait(6);
            this

                    .driverService = (ChromeDriverService)((ChromeDriverService.Builder)((ChromeDriverService.Builder)(new ChromeDriverService.Builder()).usingDriverExecutable(Paths.get("", new String[0]).resolve("./requirements/chromedriver.exe").toAbsolutePath().toFile())).usingAnyFreePort()).withSilent(true).build();
            try {
                this.driverService.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ChromeOptions options = getChromeOptions();
            this.driver = new ChromeDriver(this.driverService, options);
            this.wait = new WebDriverWait((WebDriver)this.driver, Duration.ofSeconds(30L));
            login();
            Utils.wait(1);
            for (String song : this.songs) {
                searchSong(song);
                Utils.wait(1);
                playSong();
                Utils.wait(1);
            }
            close();
        } catch (Exception e) {
            close();
            throw new RuntimeException(e);
        }
    }

    private ChromeOptions getChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("debuggerAddress", "localhost:" + this.port);
        options.addArguments(new String[] { "--no-sandbox" });
        options.addArguments(new String[] { "--disable-crash-reporter" });
        options.addArguments(new String[] { "--disable-extensions" });
        options.addArguments(new String[] { "--disable-in-process-stack-traces" });
        options.addArguments(new String[] { "--disable-logging" });
        options.addArguments(new String[] { "--disable-dev-shm-usage" });
        options.addArguments(new String[] { "--log-level=3" });
        options.addArguments(new String[] { "--output=/dev/null" });
        return options;
    }

    private void login() {
        ((WebElement)this.wait.until(d -> d.findElement(By.xpath("//*[@id=\"login-button\"]")))).click();
        Utils.wait(1);
        if (this.username.isEmpty() || this.password.isEmpty() || this.username.isBlank() || this.password.isBlank() || this.resetAccount) {
            this.resetAccount = true;
            this.username = this.faker.name().username() + this.faker.name().username() + "@gmail.com";
            this.password = this.faker.internet().password(12, 15, true, true, true);
            ((WebElement)this.wait.until(d -> d.findElement(By.xpath("//*[@id=\"signup-link\"]"))))
                    .click();
            Utils.wait(1);
            ((WebElement)this.wait.until(d -> d.findElement(By.xpath("//*[@name=\"email\"]"))))
                    .sendKeys(new CharSequence[] { this.username });
            Utils.wait(1);
            ((WebElement)this.wait.until(d -> d.findElement(By.xpath("//*[@name=\"password\"]"))))
                    .sendKeys(new CharSequence[] { this.password });
            Utils.wait(1);
            ((WebElement)this.wait.until(d -> d.findElement(By.xpath("//*[@name=\"name\"]"))))
                    .sendKeys(new CharSequence[] { this.faker.name().fullName() });
            Utils.wait(1);
            ((WebElement)this.wait.until(d -> d.findElement(By.xpath("//*[@id=\"signup-button\"]"))))
                    .click();
            Utils.wait(1);
            Select monthSelect = new Select((WebElement)this.wait.until(d -> d.findElement(By.xpath("//*[@id=\"month-field\"]"))));
            Random random = new Random();
            monthSelect.selectByIndex(random.nextInt(12));
            Utils.wait(1);
            ((WebElement)this.wait.until(d -> d.findElement(By.xpath("//*[@id=\"day-field\"]"))))
                    .sendKeys(new CharSequence[] { String.valueOf(random.nextInt(28)) });
            Utils.wait(1);
            ((WebElement)this.wait.until(d -> d.findElement(By.xpath("//*[@id=\"year-field\"]"))))
                    .sendKeys(new CharSequence[] { String.valueOf(random.nextInt(14) + 1990) });
            Utils.wait(1);
            if (random.nextBoolean()) {
                WebElement radio = (WebElement)this.wait.until(d -> d.findElement(By.xpath("//*[@id=\"female-radio\"]")));
                this.driver.executeScript("arguments[0].click();", new Object[] { radio });
            } else {
                WebElement radio = (WebElement)this.wait.until(d -> d.findElement(By.xpath("//*[@id=\"male-radio\"]")));
                this.driver.executeScript("arguments[0].click();", new Object[] { radio });
            }
            Utils.wait(1);
            try {
                WebElement agreeTerms = (WebElement)this.wait.until(d -> d.findElement(By.xpath("//input[@id=\"agree-eula-2-field\"]")));
                this.driver.executeScript("arguments[0].click();", new Object[] { agreeTerms });
            } catch (Exception exception) {}
            Utils.wait(1);
            try {
                WebElement agreeTerms = (WebElement)this.wait.until(d -> d.findElement(By.xpath("//input[@id=\"agree-tos-2-field\"]")));
                this.driver.executeScript("arguments[0].click();", new Object[] { agreeTerms });
            } catch (Exception exception) {}
            Utils.wait(1);
            try {
                WebElement agreeTerms = (WebElement)this.wait.until(d -> d.findElement(By.xpath("//input[@id=\"agree-privacy-2-field\"]")));
                this.driver.executeScript("arguments[0].click();", new Object[] { agreeTerms });
            } catch (Exception exception) {}
            Utils.wait(1);
            ((WebElement)this.wait.until(d -> d.findElement(By.xpath("//*[@id=\"signup-button\"]"))))
                    .click();
        } else {
            ((WebElement)this.wait.until(d -> d.findElement(By.xpath("//*[@id=\"GlueTextInput-1\"]")))).sendKeys(new CharSequence[] { this.username });
            ((WebElement)this.wait.until(d -> d.findElement(By.xpath("//*[@id=\"GlueTextInput-2\"]")))).sendKeys(new CharSequence[] { this.password });
            Utils.wait(1);
            ((WebElement)this.wait.until(d -> d.findElement(By.xpath("//*[@id=\"login-button\"]")))).click();
        }
        Utils.wait(1);
    }

    private void searchSong(String url) {
        WebElement searchButton = (WebElement)this.wait.until((Function)ExpectedConditions.elementToBeClickable(By.xpath("//*[@href=\"/search\"]")));
        this.driver.executeScript("arguments[0].click();", new Object[] { searchButton });
        Utils.wait(1);
        ((WebElement)this.wait.until((Function)ExpectedConditions.elementToBeClickable(By.xpath("//*[@data-testid=\"search-input\"]"))))
                .sendKeys(new CharSequence[] { url + url });
        Utils.wait(1);
    }

    private void playSong() {
        WebElement playButton = (WebElement)this.wait.until(d -> d.findElement(By.xpath("//button[@data-testid=\"play-button\"]")));
        ChromeDriver chromeDriver = this.driver;
        chromeDriver.executeScript("arguments[0].scrollIntoView(true);", new Object[] { playButton });
        Utils.wait(1);
        chromeDriver.executeScript("arguments[0].click();", new Object[] { playButton });
        Random random = new Random();
        int randomRepeat = random.nextInt(this.repeat[1] - this.repeat[0]) + this.repeat[0];
        int randomPlaytime = random.nextInt(this.playtime[1] - this.playtime[0]) + this.playtime[0];
        for (int i = 0; i < randomRepeat; i++) {
            Utils.wait(randomPlaytime);
            WebElement skip = (WebElement)this.wait.until(d -> d.findElement(By.xpath("//*[@data-testid=\"control-button-skip-forward\"]")));
            chromeDriver.executeScript("arguments[0].click();", new Object[] { skip });
            Utils.wait(2);
            WebElement previous = (WebElement)this.wait.until(d -> d.findElement(By.xpath("//*[@data-testid=\"control-button-skip-back\"]")));
            chromeDriver.executeScript("arguments[0].click();", new Object[] { previous });
            Values.addSuccessfulStreams();
            Values.addEarning(0.003D);
        }
    }

    private void close() {
        if (this.driver != null)
            this.driver.quit();
        if (this.driverService != null)
            this.driverService.stop();
        if (this.process != null)
            this.process.destroy();
    }

    public void interrupt() {
        if (this.driver != null)
            this.driver.quit();
        if (this.driverService != null)
            this.driverService.stop();
        super.interrupt();
    }
}
