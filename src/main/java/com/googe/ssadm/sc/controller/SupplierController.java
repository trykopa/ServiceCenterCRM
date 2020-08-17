package com.googe.ssadm.sc.controller;

import com.googe.ssadm.sc.dto.SupplierDTO;
import com.googe.ssadm.sc.entity.Supplier;
import com.googe.ssadm.sc.mappers.SupplierMapper;
import com.googe.ssadm.sc.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/suppliers")
public class SupplierController {
    private final SupplierService supplierService;
    private final SupplierMapper supplierMapper;

    @Autowired
    public SupplierController(SupplierService supplierService , SupplierMapper supplierMapper) {
        this.supplierService = supplierService;
        this.supplierMapper = supplierMapper;
    }

    @GetMapping(path = "")
    public String allSupliers(Model model) {
        model.addAttribute("suppliers" , supplierMapper.toSupplierDTOs(supplierService.findAll()));
        return "suppliers";
    }

    @GetMapping(path = "/new")
    public String newSupplier(Model model) {
        model.addAttribute("editsupplier" , false);
        model.addAttribute("supplier" , new SupplierDTO());
        return "supplier";
    }

    @PostMapping(path = "/new")
    public String addNewSupplier(@Valid @ModelAttribute(value = "supplier") SupplierDTO supplierDTO ,
                                 BindingResult bindingResult , RedirectAttributes redirectAttrs , Model model) {
        if (bindingResult.hasErrors()) {
            redirectAttrs.getFlashAttributes().clear();
            redirectAttrs.addFlashAttribute("org.springframework.validation.BindingResult.supplier" , bindingResult);
            redirectAttrs.addFlashAttribute("supplier" , supplierDTO);
            model.addAttribute("editsupplier" , false);
            model.addAttribute("supplier" , supplierDTO);
            return "supplier";
        }
        if (supplierService.findByMobile(supplierDTO.getMobile()).isPresent()) {
            model.addAttribute("error" , "Supplier with this mobile already exist!");
            model.addAttribute("supplier" , supplierDTO);
            return "supplier";
        }
        Supplier supplier = supplierMapper.toSupplier(supplierDTO);
        supplierService.save(supplier);
        return "redirect:/suppliers";
    }

    @GetMapping(path = "/edit/{id}")
    public String editSupplier(@PathVariable Long id , Model model) {
        model.addAttribute("editsupplier" , true);
        model.addAttribute("supplier" , supplierMapper.toSupplierDto(supplierService.findById(id).orElse(null)));
        return "supplier";
    }

    @PostMapping(path = "/edit")
    public String saveSupplier(SupplierDTO supplierDTO) {
        supplierService.save(supplierMapper.toSupplier(supplierDTO));
        return "redirect:/suppliers";
    }

    @GetMapping("/delete/{id}")
    public String deleteUserById(@PathVariable Long id) {
        supplierService.deleteById(id);
        return "redirect:/suppliers";
    }
}
