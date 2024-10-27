package teh.controller;

import teh.AppContext;
import teh.data.Cart;
import teh.data.TransactionDetail;
import teh.data.TransactionHeader;
import teh.data.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PurchaseController {

    private final AppContext context;

    public PurchaseController(AppContext context) {
        this.context = context;
    }

    public List<TransactionHeader> purchaseHistory(User user) {
        try {
            Connection connection = context.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM transaction_header WHERE userID = ?");
            preparedStatement.setString(1, user.getUserId());

            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<TransactionHeader> transactionHeaders = new ArrayList<>();
            while (resultSet.next()) {
                transactionHeaders.add(toTransactionHeader(user, resultSet));
            }
            return transactionHeaders;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public TransactionHeader purchase(User user, List<Cart> carts) {
        try {
            String transactionID = generateID();

            Connection connection = context.getConnection();
            connection.setAutoCommit(false);

            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO transaction_header (transactionID, userID) VALUES (?, ?)");

            preparedStatement.setString(1, transactionID);
            preparedStatement.setString(2, user.getUserId());

            preparedStatement.executeUpdate();

            TransactionHeader transactionHeader = new TransactionHeader(transactionID, user);
            transactionHeader.setTransactionDetails(new ArrayList<>());
            for (Cart cart : carts) {
                PreparedStatement preparedStatement1 = connection.prepareStatement("INSERT INTO transaction_detail (transactionID, productID, quantity) VALUES (?, ?, ?)");
                preparedStatement1.setString(1, transactionID);
                preparedStatement1.setString(2, cart.getProduct().getProductID());
                preparedStatement1.setInt(3, cart.getQuantity());

                preparedStatement1.executeUpdate();

                transactionHeader.getTransactionDetails()
                        .add(new TransactionDetail(transactionHeader, cart.getProduct(), cart.getQuantity()));
            }

            context.getCartController().removeCart(user);

            connection.commit();
            connection.setAutoCommit(true);

            return transactionHeader;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<TransactionDetail> purchaseHistory(TransactionHeader transactionHeader) {
        try {
            Connection connection = context.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM transaction_detail WHERE transactionID = ?");
            preparedStatement.setString(1, transactionHeader.getTransactionID());

            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<TransactionDetail> transactionDetails = new ArrayList<>();
            while (resultSet.next()) {
                transactionDetails.add(toTransactionDetail(transactionHeader, resultSet));
            }
            return transactionDetails;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String generateID() {
        try {
            Connection connection = context.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT CONCAT('TR', LPAD(CAST(SUBSTRING(transactionID, 3) as UNSIGNED) + 1, 3, '0')) FROM transaction_header ORDER BY transactionID DESC LIMIT 1"
            );

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString(1);
            } else {
                return "TR001";
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public TransactionHeader toTransactionHeader(User user, ResultSet rs) throws SQLException {
        TransactionHeader transactionHeader =  new TransactionHeader(
                rs.getString("transactionID"),
                user
        );
        transactionHeader.setTransactionDetails(purchaseHistory(transactionHeader));

        return transactionHeader;
    }

    public TransactionDetail toTransactionDetail(TransactionHeader transactionHeader, ResultSet rs) throws SQLException {
        return new TransactionDetail(
                transactionHeader,
                context.getProductController().fetchProduct(rs.getString("productID")),
                rs.getInt("quantity")
        );
    }

}
