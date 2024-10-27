package teh.menu.navigation;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import teh.data.Product;

import java.time.Clock;

public class ManageProduct extends NavigationAbstract{
    private final Label manageTitle = new Label("Manage Products");
    private final Label welcomeLabel = new Label("Welcome, " + getCurrentUser().getUsername());
    private final Label selectLabel = new Label("Select a Product to Update");
    private final Button addButton = new Button("Add Product");
    private final ListView<Product> manageProductListView = new ListView<>();

    // info
    private final Label itemLabel = new Label();
    private final Label itemDescription = new Label();
    private final Label itemPrice = new Label("Price: ");
    private final Button productButton = new Button("Add Product");
    private final Button updateButton = new Button("Update Product");
    private final Button removeButton = new Button("Remove Product");

    // create
    private final Label inputLabel = new Label("Input product name");
    private final Label priceLabel = new Label("Input product price");
    private final Label decsriptionLabel = new Label("Input product description");
    private final TextField nameField = new TextField();
    private final TextField priceField = new TextField();
    private final TextArea textField = new TextArea();
    private final Button addProductButton = new Button("Add Product");
    private final Button backAddButton = new Button("Back");

    // update
    private final TextField productPrice = new TextField();
    private final Label inputProduct = new Label("Input Product Price");
    private final Button updateProductButton = new Button ("Update Product");
    private final Button backUpdateButton = new Button ("Back");

    // delete / remove
    private final Label removeLabel = new Label("Are you sure, you want to remove this product?");
    private final Button removeProductButton = new Button ("Remove Product");
    private final Button backRemoveButton = new Button ("Back");

    public ManageProduct(Stage stage) {
        super(stage);
    }

