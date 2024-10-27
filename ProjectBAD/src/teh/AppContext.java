package teh;

import teh.controller.CartController;
import teh.controller.ProductController;
import teh.controller.PurchaseController;
import teh.controller.UserController;
import teh.data.Cart;
import teh.data.Product;
import teh.data.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AppContext {

    private Connection connection;

    private final UserController userController;
    private final ProductController productController;
    private final CartController cartController;
    private final PurchaseController purchaseController;

    private User currentUser;

    public AppContext() throws SQLException  {
        this.getConnection();
        this.userController = new UserController(this);
        this.productController = new ProductController(this);
        this.cartController = new CartController(this);
        this.purchaseController = new PurchaseController(this);
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public ProductController getProductController() {
        return productController;
    }

    public UserController getUserController() {
        return userController;
    }

    public CartController getCartController() {
        return cartController;
    }

    public PurchaseController getPurchaseController() {
        return purchaseController;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public ArrayList<Product> fetchProducts() {
        return productController.fetchProducts();
    }

    public List<Cart> fetchCarts(User user) {
        return getCartController().fetchCart(user);
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/seruput_teh", "root", null);
        }
        return this.connection;
    }

}
