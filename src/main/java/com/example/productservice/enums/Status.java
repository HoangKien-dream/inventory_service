package com.example.productservice.enums;

public enum Status {
    ACTIVE,DELETE;
    public enum TransactionStatus {
        SUCCESS, FAIL
    }

    public enum PaymentStatus {
        PENDING, NOT_ENOUGH_BALANCE, DONE, REFUND, REFUNDED
    }

    public enum InventoryStatus {
        PENDING, OUT_OF_STOCK, DONE, RETURN, RETURNED
    }

    public enum OrderStatus {
        PENDING, PROCESSING, DONE, REFUND, CANCEL
    }
}
