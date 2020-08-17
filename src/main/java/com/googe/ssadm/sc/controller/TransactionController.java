package com.googe.ssadm.sc.controller;

import com.googe.ssadm.sc.dto.TransactionDTO;
import com.googe.ssadm.sc.entity.*;
import com.googe.ssadm.sc.mappers.TransactionMapper;
import com.googe.ssadm.sc.service.TransactionService;
import com.googe.ssadm.sc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.stream.Stream;


@Controller
@RequestMapping("/transactions")
public class TransactionController {
    private final TransactionMapper transactionMapper;
    private final TransactionService transactionService;
    private final UserService userService;

    @Autowired
    public TransactionController(TransactionMapper transactionMapper , TransactionService transactionService , UserService userService) {
        this.transactionMapper = transactionMapper;
        this.transactionService = transactionService;
        this.userService = userService;
    }

    private static TransactionDTO transactionToDTO(Transaction transaction) {
        TransactionDTO tdto = new TransactionDTO();
        tdto.setId(transaction.getId());
        tdto.setDescription(transaction.getDescription());
        tdto.setAuthor(transaction.getUser().getName() + ' ' + transaction.getUser().getSurname());
        tdto.setAmount(transaction.getAmount());
        tdto.setCurrency(transaction.getCurrency().name());
        tdto.setDebit(transaction.isDebit());
        tdto.setCreatedAt(transaction.getCreatedAt());
        tdto.setShortDescription(transaction.getShortDescription());
        return tdto;
    }

    @Value("${page.transactions.size}")
    private int pageSize;
    @Value("${page.startpage}")
    private int startPage;

    @GetMapping(path = "")
    public String transactionsPage(HttpServletRequest request, Model model){
        if (request.getParameter("page") != null && !request.getParameter("page").isEmpty()) {
            startPage = Integer.parseInt(request.getParameter("page")) - 1;
        }
        Page<Transaction> trPage = transactionService.findAllPageable(PageRequest.of(startPage, pageSize, Sort.by("id").descending()));
        Page<TransactionDTO> trDTOPage = trPage.map(TransactionController::transactionToDTO);
        BigDecimal income = transactionService.findAllIncome().stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal expenses = transactionService.findAllExpense().stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        //month and day statistic
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Calendar calendarTwo = Calendar.getInstance();
        calendarTwo.setTime(today);
        calendarTwo.set(Calendar.DAY_OF_MONTH, calendarTwo.get(Calendar.DAY_OF_MONTH)-1);

        BigDecimal monthlyIncome = transactionService.findAllIncome().stream()
                .filter(t -> t.getCreatedAt().after(calendar.getTime()))
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal monthlyExpenses = transactionService.findAllExpense().stream()
                .filter(t -> t.getCreatedAt().after(calendar.getTime()))
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal dailyIncome = transactionService.findAllIncome().stream()
                .filter(t -> t.getCreatedAt().after(calendarTwo.getTime()))
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal dailyExpenses = transactionService.findAllExpense().stream()
                .filter(t -> t.getCreatedAt().after(calendarTwo.getTime()))
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        model.addAttribute("expenses", expenses);
        model.addAttribute("income", income);
        model.addAttribute("mexpenses", monthlyExpenses);
        model.addAttribute("mincome", monthlyIncome);
        model.addAttribute("dexpenses", dailyExpenses);
        model.addAttribute("dincome", dailyIncome);
        model.addAttribute("transactionsPage", trDTOPage);
        return "transactions";
    }

    @GetMapping(path = "/new")
    public String newTransaction(Model model){
        model.addAttribute("transaction", new TransactionDTO());
        model.addAttribute("currencies", Currency.values());
        return "transaction";
    }

    @PostMapping(path = "/new")
    public String addTransaction(TransactionDTO transactionDTO, Authentication authentication){
        Transaction transaction = transactionMapper.toTransaction(transactionDTO);
        transaction.setUser(userService.findByEmail(authentication.getName()).orElse(new User()));
        transactionService.save(transaction);
        return "redirect:/transactions";
    }

    @GetMapping(path = "/edit/{id}")
    public String editTransaction(@PathVariable Long id, Model model){
        Transaction transaction = transactionService.findById(id).orElse(new Transaction());
        model.addAttribute("transaction", transactionToDTO(transaction));
        return "transaction";
    }

    @GetMapping(path = "/delete/{id}")
    public String deleteTransaction(@PathVariable Long id){
        transactionService.delete(id);
        return "redirect:/transactions";
    }

}
