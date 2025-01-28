package com.dorukozgen.sponeyj;

import com.dorukozgen.sponeyj.streamer.License;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class MainController implements Initializable {
    @FXML
    private WebView mainWebView;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        License license = License.getInstance();
        WebEngine webEngine = this.mainWebView.getEngine();
        try {
            webEngine.load("http://localhost:8001/static/index.html");
        } catch (RuntimeException e) {
            webEngine.load("http://localhost:8001/static/noactive.html");
            System.out.println("Error: " + e.getMessage());
        }
        webEngine.setJavaScriptEnabled(true);
        this.mainWebView.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER)
                webEngine.executeScript("window.songEntered()");
        });
    }

    public WebView getMainWebView() {
        return this.mainWebView;
    }
}
