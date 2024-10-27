package teh.menu;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import teh.AppContext;
import teh.Main;

public abstract class MenuAbstract {

    protected final AppContext context;

    protected final GridPane gridPane = new GridPane();
    protected final Scene scene = new Scene(gridPane, 800, 700);
    protected final Stage stage;
    
    //constructor
    public MenuAbstract(Stage stage) {
        this.context = Main.context;
        this.stage = stage;
        gridPane.setVgap(10);
        gridPane.setHgap(10);
    }

    protected abstract void show();

    public void start() {
        show();
        stage.setScene(scene);
        stage.show();
    }

    public void alert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
