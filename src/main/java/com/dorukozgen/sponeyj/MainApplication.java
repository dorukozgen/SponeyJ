package com.dorukozgen.sponeyj;

import com.dorukozgen.sponeyj.streamer.ThreadManager;
import com.dorukozgen.sponeyj.streamer.Utils;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Objects;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class MainApplication extends Application {
    private static ConfigurableApplicationContext context;

    ConfigManager appConfig;

    public static FXMLLoader fxmlLoader;

    public static boolean wsConnected = false;

    public void start(Stage stage) throws IOException {
        // Utils.clearProcesses();
        this.appConfig = new ConfigManager("application.properties");
        SpringApplication app = new SpringApplication(new Class[] { MainApplication.class });
        app.setDefaultProperties(
                Collections.singletonMap("server.port", this.appConfig.getProperty("server.port")));
        context = app.run();
        while (!wsConnected)
            Utils.wait(1);
        fxmlLoader = new FXMLLoader(Objects.<URL>requireNonNull(getClass().getResource("main.fxml")));
        Parent root = (Parent)fxmlLoader.load();
        stage.setTitle("Sponey Streamer - Desktop");
        stage.setResizable(false);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.getIcons().add(new Image(

                Objects.<InputStream>requireNonNull(getClass().getClassLoader().getResourceAsStream("static/images/logo.png"))));
        stage.setOnCloseRequest(e -> {
            context.close();
            if (ThreadManager.isRunning())
                ThreadManager.stopThreads();
            Utils.clearProcesses();
            Platform.exit();
            System.exit(0);
        });
        stage.show();
    }

    public static void main(String[] args) {
        launch(new String[0]);
    }
}
