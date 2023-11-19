package dk.vv.order.creation.mediator.dtos;

import java.time.LocalDateTime;

public class DeliveryDTO {

    private int orderId;

    private LocalDateTime pickupTime;

    public DeliveryDTO() {
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public LocalDateTime getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(LocalDateTime pickupTime) {
        this.pickupTime = pickupTime;
    }
}
