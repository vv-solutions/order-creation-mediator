package dk.vv.order.creation.mediator.processors;

import dk.vv.common.data.transfer.objects.Notification.NotificationDTO;
import dk.vv.common.data.transfer.objects.kitchen.TicketResponseDTO;
import dk.vv.common.data.transfer.objects.order.OrderDTO;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.jboss.logging.Logger;

public class ConvertToNotificationDTOProcessor implements Processor {

    private final Logger logger;

    public ConvertToNotificationDTOProcessor(Logger logger){
        this.logger = logger;
    }


    @Override
    public void process(Exchange exchange) throws Exception {
        NotificationDTO notificationDTO = new NotificationDTO();

        if(exchange.getIn().getBody().getClass().equals(OrderDTO.class)){
            var order = exchange.getIn().getBody(OrderDTO.class);
            notificationDTO.setCustomerId(order.getCustomerId());
            notificationDTO.setOrderId(order.getId());
            notificationDTO.setMessage("We have received your order \n");

        } else if (exchange.getIn().getBody().getClass().equals(TicketResponseDTO.class)){
            var ticketResponse = exchange.getIn().getBody(TicketResponseDTO.class);

            notificationDTO.setOrderId(ticketResponse.getOrderId());
            String message = "";

            if(ticketResponse.isAccepted()){
                message = "Your order has been accepted by the restaurant and will be ready soon! \n";
            } else {
                message ="We regret to inform you that your order has been cancelled by the restaurant \n";
            }

            notificationDTO.setMessage(message);

        } else {
            logger.errorf("err: cannot convert [%s] to NotificationDTO",exchange.getIn().getBody().getClass());
        }

        exchange.getIn().setBody(notificationDTO);
    }
}
