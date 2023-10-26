package jpashop.realspringjpa1.domain.repository.dto;

import jpashop.realspringjpa1.domain.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderSearch {
    private String username;
    private OrderStatus orderStatus; // 주문 상태 [ORDER, CANCEL]
}
