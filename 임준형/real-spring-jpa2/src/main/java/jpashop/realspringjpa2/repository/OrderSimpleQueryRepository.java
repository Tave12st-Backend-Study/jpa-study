package jpashop.realspringjpa2.repository;

import java.util.List;
import javax.persistence.EntityManager;
import jpashop.realspringjpa2.api.dto.OrderSimpleQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {

    private final EntityManager em;

    /**
     * 위와 아래중 둘의 트레이드 오프가 있음
     * dto로 조회 재사용성 x
     * 아주 살짝의 성능 이점이 있음
     * 실시간으로 엄청 많은 트래픽이라면 고민할 필요가 있음
     * 따로 QueryRepository를 만들자.
     */
    public List<OrderSimpleQueryDto> findOrderDtos() {
        List<OrderSimpleQueryDto> resultList = em.createQuery(
                "select new jpashop.realspringjpa2.api.dto.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address) "
                        + "from Order o" +
                        " join o.member m" +
                        " join o.delivery d", OrderSimpleQueryDto.class
        ).getResultList();
        return resultList;
    }

}
