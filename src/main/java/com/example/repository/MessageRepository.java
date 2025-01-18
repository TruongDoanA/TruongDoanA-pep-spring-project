package com.example.repository;

import com.example.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer>{

    @Query("FROM Message WHERE postedBy = :postedBy")
    List<Message> findAllMessagesByAccountId(@Param("postedBy") int accountId);
}
