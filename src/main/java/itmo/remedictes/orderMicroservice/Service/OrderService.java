package itmo.remedictes.orderMicroservice.Service;

import itmo.remedictes.orderMicroservice.domain.Order;
import itmo.remedictes.orderMicroservice.domain.OrderItem;
import itmo.remedictes.orderMicroservice.domain.OrderStatus;
import itmo.remedictes.orderMicroservice.domain.SurogateKey;
import itmo.remedictes.orderMicroservice.dto.ItemAdditionParametersDto;
import itmo.remedictes.orderMicroservice.dto.OrderDto;
import itmo.remedictes.orderMicroservice.repos.OrderRepository;
import org.json.JSONObject;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.naming.directory.InvalidAttributeValueException;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class OrderService {

    private static final String WAREHOUSE_EXCHANGE = "warehouseCommandExchange";
    private static final String WAREHOUSE_KEY = "whcKey";
    private static final String EVENT_EXCHANGE = "statusExchange";

    private final RabbitTemplate rabbitTemplate;

    private OrderRepository orderRepository;

    @Autowired
    public OrderService(RabbitTemplate rabbitTemplate, OrderRepository orderRepository) {
        this.rabbitTemplate = rabbitTemplate;
        this.orderRepository = orderRepository;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(int orderId) {
        return orderRepository.findById(orderId);
    }

    @RabbitListener(queues = {"paymentStatus"})
    public void receiveMessageFromPayment(String message) throws InvalidAttributeValueException {
        JSONObject json = new JSONObject(message);
        Map<String, Object> map = json.toMap();
        String ordstat = (String) map.get("status");
        int orderID = (Integer) map.get("orderID");
        changeStatus(orderID, ordstat);
    }

    @RabbitListener(queues = {"warehouseCommand"})
    public void receiveMessageFromTopic1(String message) {
        JSONObject json = new JSONObject(message);
        System.out.println("Received topic 1 + message: " + json);
    }

    public OrderDto addItem(String orderId, ItemAdditionParametersDto itemPar) {
        if (orderId.equals("null")) {
            Order order = new Order(itemPar.getPrice(), itemPar.getAmount(), itemPar.getUsername(),
                    new OrderItem(new SurogateKey(orderRepository.count() + 1L, (long) itemPar.getId()), itemPar.getName(), itemPar.getAmount()));
            orderRepository.save(order);
            this.reserveItems(order.getOrderId(), itemPar.getId(), itemPar.getAmount());
            return new OrderDto(order.getOrderId());
        } else {
            Order updatedOrder = orderRepository.findById(Integer.parseInt(orderId)).orElse(new Order());
            updatedOrder.setTotalCost(updatedOrder.getTotalCost()
                    .add(itemPar.getPrice()
                            .multiply(BigDecimal.valueOf(itemPar.getAmount()))));
            updatedOrder.setTotalAmount(updatedOrder.getTotalAmount() + itemPar.getAmount());
            updatedOrder.setUsername(itemPar.getUsername());
            OrderItem orditem = new OrderItem(new SurogateKey(Long.parseLong(orderId), (long) itemPar.getId()), itemPar.getName(), itemPar.getAmount());
            orditem.setOrderInstance(updatedOrder);
            updatedOrder.getOrderItems().add(orditem);
            orderRepository.save(updatedOrder);
            this.reserveItems(Integer.parseInt(orderId), itemPar.getId(), itemPar.getAmount());
            return new OrderDto(updatedOrder.getOrderId());
        }
    }

    @Transactional
    public OrderDto changeStatus(Integer orderId, String status) throws InvalidAttributeValueException {
        Order updatedOrder = orderRepository.findById(orderId)
                .orElseThrow(InvalidParameterException::new);
        if (updatedOrder.getStatus().nextState().contains(OrderStatus.valueOf(status.toUpperCase()))) {
            updatedOrder.setStatus(OrderStatus.valueOf(status.toUpperCase()));
        } else {
            throw new InvalidAttributeValueException();
        }
        orderRepository.save(updatedOrder);

        for (OrderItem orderItem : updatedOrder.getOrderItems()) {
            JSONObject jo = new JSONObject();
            jo.put("status", updatedOrder.getStatus());
            jo.put("orderID", orderId);
            jo.put("id", orderItem.getOrderItemId().getItemID());
            jo.put("amount", orderItem.getAmount());
            rabbitTemplate.convertAndSend(EVENT_EXCHANGE, "", jo.toString());
        }
        return new OrderDto(orderId, updatedOrder.getStatus());
    }

    public void reserveItems(int orderID, int itemID, int amount) {
        JSONObject jo = new JSONObject();
        jo.put("orderID", orderID);
        jo.put("id", itemID);
        jo.put("amount", amount);
        rabbitTemplate.convertAndSend(WAREHOUSE_EXCHANGE, WAREHOUSE_KEY, jo.toString());
    }
}