package com.example.controller;

//import com.azul.crs.client.Response;
import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

import java.util.List;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

@RestController
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService){
        this.accountService = accountService;
        this.messageService = messageService;

    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Account account){
        try {
            Account savedAccount = accountService.registerUser(account);
            return ResponseEntity.ok(savedAccount);
        } catch (RuntimeException e) {
            if(e.getMessage().equals("Username has already been taken"))
                return ResponseEntity.status(409).body(e.getMessage());
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginWithUsernameAndPassword(@RequestBody Account account){
        Account loggedAccount = accountService.loginWithUsernameAndPassword(account.getUsername(), account.getPassword());
        if(loggedAccount != null)
            return ResponseEntity.ok(loggedAccount);
        return ResponseEntity.status(401).body("");
    }

    @PostMapping("/messages")
    public ResponseEntity<?> createMessage(@RequestBody Message message) {
        try {
            Message createdMsg = messageService.createMessage(message);
            return ResponseEntity.ok(createdMsg);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body("");
        }
    }

    @GetMapping("/messages")
    public ResponseEntity<?> getAllMessages(){
        List<Message> messages = messageService.getAllMessages();
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/messages/{message_id}")
    public ResponseEntity<?> getMessageByMessageId(@PathVariable("message_id") int messageId){
        Message message = messageService.getMessageByMessageId(messageId);
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/messages/{message_id}")
    public ResponseEntity<?> deleteMessageById(@PathVariable("message_id") int messageId){
        if(messageService.getMessageByMessageId(messageId) != null){
            messageService.deleteMessageById(messageId);
            return ResponseEntity.status(200).body("1");
        }
        return ResponseEntity.status(200).body("");
    }

    @PatchMapping("/messages/{message_id}")
    public ResponseEntity<?> updateMessagebyId(@PathVariable("message_id") int messageId, @RequestBody Message message){
        try {
            if(message.getMessageText() == null || message.getMessageText().length() >= 255 || message.getMessageText().isEmpty())
                return ResponseEntity.status(400).body("");
            messageService.updateMessageById(messageId, message.getMessageText());
            return ResponseEntity.status(200).body("1");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body("");
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body("");
        }
    }

    @GetMapping("/accounts/{account_id}/messages")
    public ResponseEntity<?> getAllMessagesByAccountId(@PathVariable("account_id") int accountId){
        List<Message> messages = messageService.getAllMessagesByAccountId(accountId);
        return ResponseEntity.ok(messages);
    }
}
