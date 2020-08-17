package com.googe.ssadm.sc.controller;

import com.googe.ssadm.sc.dto.NoteDTO;
import com.googe.ssadm.sc.dto.OrderDTO;
import com.googe.ssadm.sc.entity.*;
import com.googe.ssadm.sc.mappers.NoteMapper;
import com.googe.ssadm.sc.mappers.OrderMapper;
import com.googe.ssadm.sc.service.*;
import com.googe.ssadm.sc.utils.MailSender;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Controller
@RequestMapping("/orders")
public class OrderController {
    public static final BigDecimal ONE_HUNDRED = new BigDecimal(100);

    private final OrderService orderService;
    private final ClientService clientService;
    private final UserService userService;
    private final OrderMapper orderMapper;
    private final AwsS3Service awsS3Service;
    private final NoteMapper noteMapper;
    private final NoteService noteService;
    private final RepTableService repTableService;
    private final Environment env;
    private final TransactionService transactionService;

    @Autowired
    @Qualifier("simpleMailSender")
    public MailSender mailSender;

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(OrderService.class);

    @Autowired
    public OrderController(OrderService orderService , ClientService clientService ,
                           UserService userService , OrderMapper orderMapper ,
                           AwsS3Service awsS3Service , NoteService noteService ,
                           NoteMapper noteMapper , RepTableService repTableService , Environment env ,
                           TransactionService transactionService) {
        this.orderService = orderService;
        this.clientService = clientService;
        this.noteService = noteService;
        this.noteMapper = noteMapper;
        this.orderMapper = orderMapper;
        this.userService = userService;
        this.awsS3Service = awsS3Service;
        this.repTableService = repTableService;
        this.env = env;
        this.transactionService = transactionService;
    }

    @Value("${page.orders.size}")
    private int pageSize;
    @Value("${page.startpage}")
    private int startPage;

    @GetMapping(path = "")
    public String ordersPage(HttpServletRequest request , Model model) {
        orderListPaginated(request , model);
        return "orders";
    }

    private void orderListPaginated(HttpServletRequest request , Model model) {
        if (request.getParameter("page") != null && !request.getParameter("page").isEmpty()) {
            startPage = Integer.parseInt(request.getParameter("page")) - 1;
        }
        Page<Order> orderPage = orderService.findAllPaginated(
                PageRequest.of(startPage , pageSize , Sort.by("id").descending()));
        model.addAttribute("ordersPage" , orderPage.map(order ->
                orderMapper.toOrderDto(order , order.getClient())));
    }

    @GetMapping("/searchorder")
    public String searchOrderByNo(@RequestParam("search") String searchString , HttpServletRequest request ,
                                  Model model) {
        try {
            Long searchId = Long.parseLong(searchString);
            if (orderService.findById(searchId).isPresent()) {
                if (request.getParameter("page") != null && !request.getParameter("page").isEmpty()) {
                    startPage = Integer.parseInt(request.getParameter("page")) - 1;
                }
                Page<Order> orderPage = orderService.findByIdPaged(
                        PageRequest.of(startPage , pageSize , Sort.by("id").descending()) ,
                        Long.parseLong(searchString));
                model.addAttribute("ordersPage" , orderPage.map(order ->
                        orderMapper.toOrderDto(order , order.getClient())));
            } else {
                return "redirect:/orders";
            }
        } catch (NumberFormatException nfe) {
            log.info("OrderController: Exception {}" , searchString);
            orderListPaginated(request , model);

        }
        return "orders";
    }


    @GetMapping(path = "/new")
    public String addOrderForm(Model model) {
        model.addAttribute("editorder" , false);
        model.addAttribute("order" , new OrderDTO());
        return "order";
    }

    @PostMapping(path = "/new")
    public String addOrder(@Valid @ModelAttribute("order") OrderDTO orderDTO , BindingResult bindingResult ,
                           RedirectAttributes redirectAttrs , Model model ,
                           Authentication authentication) {
        if (bindingResult.hasErrors()) {
            redirectAttrs.getFlashAttributes().clear();
            redirectAttrs.addFlashAttribute("org.springframework.validation.BindingResult.order" ,
                    bindingResult);
            redirectAttrs.addFlashAttribute("order" , orderDTO);
            model.addAttribute("editorder" , false);
            model.addAttribute("order" , orderDTO);
            return "order";
        }
        Client client = clientService.findByMobile(orderDTO.getClientMobile()).orElseGet(

                () -> clientService.save(Client.builder()
                        .name(orderDTO.getClientName())
                        .surname(orderDTO.getClientSurname())
                        .mobile(orderDTO.getClientMobile())
                        .email(orderDTO.getClientEmail())
                        .orderSet(new HashSet<>())
                        .build())

        );
        User user = userService.findByEmail(authentication.getName()).orElse(new User());
        Set<Order> userOrderSet = user.getOrderSet();
        Set<Order> clientOrderSet = client.getOrderSet();
        Order order = orderMapper.toOrder(orderDTO);
        order.setClient(client);
        order.setStatus(Status.OPEN);//new order! work start from open
        order.setUser(user);
        clientOrderSet.add(order);
        client.setOrderSet(clientOrderSet);
        userOrderSet.add(order);
        user.setOrderSet(userOrderSet);
        orderService.saveOrder(order);
        //send confirmation email
        sendConfirmation(client , order);
        //Return to orderForm to print client copy
        return "redirect:/orders/edit/" + order.getId();
    }

