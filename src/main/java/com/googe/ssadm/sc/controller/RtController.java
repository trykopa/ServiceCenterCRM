package com.googe.ssadm.sc.controller;

import com.googe.ssadm.sc.dto.RepairTableDTO;
import com.googe.ssadm.sc.entity.Order;
import com.googe.ssadm.sc.entity.Part;
import com.googe.ssadm.sc.entity.RepairTable;
import com.googe.ssadm.sc.mappers.RepairTableMapper;
import com.googe.ssadm.sc.service.OrderService;
import com.googe.ssadm.sc.service.PartService;
import com.googe.ssadm.sc.service.RepTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;


@Controller
@RequestMapping("/table")
public class RtController {

    private final RepTableService repTableService;
    private final RepairTableMapper repairTableMapper;
    private final PartService partService;
    private final OrderService orderService;

    @Autowired
    public RtController(RepTableService repTableService ,
                        RepairTableMapper repairTableMapper ,
                        PartService partService ,
                        OrderService orderService) {
        this.repTableService = repTableService;
        this.repairTableMapper = repairTableMapper;
        this.partService = partService;
        this.orderService = orderService;
    }

    @GetMapping(path = "/edit/{id}")
    public String editTable(@PathVariable("id") Long id , Model model) {
        RepairTable repairTable = repTableService.findByOrderId(id).orElse(new RepairTable());

        RepairTableDTO repairTableDTO = repairTableMapper.repTableToRepTableDTO(repairTable);
        List<Part> lp = repairTable.getPartList();
        if (lp != null) {
            BigDecimal repaircost = lp.stream()
                    .map(Part::getRecommendedPrice)
                    .reduce(BigDecimal.ZERO , BigDecimal::add);
            BigDecimal discount = repaircost
                    .divide(OrderController.ONE_HUNDRED , 3 , RoundingMode.CEILING)
                    .multiply(repairTable.getDiscount());
            BigDecimal finalCost = repaircost.subtract(discount).setScale(2 , BigDecimal.ROUND_HALF_UP);
            model.addAttribute("repaircost" , finalCost);
        }
        repairTableDTO.setOrderId(id);
        model.addAttribute("table" , repairTableDTO);
        model.addAttribute("parts" , repairTable.getPartList());
        return "rtable";
    }

    @PostMapping(path = "/edit")
    public String addTable(RepairTableDTO repairTableDTO , Model model , Authentication authentication) {
        RepairTable rt;
        List<Part> lp = new ArrayList<>();
        if (repTableService.findByOrderId(repairTableDTO.getOrderId()).isPresent()) {
            rt = repTableService.findByOrderId(repairTableDTO.getOrderId()).get();
        } else {
            rt = repairTableMapper.repTableDtotoRepTable(repairTableDTO);
        }

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        boolean isAdmin = authorities.stream().anyMatch(r -> r.toString().equals("ROLE_ADMIN"));

        if (isAdmin || !rt.isTableClosed()) {
            if (repairTableDTO.getDiscount() != null) {
                rt.setDiscount(repairTableDTO.getDiscount());
            } else {
                rt.setDiscount(BigDecimal.ZERO);
            }
            if (rt.getPartList() != null) {
                lp = rt.getPartList();
            }
            //i dont know where this comma is coming from
            if (repairTableDTO.getPartOrWork().equals(" , ")) {
                repairTableDTO.setPartOrWork(null);
            }

            if (repairTableDTO.getPartOrWork() != null) {
                Part part = partService.findById(Long.parseLong(repairTableDTO.getPartOrWork())).orElse(null);
                if (part != null && !part.isPartOrWork()) { //if not work
                    part.setOnStock(false); // to remove part from stock
                }
                lp.add(part);
            }
            rt.setPartList(lp);
            if (lp.size() > 0) {
                BigDecimal repaircost = lp.stream()
                        .map(Part::getRecommendedPrice)
                        .reduce(BigDecimal.ZERO , BigDecimal::add);
                BigDecimal discount = repaircost
                        .divide(OrderController.ONE_HUNDRED , 3 , RoundingMode.CEILING)
                        .multiply(rt.getDiscount());
                BigDecimal finalCost = repaircost.subtract(discount).setScale(2 , BigDecimal.ROUND_HALF_UP);
                model.addAttribute("repaircost" , finalCost);
            }
            Order order = orderService.findById(repairTableDTO.getOrderId()).orElse(new Order());
            rt.setOrder(order);
            order.setRepair(rt);

            if (repairTableDTO.isTableClosed()) {
                rt.setTableClosed(true);
                rt.setTableCloseDate(new Date());
            } else if (rt.isTableClosed() & isAdmin) {
                rt.setTableClosed(false);
                rt.setTableCloseDate(null);
            }
            //Table closed!! and can't be changed

            repTableService.save(rt);
            model.addAttribute("message" , "Table saved");
        } else {
            model.addAttribute("message" , "Table can't be saved it's closed!");
        }

        //rt = repTableService.findByOrderId(repairTableDTO.getOrderId()).get();
        repairTableDTO = repairTableMapper.repTableToRepTableDTO(rt);

        model.addAttribute("table" , repairTableDTO);
        model.addAttribute("parts" , rt.getPartList().size() > 0 ? rt.getPartList() : null);
        return "rtable";
    }

    @GetMapping("/delete/{orderId}/{partId}")
    public String deleteUserById(@PathVariable Long orderId ,
                                 @PathVariable Long partId , Model model) {

        RepairTable rt = repTableService.findByOrderId(orderId).orElse(new RepairTable());
        if (!rt.isTableClosed()) {
            List<Part> list = rt.getPartList();
            list.removeIf(o -> o.getId() == partId);
            if (partService.findById(partId).isPresent()) {
                Part part = partService.findById(partId).get();
                part.setOnStock(true);
                partService.save(part);
            }
            rt.setPartList(list);
            repTableService.save(rt);
            model.addAttribute("message" , "Table saved");
        } else {
            model.addAttribute("message" , "Table can't be saved it's closed!");
        }
        RepairTableDTO repairTableDTO = repairTableMapper.repTableToRepTableDTO(rt);
        repairTableDTO.setOrderId(orderId);

        model.addAttribute("table" , repairTableDTO);
        model.addAttribute("edittable" , true);
        return "rtable";
    }

}
