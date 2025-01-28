module com.dorukozgen.sponeyj {
    requires javafx.fxml;
    requires okhttp3;
    requires javafaker;
    requires org.seleniumhq.selenium.chrome_driver;
    requires org.seleniumhq.selenium.support;
    requires spring.core;
    requires spring.context;
    requires spring.messaging;
    requires spring.beans;
    requires spring.websocket;
    requires annotations;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires javafx.web;
    requires spring.webmvc;


    opens com.dorukozgen.sponeyj to javafx.fxml,spring.core;
    opens com.dorukozgen.sponeyj.websocket to spring.core;
    opens com.dorukozgen.sponeyj.websocket.controllers to spring.core,spring.beans;
    exports com.dorukozgen.sponeyj;
    exports com.dorukozgen.sponeyj.websocket;
    exports com.dorukozgen.sponeyj.streamer;
}