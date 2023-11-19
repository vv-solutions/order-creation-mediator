package dk.vv.order.creation.mediator;

import com.rabbitmq.client.ConnectionFactory;
import dk.vv.order.creation.mediator.processors.ConvertToNotificationDTOProcessor;
import io.quarkus.arc.Unremovable;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
@ApplicationScoped
public class Producers {

    @Inject
    Configuration configuration;
    @Produces
    @Unremovable
    ConnectionFactory rabbitConnectionFactory(Logger logger) throws NoSuchAlgorithmException, KeyManagementException {
        logger.info("Configure ConnectionFactory for " + configuration.mq().host());

        return new ConnectionFactory() {{
            setHost(configuration.mq().host());
            setPort(configuration.mq().port());
            setUsername(configuration.mq().username());
            setPassword(configuration.mq().password());
            setVirtualHost(configuration.mq().vhost());

            if(configuration.mq().useSsl() == true) {
                useSslProtocol();
            }
        }};
    }
    @Produces
    ConvertToNotificationDTOProcessor getConvertToNotificationDTOProcessor (){
        return new ConvertToNotificationDTOProcessor(logger);
    }
}
