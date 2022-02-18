package org.acme.services;

import io.quarkus.runtime.StartupEvent;
import io.vertx.mutiny.core.Vertx;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import java.util.Random;

@ApplicationScoped
public class Services {

    @ConfigProperty(name = "blue.http-port", defaultValue = "9000")
    int bluePort;

    @ConfigProperty(name = "blue.delay-in-ms", defaultValue = "100")
    int blueDelay;

    @ConfigProperty(name = "yellow.http-port", defaultValue = "9001")
    int yellowPort;

    @ConfigProperty(name = "yellow.delay-in-ms", defaultValue = "500")
    int yellowDelay;

    @ConfigProperty(name = "green.http-port", defaultValue = "9002")
    int greenPort;

    @ConfigProperty(name = "green.failure-ratio", defaultValue = "20")
    int greenFailureRatio;

    public void init(@Observes StartupEvent ev, Vertx vertx, Logger logger) {
        Random random = new Random();
        vertx.createHttpServer()
                .requestHandler(req -> {
                    vertx.setTimer(blueDelay, x -> {
                        req.response().endAndForget("🔵");
                    });
                })
                .listenAndAwait(bluePort);

        vertx.createHttpServer()
                .requestHandler(req -> {
                    vertx.setTimer(yellowDelay, x -> {
                        req.response().endAndForget("🟡");
                    });
                })
                .listenAndAwait(yellowPort);

        vertx.createHttpServer()
                .requestHandler(req -> {
                    vertx.setTimer(5, x -> {
                        if (random.nextInt(100) > (100 - greenFailureRatio)) {
                            req.response().endAndForget("❌");
                        } else {
                            req.response().endAndForget("🟢");
                        }
                    });
                })
                .listenAndAwait(greenPort);

        logger.infof("""
                Services Started:
                    - 🔵 Blue -> port: %d, delay: %dms
                    - 🟡 Yellow -> port: %d, delay: %dms
                    - 🟢 Green -> %d, failure ratio: %s
                """,bluePort, blueDelay, yellowPort, yellowDelay, greenPort, greenFailureRatio + "%");
    }


}
