package com.example.service;

import com.example.entity.Message;
import com.example.entity.Account;
import com.example.repository.MessageRepository;
import com.example.repository.AccountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;

@Service
public class MessageService {
    MessageRepository messageRepository;
    AccountRepository accountRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository){
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    public Message createMessage(Message message){
        if(message.getMessageText() == null || message.getMessageText().length() >= 255 || message.getMessageText().isEmpty())
            throw new IllegalArgumentException("Message text must not be blank/null or longer than 255 characters");

        Optional<Account> optionalAccount = accountRepository.findById(message.getPostedBy());
        if(!optionalAccount.isPresent())
            throw new RuntimeException("User does not exist");

        return messageRepository.save(message);
    }

    public List<Message> getAllMessages(){
        return messageRepository.findAll();
    }
    
    public Message getMessageByMessageId(int messageId){
        Optional<Message> optionalMessage = messageRepository.findById(messageId);
        if(optionalMessage.isPresent()){
            return optionalMessage.get();
        } else {
            return null;
        }
    }

    public void deleteMessageById(int messageId){
        messageRepository.deleteById(messageId);
    }

    public void updateMessageById(int messageId, String replacement){
        if(replacement == null || replacement.length() >= 255 || replacement.isEmpty())
            throw new IllegalArgumentException("Message text must not be blank/null or longer than 255 characters");

        Optional<Message> optionalMessage = messageRepository.findById(messageId);
        if(optionalMessage.isPresent()){
            Message msg = optionalMessage.get();
           
            msg.setMessageText(replacement);
            messageRepository.save(msg);
        } else {
            throw new RuntimeException("Message with " + messageId + "does not exist");
        }

        
    }

    public List<Message> getAllMessagesByAccountId(int accountId){
        return messageRepository.findAllMessagesByAccountId(accountId);
    }
}
