package teh.menu.navigation;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import teh.data.Cart;
import teh.data.TransactionDetail;
import teh.data.TransactionHeader;

import java.util.List;

public class History extends NavigationAbstract {
    private final Label titleLabel = new Label(getCurrentUser().getUsername() + "'s Purchase History");
    private final Label nohistoryLabel = new Label("There's No History");
    private final Label descnohistoryLabel = new Label("Consider Purchasing Our Product");
    private final Label historyLabel = new Label("Select a Transaction to view Details");
    private final ListView<TransactionHeader> historyListView = new ListView<>();
    private final Label transactionID = new Label("Transaction ID: ");
    private final Label username = new Label("Username: " + getCurrentUser().getUsername());
    private final Label phonenumber = new Label("Phone Number: " + getCurrentUser().getPhonenumber());
    private final Label address = new Label ("Address: " + getCurrentUser().getAddress());
    private final Label totalPrice = new Label("Total: ");
    private final ListView<TransactionDetail> detailhistoryListView = new ListView<>();

    public History(Stage stage) {
        super(stage);
    }

    @Override
    protected void show() {
        titleLabel.setStyle("-fx-font-size: 32; -fx-font-weight: bold");
        historyLabel.setStyle("-fx-font-weight: bold");
        nohistoryLabel.setStyle("-fx-font-weight: bold");
        transactionID.setStyle("-fx-font-weight: bold");
        username.setStyle("-fx-font-weight: bold");
        GridPane.setMargin(titleLabel, new Insets(30, 0, 0, 20));
        GridPane.setMargin(historyListView, new Insets(0, 0, 0,20));

        VBox nohistoryBox = new VBox(10,
                nohistoryLabel,
                descnohistoryLabel
                );

        VBox historyBox = new VBox(10,
                transactionID,
                username,
                phonenumber,
                address,
                totalPrice,
                detailhistoryListView
        );

        historyListView.getItems().addAll(context.getPurchaseController().purchaseHistory(getCurrentUser()));


        gridPane.add(titleLabel,0,0);
        gridPane.add(historyListView,0, 1, 1, 5);
        gridPane.add(nohistoryBox, 1, 1);
        gridPane.add(historyLabel, 1,1);
        gridPane.add(historyBox,1, 1);

        historyBox.setVisible(false);

        if (historyListView.getItems().isEmpty()){
            nohistoryBox.setVisible(true);
            historyLabel.setVisible(false);
        } else {
            historyLabel.setVisible(true);
            nohistoryBox.setVisible(false);
        }

        historyListView.getSelectionModel().selectedItemProperty().addListener( (observable, oldValue, newValue) -> {
            if (newValue != null) {
                TransactionHeader transactionHeader = historyListView.getSelectionModel().getSelectedItem();

                historyLabel.setVisible(false);
                historyBox.setVisible(true);

                transactionID.setText("Transaction ID : " + newValue.getTransactionID());
                username.setText("Username : " + newValue.getUser().getUsername());
                phonenumber.setText("Phone number : " + newValue.getUser().getPhonenumber());
                address.setText("Address : " + newValue.getUser().getAddress());
                totalPrice.setText("Total: Rp. " + calculateTotal(newValue.getTransactionDetails()));
                detailhistoryListView.getItems().setAll(transactionHeader.getTransactionDetails());
            }
        });

    }

    private Long calculateTotal(List<TransactionDetail> tds) {
        long total = 0L;
        for (TransactionDetail td : tds) {
            total += td.getProduct().getProductPrice() * td.getQuantity();
        }

        return total;
    }

}
