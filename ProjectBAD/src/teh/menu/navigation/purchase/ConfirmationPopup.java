package teh.menu.navigation.purchase;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ConfirmationPopup {

    public final Stage stage = new Stage();

    private final BorderPane root = new BorderPane();
    private final GridPane top = new GridPane();
    private final GridPane center = new GridPane();

    private final Label title = new Label("Order Confirmation");

    private final Label confirmLabel = new Label("Are you sure you want to make purchase?");
    public final Button confirm = new Button("Confirm");
    public final Button cancel = new Button("Cancel");

    public ConfirmationPopup() {
        root.setTop(top);
        root.setCenter(center);
        HBox confirmBox = new HBox(10,
                confirm,
                cancel
        );

        title.setStyle("-fx-font-size: 17px; -fx-font-weight: bold; -fx-text-fill: #ffffff");

        top.setAlignment(Pos.CENTER);
        top.setStyle("-fx-background-color: #474b4d");
        top.add(title, 0, 0);
        confirmLabel.setStyle("-fx-font-size: 15px");
        confirm.setMinWidth(140);
        cancel.setMinWidth(140);

        center.setAlignment(Pos.CENTER);
        center.setStyle("-fx-background-color: #89abc1");

        center.add(confirmLabel, 0,0);
        center.add(confirmBox,0,1);



        stage.setScene(new Scene(root, 400, 500));
        stage.show();

    }

}
