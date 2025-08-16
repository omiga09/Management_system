package com.omar.Management_System.Message.MessageDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageDto {

    private Long messageId;
    private Long senderId;
    private Long receiverId;
    private String content;
    private String timestamp;

    public MessageDto() {}

    public MessageDto(Long senderId, Long receiverId, String content) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
    }

    public MessageDto(Long messageId, Long senderId, Long receiverId, String content, String timestamp) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.timestamp = timestamp;
    }
}
