package dk.vv.order.creation.mediator;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "order.creation.mediator", namingStrategy = ConfigMapping.NamingStrategy.VERBATIM)
public interface Configuration {

    QueueConfig mq();

    MainConfig routes();

    String contextName();
    interface QueueConfig {
        String host();
        String username();
        String password();
        String vhost();
        boolean useSsl();
        int redeliveryBaseDelaySec();
        int redeliveryMultiplier();
        int redeliveryMaxDelaySec();
    }
    public interface MainConfig {
        interface InRoute{
            String in();
            String routeId();
        }

        InRoute creationEvent();




    }
}
