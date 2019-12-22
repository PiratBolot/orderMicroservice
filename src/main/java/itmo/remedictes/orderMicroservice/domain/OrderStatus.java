package itmo.remedictes.orderMicroservice.domain;

import java.util.EnumSet;

public enum OrderStatus {
    COLLECTING {
        @Override
        public EnumSet<OrderStatus> nextState(){
            return EnumSet.of(OrderStatus.PAYED, OrderStatus.FAILED);
        }
    },
    PAYED {
        @Override
        public EnumSet<OrderStatus> nextState(){
            return EnumSet.of(OrderStatus.SHIPPING, OrderStatus.CANCELLED);
        }
    },
    SHIPPING {
        @Override
        public EnumSet<OrderStatus> nextState(){
            return EnumSet.of(OrderStatus.COMPLETE);
        }
    },
    COMPLETE {
        @Override
        public EnumSet<OrderStatus> nextState() {
            return EnumSet.of(this);
        }
    },
    FAILED {
        @Override
        public EnumSet<OrderStatus> nextState() {
            return EnumSet.of(this);
        }
    },
    CANCELLED {
        @Override
        public EnumSet<OrderStatus> nextState() {
            return EnumSet.of(this);
        }
    };

    public abstract EnumSet<OrderStatus> nextState();
}
