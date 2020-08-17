package com.googe.ssadm.sc.service;

import com.googe.ssadm.sc.entity.Order;
import com.googe.ssadm.sc.repository.OrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepo orderRepo;

    @Autowired
    public OrderService(OrderRepo orderRepo) {
        this.orderRepo = orderRepo;
    }

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(OrderService.class);

    public List<Order> findAll(){
        log.info("OrderService: findAll");
        return orderRepo.findAll();
    }


    public Page<Order> findAllPaginated(Pageable pageable){
        log.info("OrderService: findAll");
        return orderRepo.findAll(pageable);
    }


    public Optional<Order> findById(Long id){
        log.info("OrderService: findOrderById, {}", id);
        return orderRepo.findOrderById(id);
    }

    public Page<Order> findByIdPaged(Pageable pageable, Long id){
        log.info("OrderService: findOrderById, {}", id);
        return orderRepo.findAllById(pageable, id);
    }

    public Optional<Order> findBySerialNo(String serialNo){
        log.info("OrderService: findOrderBySerialNo, {}", serialNo);
        return orderRepo.findOrderBySerialNo(serialNo);
    }

    public List<Order> findAllBySerialNo(String serial){
        log.info("OrderService: findAllBySerialNo, {}", serial);
        return orderRepo.findAllBySerialNo(serial);
    }

    public Order saveOrder(Order order){
        log.info("OrderService: saveOrUpdate, {}", order.getId());
        return orderRepo.save(order);
    }

}
