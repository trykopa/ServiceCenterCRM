package com.googe.ssadm.sc.controller;

import com.googe.ssadm.sc.dto.PartDTO;
import com.googe.ssadm.sc.entity.*;
import com.googe.ssadm.sc.mappers.PartMapper;
import com.googe.ssadm.sc.mappers.SupplierMapper;
import com.googe.ssadm.sc.service.PartService;
import com.googe.ssadm.sc.service.SupplierService;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.Set;

@Controller
@RequestMapping("/parts")
public class PartController {
    private final PartService partService;
    private final PartMapper partMapper;
    private final SupplierService supplierService;
    private final SupplierMapper supplierMapper;
    private final TransactionService transactionService;
    private final UserService userService;


    @Autowired
    public PartController(PartService partService , PartMapper partMapper , SupplierService supplierService , SupplierMapper supplierMapper , TransactionService transactionService , UserService userService) {
        this.partService = partService;
        this.partMapper = partMapper;
        this.supplierService = supplierService;
        this.supplierMapper = supplierMapper;
        this.transactionService = transactionService;
        this.userService = userService;
    }

    @Value("${page.parts.size}")
    private int pageSize;
    @Value("${page.startpage}")
    private int startPage;

    @GetMapping(path = "")
    public String ordersPage(HttpServletRequest request , Model model) {
        partListPaginated(request , model);
        model.addAttribute("suppliers" , supplierMapper.toSupplierDTOs(supplierService.findAll()));
        return "parts";
    }

    private void partListPaginated(HttpServletRequest request , Model model) {
        if (request.getParameter("page") != null && !request.getParameter("page").isEmpty()) {
            startPage = Integer.parseInt(request.getParameter("page")) - 1;
        }
        Page<Part> partPage = partService.findAllPaginated(PageRequest.of(startPage , pageSize , Sort.by("id").descending()));
        model.addAttribute("partpage" , partPage.map(partMapper::toPartDTO));
    }

    @GetMapping(path = "/new")
    public String newPart(Model model) {
        model.addAttribute("part" , new PartDTO());
        model.addAttribute("editpart" , false);
        model.addAttribute("suppliers" , supplierMapper.toSupplierDTOs(supplierService.findAll()));
        return "part";
    }

    @PostMapping(path = "/new")
    public String addNewPart(@Valid @ModelAttribute("part") PartDTO partDTO , BindingResult bindingResult ,
                             RedirectAttributes redirectAttrs , Model model , Authentication authentication) {
        if (bindingResult.hasErrors()) {
            redirectAttrs.getFlashAttributes().clear();
            redirectAttrs.addFlashAttribute("org.springframework.validation.BindingResult.part" , bindingResult);
            redirectAttrs.addFlashAttribute("part" , partDTO);
            model.addAttribute("editpart" , false);
            model.addAttribute("part" , partDTO);
            return "part";
        }

        if (partService.findBySerialNo(partMapper.toPart(partDTO).getSerialNo()).isPresent()) {
            model.addAttribute("part" , partDTO);
            model.addAttribute("suppliers" , supplierMapper.toSupplierDTOs(supplierService.findAll()));
            model.addAttribute("error" , "Part with this SerialNo already exists! ");
            return "part";
        }

        Part part = partMapper.toPart(partDTO);
        if (!partDTO.isPartOrWork()) {
            Long supplierId = partDTO.getSupplierId();
            Supplier supplier = supplierService.findById(supplierId).get();
            part.setSupplier(supplier);
            //Take part on stock
            part.setOnStock(true);
            Set<Part> partSet = supplier.getPartSet();
            partSet.add(part);
            supplier.setPartSet(partSet);
            //add a credit transaction after payment of the spare parts received at the warehouse
            User user = userService.findByEmail(authentication.getName()).orElse(new User());
            Transaction transaction = Transaction.builder()
                    .createdAt(new Date())
                    .currency(Currency.valueOf("UAH"))
                    .amount(part.getEntryPrice())
                    .shortDescription("Buy part")
                    .description("Arrival part to stock " + part.getPartNo() + " " + part.getSupplier().getCompanyName())
                    .debit(false)
                    .user(user)
                    .build();
            transactionService.save(transaction);
            partService.save(part);
            return "redirect:/parts";
        }
        partService.save(part);
        return "redirect:/parts";
    }

    @GetMapping(path = "/edit/{id}")
    public String editPart(@PathVariable Long id , Model model) {
        model.addAttribute("editpart" , true);
        model.addAttribute("part" , partMapper.toPartDTO(partService.findById(id).orElse(new Part())));
        model.addAttribute("suppliers" , supplierMapper.toSupplierDTOs(supplierService.findAll()));
        return "part";
    }

    @PostMapping(path = "/edit")
    public String updatePart(PartDTO partDTO) {
        Part part = partMapper.toPart(partDTO);
        partService.save(part);
        return "redirect:/parts";
    }

    @GetMapping(path = "/clone/{id}")
    public String clonePart(@PathVariable Long id , Model model) {
        model.addAttribute("editpart" , false);
        Part part = partService.findById(id).orElse(new Part());
        Part newPart = new Part();
        newPart.setPartNo(part.getPartNo());
        newPart.setPartDesc(part.getPartDesc());
        newPart.setEntryPrice(part.getEntryPrice());
        newPart.setRecommendedPrice(part.getRecommendedPrice());
        newPart.setSupplier(part.getSupplier());
        model.addAttribute("part" , partMapper.toPartDTO(newPart));
        model.addAttribute("suppliers" , supplierMapper.toSupplierDTOs(supplierService.findAll()));
        return "part";
    }

    @GetMapping(path = "/delete/{id}")
    public String deletePart(@PathVariable Long id) {
        partService.deleteById(id);
        return "redirect:/parts";
    }

}
