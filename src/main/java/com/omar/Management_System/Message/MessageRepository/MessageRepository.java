package com.omar.Management_System.Message.MessageRepository;


import com.omar.Management_System.Message.Message.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

    public interface MessageRepository extends JpaRepository<Message, Long> {

        @Query("SELECT m FROM Message m WHERE " +
                "(m.senderId = :user1Id AND m.receiverId = :user2Id) OR " +
                "(m.senderId = :user2Id AND m.receiverId = :user1Id) " +
                "ORDER BY m.timestamp ASC")
        List<Message> findConversationBetweenUsers(Long user1Id, Long user2Id);
        List<Message> findByReceiverId(Long receiverId);

    }