    private void sendConfirmation(Client client , Order order) {
        mailSender.sendMail(client , order);
    }

    @GetMapping(path = "/edit/{id}")
    public String orderForm(@PathVariable Long id , Model model) {
        if (!orderService.findById(id).isPresent()) {
            return "orders";
        }
        Order order = orderService.findById(id).get();
        Client client = order.getClient();
        if (awsS3Service.checkBucketExist(env.getProperty("bucketname") + id)
                && !awsS3Service.getAllFileNameInBucket(env.getProperty("bucketname") + id).isEmpty()) {
                List<String> allFileNameInBucket =
                    awsS3Service.getAllFileNameInBucket(env.getProperty("bucketname") + id);
            model.addAttribute("s3FileNames" , allFileNameInBucket);
        }


        if (repTableService.findByOrderId(id).isPresent() &
                repTableService.findByOrderId(id).orElse(new RepairTable()).isTableClosed()) {
            BigDecimal repaircost = repTableService.findByOrderId(id).get().getPartList().stream()
                    .map(Part::getRecommendedPrice)
                    .reduce(BigDecimal.ZERO , BigDecimal::add);
            BigDecimal discount = repaircost
                    .divide(ONE_HUNDRED , 3 , RoundingMode.CEILING)
                    .multiply(repTableService.findByOrderId(id).get().getDiscount());
            BigDecimal finalCost = repaircost.subtract(discount);
            model.addAttribute("repaircost" , finalCost);
        }
        if (orderService.findAllBySerialNo(order.getSerialNo()).size() > 1) {
            List<String> repairList = orderService.findAllBySerialNo(order.getSerialNo())
                    .stream()
                    .map(o -> String.valueOf(o.getId()))
                    .collect(Collectors.toList());
            model.addAttribute("repairlist" , repairList);
        }

        model.addAttribute("created" , order.getCreatedAt());
        model.addAttribute("order" , orderMapper.toOrderDto(order , client));
        model.addAttribute("statuses" , Status.values());
        model.addAttribute("notes" , noteMapper.toNoteDTOs(noteService.findByOrderId(id)));
        model.addAttribute("note" , new NoteDTO());
        model.addAttribute("editorder" , true);
        return "order";
    }

    @PostMapping(path = "/edit")
    public String updateOrder(OrderDTO orderDTO , Authentication authentication) {
        Order order = orderService.findById(orderDTO.getId()).orElse(new Order());
        order.setClient(clientService.findByMobile(orderDTO.getClientMobile()).orElse(null));
        order.setSerialNo(orderDTO.getSerialNo());
        order.setModelName(orderDTO.getModelName());
        order.setDefectDescription(orderDTO.getDefectDescription());
        User user = userService.findByEmail(authentication.getName()).orElse(new User());
        //add a DEBIT transaction after receiving payment for the repair from the client
        if ((order.getRepair() != null) & orderDTO.getStatus().equals("GIVEN") & !orderDTO.getStatus().equals(order.getStatus().toString())) {
            List<Part> lp = order.getRepair().getPartList();
            RepairTable rt = order.getRepair();
            BigDecimal repaircost = lp.stream()
                    .map(Part::getRecommendedPrice)
                    .reduce(BigDecimal.ZERO , BigDecimal::add);
            BigDecimal discount = repaircost
                    .divide(OrderController.ONE_HUNDRED , 3 , RoundingMode.CEILING)
                    .multiply(rt.getDiscount());
            BigDecimal finalCost = repaircost.subtract(discount).setScale(2 , BigDecimal.ROUND_HALF_UP);

            if(finalCost.compareTo(BigDecimal.ZERO) != 0) {
                Transaction tr = Transaction.builder()
                        .createdAt(new Date())
                        .description("Order " + order.getId() + " given to client")
                        .shortDescription("GIVEN")
                        .debit(true)
                        .user(user)
                        .amount(finalCost)
                        .currency(Currency.valueOf("UAH"))
                        .build();
                transactionService.save(tr);
            }
        }
        order.setStatus(Status.valueOf(orderDTO.getStatus()));
        orderService.saveOrder(order);
        return "redirect:/orders";
    }


}
