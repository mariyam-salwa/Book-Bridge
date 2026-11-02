package models;

/**
 * OOP CONCEPT: Encapsulation & Domain Modeling
 * Transaction tracks the direct handover, payment method, and completion status
 * between the student buyer and seller for a physical book or paid resource.
 */
public class Transaction {
    private int transactionId;
    private int materialId;
    private int buyerId;
    private int sellerId;
    private String paymentType; // "CASH_ON_HANDOVER", "FREE"
    private String status;      // "PENDING_HANDOVER", "COMPLETED", "FAILED"

    // Default Constructor
    public Transaction() {
    }

    // Parameterized Constructor
    public Transaction(int transactionId, int materialId, int buyerId, int sellerId,
                       String paymentType, String status) {
        this.transactionId = transactionId;
        this.materialId = materialId;
        this.buyerId = buyerId;
        this.sellerId = sellerId;
        this.paymentType = paymentType;
        this.status = status;
    }

    // Methods
    public void createTransaction() {
        this.status = "PENDING_HANDOVER";
        System.out.println("Transaction #" + this.transactionId + " initiated. Waiting for direct campus handover.");
    }

    public void completeTransaction() {
        this.status = "COMPLETED";
        System.out.println("Transaction #" + this.transactionId + " COMPLETED. Payment received in cash during direct handover.");
    }

    public void displayTransaction() {
        System.out.println("==========================================");
        System.out.println("🤝 TRANSACTION DETAILS");
        System.out.println("Transaction ID: " + this.transactionId);
        System.out.println("Material ID:    " + this.materialId);
        System.out.println("Buyer User ID:  " + this.buyerId);
        System.out.println("Seller User ID: " + this.sellerId);
        System.out.println("Payment Method: " + this.paymentType);
        System.out.println("Status:         " + this.status);
        System.out.println("==========================================");
    }

    // Getters and Setters
    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    public int getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(int buyerId) {
        this.buyerId = buyerId;
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
