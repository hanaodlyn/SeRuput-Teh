package teh;

import javafx.application.Application;
import javafx.stage.Stage;
import teh.menu.Login;

import java.sql.*;

public class Main extends Application {

    public static AppContext context;

    public static void main(String[] args) throws SQLException {
        context = new AppContext();
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("SeRuput Teh");

        Login login = new Login(primaryStage);
        login.start();


    }
}
