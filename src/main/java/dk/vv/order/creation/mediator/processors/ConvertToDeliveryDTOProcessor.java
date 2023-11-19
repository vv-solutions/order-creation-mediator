package dk.vv.order.creation.mediator.processors;

import dk.vv.common.data.transfer.objects.order.OrderDTO;
import dk.vv.order.creation.mediator.dtos.DeliveryDTO;
import dk.vv.order.creation.mediator.dtos.NotificationDTO;
import dk.vv.order.creation.mediator.dtos.TicketResponseDTO;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.jboss.logging.Logger;

public class ConvertToDeliveryDTOProcessor implements Processor {

    private final Logger logger;

    public ConvertToDeliveryDTOProcessor(Logger logger){
        this.logger = logger;
    }


    @Override
    public void process(Exchange exchange) throws Exception {
        var ticketResponse = exchange.getIn().getBody(TicketResponseDTO.class);

        DeliveryDTO deliveryDTO = new DeliveryDTO();

        deliveryDTO.setOrderId(ticketResponse.getOrderId());
        deliveryDTO.setPickupTime(ticketResponse.getPickupTime());

        exchange.getIn().setBody(deliveryDTO);

    }
}
