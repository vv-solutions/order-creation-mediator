package dk.vv.order.creation.mediator.dtos;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDateTime;

public class TicketResponseDTO {

    private int orderId;


    private boolean accepted;

    private LocalDateTime pickupTime;

    private String comment;



    public TicketResponseDTO() {
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public LocalDateTime getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(LocalDateTime pickupTime) {
        this.pickupTime = pickupTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
