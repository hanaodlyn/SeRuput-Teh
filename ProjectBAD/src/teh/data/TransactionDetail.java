package teh.data;

public class TransactionDetail {

    private TransactionHeader transaction;
    private Product product;
    private Integer quantity;


    public TransactionDetail(TransactionHeader transaction, Product product, Integer quantity) {
        this.transaction = transaction;
        this.product = product;
        this.quantity = quantity;
    }

    public TransactionHeader getTransaction() {
        return transaction;
    }

    public void setTransaction(TransactionHeader transaction) {
        this.transaction = transaction;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return String.format("%dx %s (Rp.%d)", quantity, product.getProductName(), product.getProductPrice());
    }
}
