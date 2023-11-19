package dk.vv.order.creation.mediator.processors;

import dk.vv.common.data.transfer.objects.order.OrderDTO;
import dk.vv.common.data.transfer.objects.order.OrderLineDTO;
import dk.vv.order.creation.mediator.dtos.DeliveryDTO;
import dk.vv.order.creation.mediator.dtos.TicketDTO;
import dk.vv.order.creation.mediator.dtos.TicketLineDTO;
import dk.vv.order.creation.mediator.dtos.TicketResponseDTO;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.jboss.logging.Logger;

public class ConvertToTicketDTOProcessor implements Processor {

    private final Logger logger;

    public ConvertToTicketDTOProcessor(Logger logger){
        this.logger = logger;
    }


    @Override
    public void process(Exchange exchange) throws Exception {
        var order = exchange.getIn().getBody(OrderDTO.class);
        TicketDTO ticketDTO = new TicketDTO();

        ticketDTO.setComment(order.getComment());
        ticketDTO.setSupplierId(order.getSupplierId());

        for (OrderLineDTO orderLine : order.getOrderLines()) {
            TicketLineDTO ticketLineDTO = new TicketLineDTO();

            ticketLineDTO.setProductId(orderLine.getProductId());
            ticketLineDTO.setQuantity(orderLine.getQuantity());
            ticketLineDTO.setProductName(order.getComment());

            ticketDTO.addTicketLine(ticketLineDTO);
        }

        exchange.getIn().setBody(ticketDTO);
    }
}
