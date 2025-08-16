package com.omar.Management_System.Message.MessageService;

import com.omar.Management_System.Message.Message.Message;
import com.omar.Management_System.Message.MessageDto.MessageDto;
import com.omar.Management_System.Message.MessageRepository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message sendMessage(MessageDto messageDto) {
        Message message = new Message();
        message.setSenderId(messageDto.getSenderId());
        message.setReceiverId(messageDto.getReceiverId());
        message.setContent(messageDto.getContent());
        return messageRepository.save(message);
    }

    public List<Message> getConversation(Long user1Id, Long user2Id) {
        return messageRepository.findConversationBetweenUsers(user1Id, user2Id);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    // ✅ Hii ndiyo method mpya ya inbox
    public List<Message> getMessagesForUser(Long userId) {
        return messageRepository.findByReceiverId(userId);
    }
    public void markMessageAsRead(Long id) {
        Optional<Message> optionalMessage = messageRepository.findById(id);
        if (optionalMessage.isPresent()) {
            Message message = optionalMessage.get();
            message.setRead(true);
            messageRepository.save(message);
        } else {
            throw new RuntimeException("Message not found with ID: " + id);
        }
    }

}
