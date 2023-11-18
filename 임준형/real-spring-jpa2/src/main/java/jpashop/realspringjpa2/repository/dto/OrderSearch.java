package jpashop.realspringjpa2.repository.dto;

import jpashop.realspringjpa2.domain.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderSearch {
    private String name;
    private OrderStatus orderStatus; // 주문 상태 [ORDER, CANCEL]
}
