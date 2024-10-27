package teh.data;

import java.util.ArrayList;

public class TransactionHeader {

    private String transactionID;
    private User user;
    private ArrayList<TransactionDetail> transactionDetails;

    public TransactionHeader(String transactionID, User user) {
        this.transactionID = transactionID;
        this.user = user;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ArrayList<TransactionDetail> getTransactionDetails() {
        return transactionDetails;
    }

    public void setTransactionDetails(ArrayList<TransactionDetail> transactionDetails) {
        this.transactionDetails = transactionDetails;
    }

    @Override
    public String toString() {
        return transactionID;
    }

}
