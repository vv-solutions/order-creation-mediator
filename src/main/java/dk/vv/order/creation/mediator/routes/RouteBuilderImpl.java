package dk.vv.order.creation.mediator.routes;

import dk.vv.common.data.transfer.objects.order.OrderDTO;
import dk.vv.order.creation.mediator.Configuration;
import dk.vv.order.creation.mediator.Constants;
import dk.vv.order.creation.mediator.dtos.NotificationDTO;
import dk.vv.order.creation.mediator.dtos.TicketResponseDTO;
import jakarta.inject.Inject;
import org.apache.camel.CamelContext;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.apache.camel.component.rabbitmq.RabbitMQConstants;
import org.apache.camel.quarkus.core.FastCamelContext;
import org.jboss.logging.Logger;

@ApplicationScoped

public class RouteBuilderImpl extends EndpointRouteBuilder {

    private final Logger logger;

    private final Configuration configuration;

    private final CamelContext camelContext;

    @Inject
    public RouteBuilderImpl(Logger logger, Configuration configuration, CamelContext camelContext) {
        this.logger = logger;
        this.configuration = configuration;
        this.camelContext = camelContext;
    }


    @Override
    public void configure() throws Exception {

        ((FastCamelContext)this.camelContext).setName(configuration.contextName());

        onException(Exception.class)
                .process(exchange -> {
                    logger.error(String.format("recv: failed: %s-%s",exchange.getExchangeId(), exchange.getMessage().getBody()));
                })
                .handled(false)
                .end();


        // Handle order creation event
        from(configuration.routes().creationEvent().in()).routeId(configuration.routes().creationEvent().routeId())
                .process(exchange -> logger.info("recv: Started processing order creation event from queue"))
                .unmarshal().json(OrderDTO.class)
                .multicast()
                    // send ticket to kitchen
                    .to(configuration.routes().ticket().in())
                     // send order received notification queue
                    .to(configuration.routes().notification().in())
                .end()
        ;


        // Handle ticket accept / deny
        from(configuration.routes().handleResponse().in()).routeId(configuration.routes().handleResponse().routeId())
                .process(exchange -> logger.info("recv: Started processing order accept/deny event from queue"))
                .unmarshal().json(TicketResponseDTO.class)

                // accepted
                .choice().when(e-> e.getIn().getBody(TicketResponseDTO.class).isAccepted())
                    .multicast()
                        // send delivery information
                        .to(configuration.routes().delivery().in())
                        // send order status update
                        .to(configuration.routes().updateStatus().in())
                        // send information on notification queue
                        .to(configuration.routes().notification().in())
                    .end()
                .endChoice()

                // denied
                .otherwise()
                    .multicast()
                        .to(configuration.routes().updateStatus().in())
                        .to(configuration.routes().notification().in())
                    .end()
                .end()
        ;
//
        from(configuration.routes().notification().in()).routeId(configuration.routes().notification().routeId())
                .process(convertToNotificationDTOProcessor)
                .marshal().json()
                .to(configuration.routes().notification().out())
        ;

        from(configuration.routes().ticket().in()).routeId(configuration.routes().ticket().routeId())
                .process(convertToTicketDTOProcessor)
                .marshal().json()
                // set routing key
                .process( e-> {
                    e.getIn().setHeader(RabbitMQConstants.ROUTING_KEY,Constants.TICKET_CREATION_ROUTING_KEY);
                })

                .to(ExchangePattern.InOnly,configuration.routes().ticket().out())
        ;

        from(configuration.routes().delivery().in()).routeId(configuration.routes().delivery().routeId())
                .process(convertToDeliveryDTOProcessor)
                .marshal().json()
                .to(configuration.routes().delivery().out())
        ;


        from(configuration.routes().updateStatus().in()).routeId(configuration.routes().updateStatus().routeId())
                .process(convertToOrderDTOProcessor)
                .marshal().json()
                .to(configuration.routes().delivery().out())

        ;

    }
}
