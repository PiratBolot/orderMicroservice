package itmo.remedictes.orderMicroservice.controller;

import itmo.remedictes.orderMicroservice.Service.OrderService;
import itmo.remedictes.orderMicroservice.domain.Order;
import itmo.remedictes.orderMicroservice.dto.ItemAdditionParametersDto;
import itmo.remedictes.orderMicroservice.dto.OrderDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.directory.InvalidAttributeValueException;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("orders")
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    private OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<Order>> getOrders() {
        log.info("Calling method getOrders");
        return new ResponseEntity<>(orderService.getAllOrders(), HttpStatus.OK);
    }

    @GetMapping("{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable Integer orderId) {
        log.info("Calling method getOrderById with orderID = {}", orderId);
        Optional<Order> orderOptional = orderService.getOrderById(orderId);
        return orderOptional
                .map(order -> new ResponseEntity<>(order, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("{orderId}/item")
    public ResponseEntity<OrderDto> addItemToOrder(@PathVariable String orderId,
                                                   @RequestBody ItemAdditionParametersDto itemAdditionParameters) {
        log.info("Calling method addItemToOrder with orderId = {} and item with name = {} and price = {}",
                orderId, itemAdditionParameters.getName(), itemAdditionParameters.getPrice());
        return new ResponseEntity<>(orderService.addItem(orderId, itemAdditionParameters), HttpStatus.OK);
    }

    @PutMapping("{orderId}/status/{status}")
    public ResponseEntity<OrderDto> changeOrderStatus(@PathVariable Integer orderId,
                                                      @PathVariable String status) {
        try {
            log.info("Calling method changeOrderStatus with orderID = {} and status = {}", orderId, status);
            return new ResponseEntity<>(orderService.changeStatus(orderId, status), HttpStatus.OK);
        } catch (InvalidParameterException e){
            log.error("No order with such ID exists");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            log.error("No status with such status name exists");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (InvalidAttributeValueException e) {
            log.error("Can't go to status = {}; check the state machine", status);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
