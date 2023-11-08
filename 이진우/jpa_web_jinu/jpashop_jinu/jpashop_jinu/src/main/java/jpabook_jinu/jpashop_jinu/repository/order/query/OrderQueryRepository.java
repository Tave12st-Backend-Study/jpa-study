package jpabook_jinu.jpashop_jinu.repository.order.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {
    private final EntityManager em;

    public List<OrderQueryDto> findOrderQueryDtos(){
        List<OrderQueryDto> result=findOrders();

        result.forEach(o->{
            List<OrderItemQueryDto> orderItems=findOrderItems(o.getOrderId());
            o.setOrderItems(orderItems);
        });
        return result;
    }

    private List<OrderQueryDto> findOrders(){
        return em.createQuery(
                "select new jpabook_jinu.jpashop_jinu.repository.order.query.OrderQueryDto(o.id,m.name,o.orderDate,o.orderStatus,d.address)"+
                        " from Order o"+
                        " join o.member m"+
                        " join o.delivery d",OrderQueryDto.class).getResultList();

    }

   private List<OrderItemQueryDto> findOrderItems(Long orderId){
       return em.createQuery(
               "select new jpabook_jinu.jpashop_jinu.repository.order.query.OrderItemQueryDto(oi.order.id,i.name,oi.orderPrice,oi.count)"+
                       " from OrderItem oi"+
                       " join oi.item i"+
                       " where oi.order.id=:orderId", OrderItemQueryDto.class).setParameter("orderId",orderId).getResultList();

   }

   public List<OrderQueryDto> findAllByDto_optimization(){
        List<OrderQueryDto> result=findOrders();
        Map<Long,List<OrderItemQueryDto>> orderItemMap=
                findOrderItemMap(toOrderIds(result));

        result.forEach(o->o.setOrderItems(orderItemMap.get(o.getOrderId())));

        return result;

   }
   private List<Long> toOrderIds(List<OrderQueryDto> result){
        return result.stream().map(o->o.getOrderId()).collect(Collectors.toList());
   }
   private Map<Long,List<OrderItemQueryDto>> findOrderItemMap(List<Long> orderIds){
        List<OrderItemQueryDto> orderItems=em.createQuery(
                "select new jpabook_jinu.jpashop_jinu.repository.order.query.OrderItemQueryDto(oi.order.id,i.name,oi.orderPrice,oi.count)"+
                        " from OrderItem oi"+
                        " join oi.item i" +
                        " where oi.order.id in :orderIds", OrderItemQueryDto.class).setParameter("orderIds",orderIds)
                .getResultList();

        return orderItems.stream().collect(Collectors.groupingBy(OrderItemQueryDto::getOrderId));

   }
}
