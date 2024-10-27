package teh.menu;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Register extends MenuAbstract {

    private final Label register = new Label("Register");
    private final Label usernameLabel = new Label("Username :");
    private final Label emailLabel = new Label("Email :");
    private final Label passLabel = new Label("Password :");
    private final Label confirmpassLabel = new Label("Confirm Password :");
    private final Label phoneLabel = new Label("Phone Number: ");
    private final Label addressLabel = new Label("Address: ");
    private final Label genderLabel = new Label("Gender: ");
    private final Label haveaccountLabel = new Label("Have an account?");
    private final Label loginLabel = new Label("login here");

    private final TextField usernameField = new TextField();
    private final TextField emailField = new TextField();
    private final PasswordField passField = new PasswordField();;
    private final PasswordField confirmpassField = new PasswordField();
    private final TextField phoneField = new TextField();
    private final TextArea addressField = new TextArea();
    private final RadioButton maleRButton = new RadioButton("Male");
    private final RadioButton femaleRButton = new RadioButton("Female");
    private final CheckBox termscheckbox = new CheckBox("I agree to all terms and conditions");
    private final Button registerButton = new Button("Register");


    public Register(Stage stage) {
        super(stage);
    }

    @Override
    protected void show() {
        HBox genderHbox = new HBox(10 , maleRButton, femaleRButton);
        HBox haveaccountHbox = new HBox(5, haveaccountLabel, loginLabel);
        register.setStyle("-fx-font-size: 32; -fx-font-weight: bold");
        loginLabel.setStyle("-fx-text-fill: #2750fa");

        //togglegroup (buat nge groupping)
        ToggleGroup genderGroup = new ToggleGroup();
        maleRButton.setToggleGroup(genderGroup);
        femaleRButton.setToggleGroup(genderGroup);

        loginLabel.setOnMouseClicked(event -> {
            Login login = new Login(stage);
            login.start();
        });

        loginLabel.setOnMouseEntered(e -> {
            loginLabel.setStyle("-fx-text-fill: #190766");
        });

        loginLabel.setOnMouseExited(e -> {
            loginLabel.setStyle("-fx-text-fill: #2750fa");
        });

        registerButton.setMinWidth(140);


        gridPane.setAlignment(Pos.CENTER);
        gridPane.add(register, 1, 0);
        gridPane.add(usernameLabel, 0, 1);
        gridPane.add(emailLabel, 0, 2);
        gridPane.add(passLabel, 0, 3);
        gridPane.add(confirmpassLabel, 0, 4);
        gridPane.add(phoneLabel, 0, 5);
        gridPane.add(addressLabel, 0, 6);
        gridPane.add(genderLabel, 0, 7);


        gridPane.add(usernameField, 1, 1);
        gridPane.add(emailField, 1, 2);
        gridPane.add(passField, 1, 3);
        gridPane.add(confirmpassField, 1, 4);
        gridPane.add(phoneField, 1, 5);
        gridPane.add(addressField, 1, 6);
        gridPane.add(genderHbox, 1, 7);
        gridPane.add(termscheckbox,1, 8);
        gridPane.add(haveaccountHbox, 1, 9);
        gridPane.add(registerButton,1, 10);



        registerButton.setOnAction(event -> {
            String username = usernameField.getText();
            String email = emailField.getText();
            String password = passField.getText();
            String confirmpassword = confirmpassField.getText();
            String phonenumber = phoneField.getText();
            String address = addressField.getText();

            if (username == null ||
                    email == null ||
                    password == null ||
                    confirmpassword == null ||
                    phonenumber == null ||
                    address == null ||
                    username.isEmpty() ||
                    email.isEmpty() ||
                    password.isEmpty() ||
                    confirmpassword.isEmpty() ||
                    phonenumber.isEmpty() ||
                    address.isEmpty()
            ) {
                alert(Alert.AlertType.ERROR, "Error",
                        "Failed to Register",
                        "All fields must be filled"
                );
                return;
            }

            //validasi-validasi
            if (!emailField.getText().endsWith("@gmail.com")) {
                alert(Alert.AlertType.ERROR, "Error",
                        "Failed to Register",
                        "Email must end with ‘@gmail.com’"
                         );
                return;
            }

            if (usernameField.getText().length() < 5 || usernameField.getText().length() > 20) {
                alert(Alert.AlertType.ERROR, "Error",
                        "Failed to Register",
                        "Username must be 5-20 characters"
                );
                return;
            }

            if (passField.getText().length() < 5) {
                alert(Alert.AlertType.ERROR, "Error",
                        "Failed to Register",
                        "Password must be at least 5 characters"
                        );
                return;
            }

            if (!passField.getText().chars().allMatch(Character::isLetterOrDigit)){
                alert(Alert.AlertType.ERROR, "Error",
                        "Failed to Register",
                        "Password must be alphanumeric"
                        );
                return;
            }

            if(!passField.getText().equals(confirmpassField.getText())) {
                alert(Alert.AlertType.ERROR, "Error",
                        "Failed to Register",
                        "Confirm Password must be equals to Password"
                        );
                return;
            }

            if (!phoneField.getText().startsWith("+62")) {
                alert(Alert.AlertType.ERROR, "Error",
                        "Failed to Register",
                        "Phone number must be start with +62"
                        );
                return;
            }

            if (!phoneField.getText().substring(4).chars().allMatch(Character::isDigit)) {
                alert(Alert.AlertType.ERROR, "Error",
                        "Failed to Register",
                        "Phone number must be numeric"
                );
                return;
            }

            ToggleButton toggleButton = (ToggleButton) genderGroup.getSelectedToggle();

            context.getUserController().register(username, password, phonenumber, address, "User", toggleButton.getText());
            alert(Alert.AlertType.INFORMATION, "Success",
                    "Registered Successfully!",
                    null
            );

                Login login = new Login(stage);
                login.start();

        });




    }

}
