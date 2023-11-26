package dk.vv.order.creation.mediator.processors;

import dk.vv.common.data.transfer.objects.kitchen.TicketResponseDTO;
import dk.vv.common.data.transfer.objects.order.OrderDTO;
import dk.vv.common.enums.order.OrderStatus;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.jboss.logging.Logger;

public class ConvertToOrderDTOProcessor implements Processor {

    private final Logger logger;

    public ConvertToOrderDTOProcessor(Logger logger){
        this.logger = logger;
    }


    @Override
    public void process(Exchange exchange) throws Exception {
        var ticketResponse = exchange.getIn().getBody(TicketResponseDTO.class);
        OrderDTO orderDTO = new OrderDTO();

        if (ticketResponse.isAccepted()){
            orderDTO.setStatusId(OrderStatus.ACCEPTED.value());
        } else {
            orderDTO.setStatusId(OrderStatus.DENIED.value());
        }

        orderDTO.setId(ticketResponse.getOrderId());
        exchange.getIn().setBody(orderDTO);
    }
}
