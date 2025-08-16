package com.omar.Management_System.Document.DocumentRepository;

import com.omar.Management_System.Document.Document.Document;
import com.omar.Management_System.Property.Property.Property;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByProperty(Property property);

}
