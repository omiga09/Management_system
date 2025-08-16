package com.omar.Management_System.Message.Message;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "messages")
public class Message {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private Long senderId;
        private Long receiverId;

        private String content;

        private LocalDateTime timestamp;

        private boolean isRead;

        public Message() {
            this.timestamp = LocalDateTime.now();
            this.isRead = false;
        }
    }


