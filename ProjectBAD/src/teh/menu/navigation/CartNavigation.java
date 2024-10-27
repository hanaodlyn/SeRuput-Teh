package teh.menu.navigation;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import teh.data.Cart;
import teh.data.User;
import teh.menu.navigation.purchase.ConfirmationPopup;

import java.util.List;

public class CartNavigation extends NavigationAbstract {

    private final Label cartTitle = new Label(getCurrentUser().getUsername() + "'s Cart");
    private final Label welcomeLabel = new Label("Welcome, " + getCurrentUser().getUsername());
    private final Label selectLabel = new Label("Select a product to add and remove");
    private final ListView<Cart> cartListView = new ListView<>();
    private final Label itemName = new Label();
    private final Label itemDescription = new Label();
    private final Label itemPrice = new Label("Price: ");
    private final Label quantityLabel = new Label("Quantity: ");
    private final Spinner<Integer> quantitySpinner = new Spinner<>(1, Integer.MAX_VALUE, 1);
    private final Button updateCartButton = new Button("Update Cart");
    private final Button removeFromCartButton = new Button("Remove From Cart");
    private final Label totalItem = new Label("Total: ");
    private final Label totalLabel = new Label("Total: Rp. ");
    private final Label orderinformation = new Label("Order Information");
    private final Label username = new Label("Username: " + getCurrentUser().getUsername());
    private final Label phonenumber = new Label("Phone Number: " + getCurrentUser().getPhonenumber());
    private final Label address = new Label ("Address: " + getCurrentUser().getAddress());
    private final Button purchaseButton = new Button("Make Purchase");


    //no cart
    private final Label nocartLabel = new Label("No item in cart");
    private final Label descnocartLabel = new Label("Consider adding one!");

    public CartNavigation(Stage stage) {
        super(stage);
    }

    @Override
    protected void show() {
        VBox welcomeBox = new VBox(10, welcomeLabel, selectLabel);
        HBox cartBox = new HBox(10, updateCartButton, removeFromCartButton);

        VBox productBox = new VBox(10,
                itemName,
                itemDescription,
                itemPrice,
                new HBox(10, quantityLabel, quantitySpinner, totalItem),
                cartBox
        );
        productBox.setVisible(false);

        VBox infoBox = new VBox(10,
                totalLabel,
                orderinformation,
                username,
                phonenumber,
                address,
                purchaseButton
        );

        VBox noproductBox = new VBox(10,
                nocartLabel,
                descnocartLabel
                );
        noproductBox.setVisible(false);


        //gridpane
        GridPane.setMargin(cartTitle, new Insets(30, 0, 0, 20));
        GridPane.setMargin(cartListView, new Insets(0, 0, 0,20));
        GridPane.setMargin(infoBox, new Insets(0, 0, 0,20));

        nocartLabel.setStyle("-fx-font-weight: bold");
        cartTitle.setStyle("-fx-font-size: 32; -fx-font-weight: bold");
        welcomeLabel.setStyle("-fx-font-weight: bold");
        orderinformation.setStyle("-fx-font-weight: bold");
        updateCartButton.setMinWidth(140);
        removeFromCartButton.setMinWidth(140);
        purchaseButton.setMinWidth(140);
        cartListView.getItems().addAll(context.fetchCarts(getCurrentUser()));
        cartListView.setMaxHeight(150);
        cartListView.setMinWidth(250);

        welcomeBox.setVisible(true);

        cartListView.getSelectionModel().selectedItemProperty().addListener( (observable, oldValue, newValue) -> {
            if (newValue != null) {
                welcomeBox.setVisible(false);
                productBox.setVisible(true);

                itemName.setText(newValue.getProduct().getProductName());
                itemDescription.setText(newValue.getProduct().getProductDesc());
                itemPrice.setText("Price: Rp. " + newValue.getProduct().getProductPrice());
                quantitySpinner.getValueFactory().setValue(1);
            }
        });

        totalItem.setVisible(true);
        quantitySpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue >= 2) {
                totalItem.setVisible(true);
                totalItem.setText("Total: Rp. " + newValue * cartListView.getSelectionModel().
                        getSelectedItem().getProduct().getProductPrice());
            } else {
                totalItem.setVisible(false);
            }
        });

        itemDescription.setWrapText(true);
        itemDescription.setMaxWidth(400);

        if (cartListView.getItems().isEmpty()) {
            welcomeBox.setVisible(false);
            noproductBox.setVisible(true);
        }

        totalLabel.setText("Total: Rp." + calculateTotal(cartListView.getItems()));

        gridPane.add(cartTitle, 0, 0);
        gridPane.add(cartListView,0, 1, 1, 1);
        gridPane.add(noproductBox, 1, 1, 1, 2);
        gridPane.add(welcomeBox, 1, 1, 1, 2);
        gridPane.add(productBox, 1, 1, 1, 2);
        gridPane.add(infoBox, 0, 2);

        //gridPane.setGridLinesVisible(true);

        updateCartButton.setOnAction(event -> {
            Cart cart = cartListView.getSelectionModel().getSelectedItem();
            if (quantitySpinner.getValue() == 0) {
                alert(Alert.AlertType.ERROR, "Error",
                        "Not A Valid Amount",
                        null
                );
            }

            cart.setQuantity(quantitySpinner.getValue() + cart.getQuantity());

            if (cart.getQuantity() == 0) {
                context.getCartController().removeCart(cart);
            }

            context.getCartController().addToCart(cart.getUser(), cart.getProduct(), cart.getQuantity());
            cartListView.refresh();

            alert(Alert.AlertType.INFORMATION,
                    "Success",
                    "Cart Updated Successfully!",
                    null);
        });

        purchaseButton.setOnAction(event -> {

            if (cartListView.getItems().isEmpty()) {
                alert(Alert.AlertType.ERROR, "Error",
                        "Failed to Make Purchase",
                        "The cart is still empty"
                );
            } else {
                List<Cart> carts = cartListView.getItems();
                User user = getCurrentUser();

                ConfirmationPopup popup = new ConfirmationPopup();

                popup.confirm.setOnAction(e -> {
                    context.getPurchaseController().purchase(user, carts);
                    popup.stage.close();
                    new CartNavigation(stage).start();
                });

                popup.cancel.setOnAction(e -> {
                    popup.stage.close();
                });
            }

        });

        removeFromCartButton.setOnAction(event -> {
            Cart cart = cartListView.getSelectionModel().getSelectedItem();
            context.getCartController().removeCart(cart);

            cartListView.getItems().remove(cart);

            totalLabel.setText("Total: Rp." + calculateTotal(cartListView.getItems()));
            if (cartListView.getItems().isEmpty()) {
                welcomeBox.setVisible(true);
                productBox.setVisible(false);
            }
            alert(Alert.AlertType.INFORMATION,
                    "Success",
                    "Deleted from Cart",
                    null);
        });



   }

    private Long calculateTotal(List<Cart> carts) {
        long total = 0L;
        for (Cart cart : carts) {
            total += cart.getProduct().getProductPrice() * cart.getQuantity();
        }

        return total;
    }


}
