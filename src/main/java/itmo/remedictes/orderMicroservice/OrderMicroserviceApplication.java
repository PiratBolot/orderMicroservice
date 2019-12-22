package itmo.remedictes.orderMicroservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OrderMicroserviceApplication {
    private static final Logger log = LoggerFactory.getLogger(OrderMicroserviceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(OrderMicroserviceApplication.class, args);
        log.info("Order Microservice started");
    }
}
