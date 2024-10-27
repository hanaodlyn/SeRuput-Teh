package teh.controller;

import teh.AppContext;
import teh.data.Product;
import teh.data.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class ProductController {

    private final AppContext context;

    public ProductController(AppContext context) {
        this.context = context;
    }

    public ArrayList<Product> fetchProducts() {
        try {
            Connection connection = context.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM product");

            ResultSet resultSet = statement.executeQuery();
            ArrayList<Product> products = new ArrayList<>();
            while (resultSet.next()) {
                products.add(toProduct(resultSet));
            }
            return products;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addProduct(String productName, Long productPrice, String productDesc) {
        try {
            String productID = generateID();
            Connection connection = context.getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO product (productID, product_name, product_price, product_des) VALUES (?, ?, ?, ?)");
            statement.setString(1, productID);
            statement.setString(2, productName);
            statement.setLong(3, productPrice);
            statement.setString(4, productDesc);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String generateID() {
        try {
            Connection connection = context.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT CONCAT('TE', LPAD(CAST(SUBSTRING(productID, 3) as UNSIGNED) + 1, 3, '0')) FROM product ORDER BY productID DESC LIMIT 1"
            );

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString(1);
            } else {
                return "TE001";
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Product> deleteProduct(Product product) {
        try {
            Connection connection = context.getConnection();
            PreparedStatement statement = connection.prepareStatement("DELETE FROM product WHERE productID = ?");
            statement.setString(1, product.getProductID());

            statement.executeUpdate();
            return Optional.of(product);
        } catch (SQLException e) {
            return Optional.empty();
        }
    }


    public Product fetchProduct(String productID) {
        try {
            Connection connection = context.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM product WHERE productID = ?");
            statement.setString(1, productID);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return toProduct(resultSet);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateProduct(Product product) {
        try {
            Connection connection = context.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE product SET product_name = ?, product_price = ?, product_des = ? WHERE productID = ?");
            statement.setString(1, product.getProductName());
            statement.setLong(2, product.getProductPrice());
            statement.setString(3, product.getProductDesc());
            statement.setString(4, product.getProductID());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Product toProduct(ResultSet rs) throws SQLException {
        return new Product(
                rs.getString("productID"),
                rs.getString("product_name"),
                rs.getLong("product_price"),
                rs.getString("product_des")
        );
    }


}
