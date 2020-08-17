package com.googe.ssadm.sc.service;

import com.googe.ssadm.sc.entity.Message;
import com.googe.ssadm.sc.repository.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    private final MessageRepo messageRepo;

    @Autowired
    public MessageService(MessageRepo messageRepo) {
        this.messageRepo = messageRepo;
    }

    private static final org.slf4j.Logger log =
            org.slf4j.LoggerFactory.getLogger(MessageService.class);

    public Optional<Message> findById(Long id){
        log.info("MessageService: findById{id} " + id);
        return messageRepo.findById(id);
    }

    public Page<Message> findAllPaginated(Pageable pageable){
        log.info("MessageService: findAllPaginated");
        return messageRepo.findAll(pageable);
    }

    public Page<Message> findMessageToUser(Pageable pageable, Long id){
        log.info("MessageService: findMessageToUser{id} " + id);
        return messageRepo.findByToUser_Id(pageable, id);
    }

    public Page<Message> findMessageFromUser(Pageable pageable, Long id){
        log.info("MessageService: findMessageFromUser{id} " + id);
        return messageRepo.findByFromUser_Id(pageable, id);
    }

    public int findUnread(String username){
        log.info("MessageService: findUnread");
        return messageRepo.findAllMessagesByUnreadIsTrueAndToUser_Email(username).size();
    }

    public Message save(Message message){
        log.info("MessageService: save id:{id}" + message.getId());
        return messageRepo.save(message);
    }

    public void deleteById(Long id){
        log.info("MessageService: delete id:{id}" + id);
        messageRepo.deleteById(id);
    }
}
