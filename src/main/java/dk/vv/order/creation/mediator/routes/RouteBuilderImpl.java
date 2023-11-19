package dk.vv.order.creation.mediator.routes;

import dk.vv.common.data.transfer.objects.order.OrderDTO;
import dk.vv.order.creation.mediator.Configuration;
import dk.vv.order.creation.mediator.dtos.NotificationDTO;
import dk.vv.order.creation.mediator.dtos.TicketResponseDTO;
import jakarta.inject.Inject;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.apache.camel.quarkus.core.FastCamelContext;
import org.jboss.logging.Logger;

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
        from(configuration.routes().creationEvent().in())
                .routeId(configuration.routes().creationEvent().routeId())
                .process(exchange -> logger.info("recv: Started processing order creation event from queue"))

                .unmarshal().json(OrderDTO.class)

                // send ticket to kitchen
                // send order received notification queue
                .multicast()
                    .to(configuration.routes().kitchen().in())
                    .to(configuration.routes().notification().in())
                .end()


                ;



        // Handle ticket accept / deny
        from(configuration.routes().handleResponse().in()).routeId(configuration.routes().handleResponse().routeId())
                .process(exchange -> logger.info("recv: Started processing order accept/deny event from queue"))

                .unmarshal().json(TicketResponseDTO.class)

                .choice().when(e-> e.getIn().getBody(TicketResponseDTO.class).isAccepted())
                    .multicast()
                        .to(configuration.routes().delivery().in())
                        .to(configuration.routes().updateStatus().in())
                        .to(configuration.routes().notification().in())
                    .end()
                .otherwise()
                    .multicast()
                        .to(configuration.routes().updateStatus().in())
                        .to(configuration.routes().notification().in())
                    .end()
                .end()


                // accept:
                    // send notification queue
                    // send delivery info on delivery queue
                    // send update order status on queue

                // deny:
                    // send notification queue
                    // send update order status on queue
                ;

        from(configuration.routes().notification().in()).routeId(configuration.routes().notification().routeId())

                .process(convertToNotificationDTOProcessor)
                .marshal().json()
                .to(onfiguration.routes().notification().out())

                ;

        from(configuration.routes().kitchen().in()).routeId(configuration.routes().kitchen().routeId())

                .process(convertToTicketDTOProcessor)
                .marshal().json()
                .to(configuration.routes().kitchen().out())

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
