package itmo.remedictes.orderMicroservice.controller;

import itmo.remedictes.orderMicroservice.domain.Order;
import itmo.remedictes.orderMicroservice.domain.OrderStatus;
import itmo.remedictes.orderMicroservice.dto.ItemAdditionParametersDto;
import itmo.remedictes.orderMicroservice.dto.UserDetailsDto;
import itmo.remedictes.orderMicroservice.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("orders")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    private final OrderRepository orderRepository;

    @Autowired
    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return new ResponseEntity<>(orderRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Optional<Order> customer = orderRepository.findById(id);
        return customer
                .map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("{id}/item")
    public ResponseEntity<Long> addItemToOrder(@PathVariable Long id,
            @Valid @RequestBody ItemAdditionParametersDto itemDto) {
        if (orderRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
//        orderRepository.save();
        /*logger.info("Created new customer: { id: " + itemDto.getId() +
                ", customerName: " + customer.getCustomerName() + " }");*/
        return new ResponseEntity<>(itemDto.getId(), HttpStatus.CREATED);
    }

    @PutMapping("{id}/payment")
    public ResponseEntity<Order> performPayment(@PathVariable Long id,
                                                @Valid @RequestBody UserDetailsDto userDetailsDto) {
        return new ResponseEntity<>(orderRepository.getOne(id), HttpStatus.OK);
    }

    @PutMapping("{id}/status/{status}")
    public ResponseEntity<Order> changeOrderStatus(@PathVariable Long id,
                                                @PathVariable OrderStatus newStatus) {
        return new ResponseEntity<>(orderRepository.getOne(id), HttpStatus.OK);
    }






}
