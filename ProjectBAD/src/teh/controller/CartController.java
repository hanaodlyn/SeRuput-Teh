package teh.controller;

import teh.AppContext;
import teh.data.Cart;
import teh.data.Product;
import teh.data.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CartController {

    private final AppContext context;

    public CartController(AppContext context) {
        this.context = context;
    }

    public ArrayList<Cart> fetchCart(User user) {
        try {
            Connection connection = context.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM cart WHERE UserID = ?");

            preparedStatement.setString(1, user.getUserId());
            ResultSet rs = preparedStatement.executeQuery();

            ArrayList<Cart> cart = new ArrayList<>();
            while (rs.next()) {
                cart.add(toCart(rs));
            }
            return cart;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addToCart(User user, Product product, int quantity) {
        try {
            Connection connection = context.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO cart (UserID, ProductID, quantity) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE quantity = ?"
            );

            preparedStatement.setString(1, user.getUserId());
            preparedStatement.setString(2, product.getProductID());
            preparedStatement.setInt(3, quantity);
            preparedStatement.setInt(4, quantity);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeCart(User user) {
        try {
            Connection connection = context.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM cart WHERE UserID = ?"
            );

            preparedStatement.setString(1, user.getUserId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeCart(Cart cart) {
        try {
            Connection connection = context.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM cart WHERE UserID = ? AND ProductID = ?"
            );

            preparedStatement.setString(1, cart.getUser().getUserId());
            preparedStatement.setString(2, cart.getProduct().getProductID());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Cart toCart(ResultSet rs) throws SQLException {
        return new Cart(
                context.getProductController().fetchProduct(rs.getString("ProductID")),
                context.getUserController().resolveUser(rs.getString("UserID")),
                rs.getInt("quantity")
        );
    }

}
