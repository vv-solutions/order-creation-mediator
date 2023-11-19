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
        int port();
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
        interface InOutRoute{
            String in();
            String out();
            String routeId();
        }

        InRoute creationEvent();
        InRoute handleResponse();
        InOutRoute ticket();
        InOutRoute notification();
        InOutRoute updateStatus();
        InOutRoute delivery();


    }
}
