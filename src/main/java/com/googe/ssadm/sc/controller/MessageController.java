package com.googe.ssadm.sc.controller;

import com.googe.ssadm.sc.dto.MessageDTO;
import com.googe.ssadm.sc.entity.Message;
import com.googe.ssadm.sc.entity.Note;
import com.googe.ssadm.sc.entity.Order;
import com.googe.ssadm.sc.entity.User;
import com.googe.ssadm.sc.mappers.MessageMapper;
import com.googe.ssadm.sc.mappers.UserMapper;
import com.googe.ssadm.sc.service.MessageService;
import com.googe.ssadm.sc.service.NoteService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/messages")
public class MessageController {
    private final MessageService messageService;
    private final MessageMapper messageMapper;
    private final NoteService noteService;
    private final UserService userService;
    private final UserMapper userMapper;

    @Autowired
    public MessageController(MessageService messageService , MessageMapper messageMapper , NoteService noteService , UserService userService , UserMapper userMapper) {
        this.messageService = messageService;
        this.messageMapper = messageMapper;
        this.noteService = noteService;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @Value("${page.messages.size}")
    private int pageSize;
    @Value("${page.startpage}")
    private int startPage;


    @GetMapping(path = "")
    public String allMessages(HttpServletRequest request , Model model) {
        if (request.getParameter("page") != null && !request.getParameter("page").isEmpty()) {
            startPage = Integer.parseInt(request.getParameter("page")) - 1;
        }
        Page<Message> messagePage = messageService.findAllPaginated(PageRequest.of(
                startPage , pageSize , Sort.by("id").descending()));
        model.addAttribute("messagePage" , messagePage);
        return "messages";
    }

    @GetMapping(path = "/inbox")
    public String inbox(HttpServletRequest request , Model model ,
                        Authentication authentication) {
        if (request.getParameter("page") != null && !request.getParameter("page").isEmpty()) {
            startPage = Integer.parseInt(request.getParameter("page")) - 1;
        }
        User user = userService.findByEmail(authentication.getName()).orElse(new User());
        Page<Message> messagePage = messageService.findMessageToUser(PageRequest.of(
                startPage , pageSize , Sort.by("id").descending()) , user.getId());
        model.addAttribute("messagePage" , messagePage);
        return "/messages";
    }

    @GetMapping(path = "/sent")
    public String sent(HttpServletRequest request , Model model ,
                       Authentication authentication) {
        if (request.getParameter("page") != null && !request.getParameter("page").isEmpty()) {
            startPage = Integer.parseInt(request.getParameter("page")) - 1;
        }
        User user = userService.findByEmail(authentication.getName()).orElse(new User());
        Page<Message> messagePage = messageService.findMessageFromUser(PageRequest.of(
                startPage , pageSize , Sort.by("id").descending()) , user.getId());
        model.addAttribute("messagePage" , messagePage);
        return "/messages";
    }

    @GetMapping(path = "/new")
    public String newMessage(Model model) {
        model.addAttribute("messageform" , new MessageDTO());
        model.addAttribute("userlist" , userMapper.toUserDTOs(userService.findAll()));
        return "messageform";
    }

    @PostMapping(path = "/new")
    public String addMessage(MessageDTO messageDTO , Authentication authentication) {
        boolean forall;
        forall = messageDTO.getToUser() == 0;

        if (forall) {
            List<User> allUsers = userService.findAll();
            for (User user : allUsers) {
                Message multipleMessage = messageMapper.toMessage(messageDTO);
                sendMesssage(authentication , multipleMessage , user);
                messageService.save(multipleMessage);
            }

        } else {
            Message message = messageMapper.toMessage(messageDTO);
            User toUser = userService.findById(messageDTO.getToUser()).orElse(new User());
            sendMesssage(authentication , message , toUser);
            //add message as note to order
            if (message.getOrder() != null) {
                Order order = message.getOrder();
                Set<Note> noteSet = order.getNoteSet();
                Note note = new Note();
                note.setOrder(order);
                note.setMessage(
                        "From: " + message.getFromUser().getSurname() + " "
                                + message.getFromUser().getName() + "\n" +
                                "To: " + message.getToUser().getSurname() + " "
                                + message.getToUser().getName() + "\n" +
                                message.getText()
                );
                noteSet.add(note);
                noteService.save(note);
            }
            messageService.save(message);
        }
        return "redirect:/messages/inbox";
    }

    @GetMapping(path = "/read/{id}")
    public String readMessage(@PathVariable Long id , Model model , Authentication authentication) {
        Message message = messageService.findById(id).orElse(new Message());
        User reader = userService.findByEmail(authentication.getName()).orElse(new User());
        User targetUser = message.getToUser();
        if (reader.getId() == targetUser.getId()) message.setUnread(false);
        messageService.save(message);
        model.addAttribute("readmes" , message);
        return "readmessage";
    }

    @GetMapping(path = "/delete/{id}")
    public String deleteMessage(@PathVariable Long id) {
        messageService.deleteById(id);
        return "redirect:/messages/inbox";
    }

    private void sendMesssage(Authentication authentication , Message message , User toUser) {
        List<Message> toUserMess = toUser.getToUserList();
        if (toUserMess == null) toUserMess = new ArrayList<>();
        toUserMess.add(message);
        User fromUser = userService.findByEmail(authentication.getName()).orElse(new User());
        List<Message> fromUserMess = fromUser.getFromUserList();
        if (fromUserMess == null) fromUserMess = new ArrayList<>();
        fromUserMess.add(message);
        message.setToUser(toUser);
        message.setFromUser(fromUser);
        message.setUnread(true);
    }
}