    @Override
    protected void show() {
        manageTitle.setStyle("-fx-font-size: 32; -fx-font-weight: bold");
        welcomeLabel.setStyle("-fx-font-weight: bold");
        inputLabel.setStyle("-fx-font-size: 15; -fx-font-weight: bold");
        priceLabel.setStyle("-fx-font-size: 15; -fx-font-weight: bold");
        decsriptionLabel.setStyle("-fx-font-size: 15; -fx-font-weight: bold");
        VBox welcomeBox = new VBox(10, welcomeLabel, selectLabel, addButton);

        VBox infoProductBox = new VBox(10,
                itemLabel,
                itemDescription,
                itemPrice,
                productButton,
                new HBox(10, updateButton, removeButton)
        );

        VBox addProductBox = new VBox(10,
                inputLabel,
                nameField,
                priceLabel,
                priceField,
                decsriptionLabel,
                textField,
                new HBox(10, addProductButton, backAddButton)
        );

        VBox updateProductBox = new VBox(10,
                inputProduct,
                productPrice,
                new HBox(10, updateProductButton, backUpdateButton)
        );

        VBox removeProductBox = new VBox(10,
                removeLabel,
                new HBox(10, removeProductButton, backRemoveButton)
                );


        GridPane.setMargin(manageTitle, new Insets(30, 0, 0, 20));
        GridPane.setMargin(manageProductListView, new Insets(0, 0, 0,20));
        gridPane.add(manageTitle, 0, 0);
        gridPane.add(manageProductListView,0, 1, 1, 3);
        gridPane.add(welcomeBox, 1, 1);
        gridPane.add(infoProductBox, 1, 1);


        //add
        addProductBox.setVisible(false);
        addButton.setMinWidth(140);
        addProductButton.setMinWidth(140);
        backAddButton.setMinWidth(140);
        gridPane.add(addProductBox, 1, 2);
        nameField.setMaxWidth(400);
        priceField.setMaxWidth(400);
        textField.setMaxWidth(400);

        //info
        infoProductBox.setVisible(false);
        itemLabel.setStyle("-fx-font-weight: bold");
        itemDescription.setWrapText(true);
        itemDescription.setMaxWidth(400);
        productButton.setMinWidth(140);
        updateButton.setMinWidth(140);
        removeButton.setMinWidth(140);

        //update
        updateProductBox.setVisible(false);
        inputProduct.setStyle("-fx-font-size: 15; -fx-font-weight: bold");
        gridPane.add(updateProductBox, 1, 2);
        updateProductButton.setMinWidth(140);
        backUpdateButton.setMinWidth(140);

        //delete / remove
        backRemoveButton.setMinWidth(140);
        removeProductButton.setMinWidth(140);
        removeProductBox.setVisible(false);
        gridPane.add(removeProductBox, 1, 2);
        removeLabel.setStyle("-fx-font-size: 15; -fx-font-weight: bold");


     // gridPane.setGridLinesVisible(true);

        //info
        manageProductListView.getItems().addAll(context.fetchProducts());
        manageProductListView.getSelectionModel().selectedItemProperty().addListener( (observable, oldValue, newValue) -> {
            if (newValue != null) {
                welcomeBox.setVisible(false);
                infoProductBox.setVisible(true);

                itemLabel.setText(newValue.getProductName());
                itemDescription.setText(newValue.getProductDesc());
                itemPrice.setText("Price: Rp. " + newValue.getProductPrice());
            }

            //update
            updateButton.setOnAction(event -> {
                updateProductBox.setVisible(true);
                productButton.setVisible(false);
                updateButton.setVisible(false);
                removeButton.setVisible(false);

                updateProductButton.setOnAction(e -> {
                    String price = productPrice.getText();
                    if (price == null ||
                            price.isEmpty()
                    ) {
                        alert(Alert.AlertType.ERROR, "Error",
                                "Failed to Update Product",
                                "All fields must be filled"
                        );
                        return;
                    }

                    long converted = Long.parseLong(price);
                    if (converted < 1){
                        alert(Alert.AlertType.ERROR, "Error",
                                "Failed to Update Product",
                                "Product price must be more than 0"
                        );
                        return;
                    }

                    Product product = manageProductListView.getSelectionModel().getSelectedItem();
                    product.setProductPrice(converted);
                    context.getProductController().updateProduct(product);
                    alert(Alert.AlertType.INFORMATION, "Success",
                    "Product Update Successfully!",
                    null
                    );

                    updateProductBox.setVisible(false);
                    productButton.setVisible(true);
                    updateButton.setVisible(true);
                    removeButton.setVisible(true);
                    productPrice.setText(null);
                    itemPrice.setText("Price: Rp." + converted);

                });

            });

            backUpdateButton.setOnAction(event -> {
                updateProductBox.setVisible(false);
                productButton.setVisible(true);
                updateButton.setVisible(true);
                removeButton.setVisible(true);
            });

            //delete / remove
            removeButton.setOnAction(event -> {
                removeProductBox.setVisible(true);
                productButton.setVisible(false);
                updateButton.setVisible(false);
                removeButton.setVisible(false);


            });

            removeProductButton.setOnAction(event -> {
                Product product = manageProductListView.getSelectionModel().getSelectedItem();
                context.getProductController().deleteProduct(product).ifPresentOrElse(p -> {
                    alert(Alert.AlertType.INFORMATION, "Success",
                            "Product Delete Successfully!",
                            null
                    );
                }, () -> {
                    alert(Alert.AlertType.ERROR, "Error",
                            "Failed to Remove Product",
                            "The remove request is not successful"
                    );
                });
                removeProductBox.setVisible(false);
                new ManageProduct(stage).start();

            });

            backRemoveButton.setOnAction(event -> {
                removeProductBox.setVisible(false);
                productButton.setVisible(true);
                updateButton.setVisible(true);
                removeButton.setVisible(true);
            });


        });

        //add
        addButton.setOnAction(event -> {
            addProductBox.setVisible(true);
            addButton.setVisible(false);
        });

        backAddButton.setOnAction(event -> {
            addProductBox.setVisible(false);
            addButton.setVisible(true);
        });

        addProductButton.setOnAction(event -> {
            String name = nameField.getText();
            String price = priceField.getText();
            String desc = textField.getText();

            //validasi-validasi
            if (name == null ||
            price == null ||
            desc == null ||
            name.isEmpty() ||
            price.isEmpty() ||
            desc.isEmpty()
            ) {
                alert(Alert.AlertType.ERROR, "Error",
                        "Failed to Add Product",
                        "All fields must be filled"
                );
                return;
            }

            try {
                long converted = Long.parseLong(price);
                if (converted < 1){
                    alert(Alert.AlertType.ERROR, "Error",
                            "Failed to Add Product",
                            "Product price must be more than 0"
                    );
                    return;
                }
                context.getProductController().addProduct(name, converted, desc);
                alert(Alert.AlertType.INFORMATION,
                        "Success",
                        "Product Added Successfully",
                        null
                );


            } catch (NumberFormatException e) {
                alert(Alert.AlertType.ERROR, "Error",
                        "Failed to Add Product",
                        "Product price must be in numbers"
                );


            }
            addProductBox.setVisible(false);
            new ManageProduct(stage).start();
        });

    }
}
