package dk.vv.order.creation.mediator.dtos;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class TicketDTO {

    private int id;
    private int orderId;
    private int supplierId;
    private String comment;

    private LocalDateTime pickupTime;

    @JsonManagedReference
    Set<TicketLineDTO> ticketLines = new HashSet<>();

    public TicketDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(LocalDateTime pickupTime) {
        this.pickupTime = pickupTime;
    }

    public Set<TicketLineDTO> getTicketLines() {
        return ticketLines;
    }

    public void setTicketLines(Set<TicketLineDTO> ticketLines) {
        this.ticketLines = ticketLines;
    }
    public void addTicketLine(TicketLineDTO ticketLineDTO){
        this.ticketLines.add(ticketLineDTO);

        if(ticketLineDTO.getTicketDTO() != this){
            ticketLineDTO.setTicketDTO(this);
        }
    }
}
