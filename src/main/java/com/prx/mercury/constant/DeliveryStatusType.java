package com.prx.mercury.constant;

/// Enum for the status of the order
public enum DeliveryStatusType {
    OPENED,
    PENDING,
    IN_PROGRESS,
    SENT,
    DELIVERED,
    REJECTED,
    CANCELED,
    ABORTED,
    FAILED;

    @Override
    public String toString() {
        return this.name();
    }
}
