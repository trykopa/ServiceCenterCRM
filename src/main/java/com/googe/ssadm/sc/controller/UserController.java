package com.googe.ssadm.sc.controller;

import com.googe.ssadm.sc.dto.UserDTO;
import com.googe.ssadm.sc.entity.Role;
import com.googe.ssadm.sc.entity.User;
import com.googe.ssadm.sc.mappers.UserMapper;
import com.googe.ssadm.sc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;


@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserController(UserService userService , UserMapper userMapper , BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @GetMapping(path = "")
    public String getUsers(Model model) {
        model.addAttribute("users" , userMapper.toUserDTOs(userService.findAll()));
        return "users";
    }

    @GetMapping(path = "/edit/{id}")
    public String getUser(@PathVariable Long id , Model model) {
        model.addAttribute("edituser" , true);
        model.addAttribute("roles" , Role.values());
        model.addAttribute("user" , userMapper.toUserDTO(userService.findById(id).orElse(null)));
        return "user";
    }

    @PostMapping(path = "/edit")
    public String updateUser(UserDTO userDTO) {
        User user = userMapper.toUser(userDTO);
        userService.save(user);
        return "redirect:/users";
    }

    @GetMapping("/new")
    public String newUser(Model model) {
        model.addAttribute("edituser" , false);
        model.addAttribute("roles" , Role.values());
        model.addAttribute("user" , new UserDTO());
        return "user";
    }

    @PostMapping("/new")
    public String addNewUser(@Valid @ModelAttribute(value = "user") UserDTO userDTO , BindingResult bindingResult ,
                             RedirectAttributes redirectAttrs , Model model) {
        if (bindingResult.hasErrors()) {
            redirectAttrs.getFlashAttributes().clear();
            redirectAttrs.addFlashAttribute("org.springframework.validation.BindingResult.user" , bindingResult);
            redirectAttrs.addFlashAttribute("user" , userDTO);
            model.addAttribute("roles" , Role.values());
            model.addAttribute("edituser" , false);
            model.addAttribute("user" , userDTO);
            return "user";
        }
        if (userService.findByEmail(userDTO.getEmail()).isPresent() &&
                userService.findByFullName(userDTO.getName() , userDTO.getSurname()).isPresent()) {
            model.addAttribute("error" , "User with this Name,Surname or Email already exists!");
            model.addAttribute("user" , userDTO);
            return "user";
        }

        if (!userDTO.getPassword().equals(userDTO.getPasswordConfirm())) {
            model.addAttribute("error" , "Passwords don't match");
            model.addAttribute("user" , userDTO);
            return "user";
        }
        User user = userMapper.toUser(userDTO);
        user.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
        userService.save(user);
        return "redirect:/users";
    }

    @GetMapping("/delete/{id}")
    public String deleteUserById(@PathVariable Long id) {
        if (
                (userService.findById(id).isPresent()) &
                        (!userService.findById(id).get().getRoles().contains(Role.valueOf("ROLE_ADMIN")))) {
            userService.deleteById(id);
        }
        return "redirect:/users";
    }
}
