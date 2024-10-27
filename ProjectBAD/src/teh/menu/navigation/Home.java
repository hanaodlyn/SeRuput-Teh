package teh.menu.navigation;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import teh.data.Cart;
import teh.data.Product;
import teh.menu.Login;

public class Home extends NavigationAbstract {

    private final Label titleLabel = new Label("SeRuput Teh");
    private final ListView<Product> productlistView = new ListView<>();

    private final Label welcomeLabel = new Label("Welcome, " + getCurrentUser().getUsername());
    private final Label descLabel = new Label("Select a product to view");
    private final Label itemName = new Label();
    private final Label itemDescription = new Label();
    private final Label itemPrice = new Label("Price: ");
    private final Label quantityLabel = new Label("Quantity: ");
    private final Spinner<Integer> quantitySpinner = new Spinner<>(1, Integer.MAX_VALUE, 1);
    private final Button addToCartButton = new Button("Add to Cart");
    private final Label totalLabel = new Label("Total: ");


    public Home(Stage stage) {
        super(stage);
    }

    @Override
    protected void show() {
        VBox welcomeBox = new VBox(10, welcomeLabel, descLabel);

        VBox productBox;
        if (getCurrentUser().isAdmin()) {
            productBox = new VBox(10,
                    itemName,
                    itemDescription,
                    itemPrice
            );
        } else {
            productBox = new VBox(10,
                    itemName,
                    itemDescription,
                    itemPrice,
                    new HBox(10, quantityLabel, quantitySpinner, totalLabel),
                    addToCartButton
            );
        }
        productBox.setVisible(false);

       // gridPane.setGridLinesVisible(true);
        GridPane.setMargin(titleLabel, new Insets(30, 0, 0, 20));
        GridPane.setMargin(productlistView, new Insets(0, 0, 0,20 ));
        titleLabel.setStyle("-fx-font-size: 32; -fx-font-weight: bold");
        welcomeLabel.setStyle("-fx-font-weight: bold");
        itemName.setStyle("-fx-font-weight: bold");
        addToCartButton.setMinWidth(140);
        productlistView.getItems().addAll(context.fetchProducts());

        productlistView.getSelectionModel().selectedItemProperty().addListener( (observable, oldValue, newValue) -> {
            if (newValue != null) {
                welcomeBox.setVisible(false);
                productBox.setVisible(true);

                itemName.setText(newValue.getProductName());
                itemDescription.setText(newValue.getProductDesc());
                itemPrice.setText("Price: Rp. " + newValue.getProductPrice());
                quantitySpinner.getValueFactory().setValue(1);
            }
        });

        itemDescription.setWrapText(true);
        itemDescription.setMaxWidth(400);

        totalLabel.setVisible(false);
        quantitySpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue >= 2) {
                    totalLabel.setVisible(true);
                    totalLabel.setText("Total: Rp. " + newValue * productlistView.getSelectionModel().getSelectedItem().getProductPrice());
                } else {
                    totalLabel.setVisible(false);
                }
        });

        gridPane.add(titleLabel, 0, 0);
        gridPane.add(productlistView,0, 1, 1, 5);
        gridPane.add(welcomeBox, 1, 1);
        gridPane.add(productBox, 1, 1);

        addToCartButton.setOnAction(event -> {
            Product product = productlistView.getSelectionModel().getSelectedItem();
            context.getCartController().addToCart(getCurrentUser(), product, quantitySpinner.getValue());
            alert(Alert.AlertType.INFORMATION,
                    "Message",
                    "Added to Cart",
                    null
            );
        });



    }
}
