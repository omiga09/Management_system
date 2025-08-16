package com.omar.Management_System.Message.MessageController;

import com.omar.Management_System.Message.Message.Message;
import com.omar.Management_System.Message.MessageDto.MessageDto;
import com.omar.Management_System.Message.MessageService.MessageService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/send")
    @PreAuthorize("hasAnyRole('BUYER', 'SELLER')")
    public Message sendMessage(@RequestBody MessageDto messageDto) {
        return messageService.sendMessage(messageDto);
    }

    @GetMapping("/conversation")
    @PreAuthorize("hasAnyRole('BUYER', 'SELLER')")
    public List<Message> getConversation(@RequestParam Long user1Id, @RequestParam Long user2Id) {
        return messageService.getConversation(user1Id, user2Id);
    }
    @PreAuthorize("hasAnyRole('BUYER', 'SELLER')")
    @GetMapping("/inbox/{userId}")
    public List<MessageDto> getInbox(@PathVariable Long userId) {
        List<Message> messages = messageService.getMessagesForUser(userId);
        return messages.stream()
                .map(msg -> new MessageDto(
                        msg.getId(),
                        msg.getSenderId(),
                        msg.getReceiverId(),
                        msg.getContent(),
                        msg.getTimestamp().toString()
                ))
                .toList();
    }
    @PreAuthorize("hasAnyRole('BUYER', 'SELLER')")
    @PutMapping("/messages/{id}/read")
    public void markAsRead(@PathVariable Long id) {
        messageService.markMessageAsRead(id);
    }


    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Message> getAllMessages() {
        return messageService.getAllMessages();
    }
}
