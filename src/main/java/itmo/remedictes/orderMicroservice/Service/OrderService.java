package itmo.remedictes.orderMicroservice.Service;

import itmo.remedictes.orderMicroservice.domain.*;
import itmo.remedictes.orderMicroservice.dto.ItemAdditionParametersDto;
import itmo.remedictes.orderMicroservice.dto.OrderDto;
import itmo.remedictes.orderMicroservice.repos.ItemRepository;
import itmo.remedictes.orderMicroservice.repos.OrderItemRepository;
import itmo.remedictes.orderMicroservice.repos.OrderRepository;
import org.json.JSONObject;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.naming.directory.InvalidAttributeValueException;
import javax.transaction.Transactional;
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

    private ItemRepository itemRepository;

    private OrderItemRepository orderItemRepository;

    @Autowired
    public OrderService(RabbitTemplate rabbitTemplate, OrderRepository orderRepository,
                        ItemRepository itemRepository, OrderItemRepository orderItemRepository) {
        this.rabbitTemplate = rabbitTemplate;
        this.orderRepository = orderRepository;
        this.itemRepository = itemRepository;
        this.orderItemRepository = orderItemRepository;
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
                    new OrderItem(new Item(itemPar.getId(), itemPar.getName(), itemPar.getPrice()), itemPar.getAmount()));
            orderRepository.save(order);
            this.reserveItems(order.getOrderID(), itemPar.getId(), itemPar.getAmount());
            return new OrderDto(order.getOrderID());
        } else {
            Item item = new Item(itemPar.getId(), itemPar.getName(), itemPar.getPrice());
            itemRepository.save(item);
            Order ordertoupdate = orderRepository.findById(Integer.parseInt(orderId)).orElse(new Order());
            ordertoupdate.setTotalCost(ordertoupdate.getTotalCost().add(item.getPrice()));
            ordertoupdate.setTotalAmount(ordertoupdate.getTotalAmount() + itemPar.getAmount());
            OrderItem orditem = new OrderItem(ordertoupdate, item, itemPar.getAmount());
            OrderItemKey oikey = new OrderItemKey(ordertoupdate.getOrderID(), item.getItemId());
            if (orderItemRepository.existsById(oikey)) {
                OrderItem oitem = orderItemRepository.findById(oikey).orElse(new OrderItem());
                oitem.setAmount(oitem.getAmount() + itemPar.getAmount());
                orderItemRepository.save(oitem);
            } else {
                ordertoupdate.getOrderItems().add(orditem);
            }
            orderRepository.save(ordertoupdate);
            this.reserveItems(Integer.parseInt(orderId), itemPar.getId(), itemPar.getAmount());
            return new OrderDto(ordertoupdate.getOrderID());
        }
    }

    @Transactional
    public OrderDto changeStatus(Integer orderID, String status) throws InvalidAttributeValueException {
        Order ordertoupdate = orderRepository.findById(orderID).orElseThrow(InvalidParameterException::new);
        if (ordertoupdate.getStatus().nextState().contains(OrderStatus.valueOf(status.toUpperCase()))) {
            ordertoupdate.setStatus(OrderStatus.valueOf(status.toUpperCase()));
        } else throw new InvalidAttributeValueException();
        orderRepository.save(ordertoupdate);
        JSONObject jo = new JSONObject();
        jo.put("status", ordertoupdate.getStatus());
        jo.put("orderID", orderID);
        rabbitTemplate.convertAndSend(EVENT_EXCHANGE, "", jo.toString());
        return new OrderDto(orderID, ordertoupdate.getStatus());
    }

    public void reserveItems(int orderID, int itemID, int amount) {
        JSONObject jo = new JSONObject();
        jo.put("orderID", orderID);
        jo.put("id", itemID);
        jo.put("amount", amount);
        rabbitTemplate.convertAndSend(WAREHOUSE_EXCHANGE, WAREHOUSE_KEY, jo.toString());
    }
}