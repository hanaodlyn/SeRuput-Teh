package teh.menu;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import teh.menu.navigation.Home;

public class Login extends MenuAbstract {

    private final Label title = new Label("Login");
    private final Label usernameLabel = new Label("Username : ");
    private final Label passwordLabel = new Label("Password : ");

    private final Label noAccountLabel = new Label("Don't have account yet?");
    private final Label registerLabel = new Label("register here");

    private final TextField usernameField = new TextField();
    private final TextField passwordField = new PasswordField();

    private final Button loginButton = new Button("Login");

    public Login(Stage stage) {
        super(stage);
    }

    @Override
    protected void show() {
        HBox noAccountBox = new HBox(5, noAccountLabel, registerLabel);
        registerLabel.setStyle("-fx-text-fill: #2750fa");
        registerLabel.setOnMouseClicked(event -> {
            Register register = new Register(stage);
            register.start();
        });

        registerLabel.setOnMouseEntered(e -> {
            registerLabel.setStyle("-fx-text-fill: #190766");
        });

        registerLabel.setOnMouseExited(e -> {
            registerLabel.setStyle("-fx-text-fill: #2750fa");
        });

        loginButton.setMinWidth(140);

        title.setStyle("-fx-font-size: 32; -fx-font-weight: bold");

        gridPane.setAlignment(Pos.CENTER);
        gridPane.add(title, 1, 0);
        gridPane.add(usernameLabel, 0, 1);
        gridPane.add(passwordLabel, 0, 2);

        gridPane.add(usernameField, 1, 1);
        gridPane.add(passwordField, 1, 2);
        gridPane.add(noAccountBox, 1, 3);

        gridPane.add(loginButton, 1, 4);

        // buat ngeliatin posisi gridpane nya
        // gridPane.setGridLinesVisible(true);

        //event listener
        loginButton.setOnAction(event -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            alert(Alert.AlertType.ERROR, "Error",
                    "Failed to Login",
                    "All fields must be filled"
                    );

                return;
            }

            context.getUserController().login(username, password).ifPresentOrElse(user -> {
                context.setCurrentUser(user);

                Home home = new Home(stage);
                home.start();
            }, () -> {
                alert(Alert.AlertType.ERROR, "Error",
                        "Failed to Login",
                        "Invalid Credential");
            });

        });







    }



}
