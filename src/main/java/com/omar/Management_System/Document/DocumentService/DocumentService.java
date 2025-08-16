package com.omar.Management_System.Document.DocumentService;


import com.omar.Management_System.Document.Document.Document;
import com.omar.Management_System.Document.DocumentRepository.DocumentRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentService {

        private final DocumentRepository documentRepository;

        public DocumentService(DocumentRepository documentRepository) {

            this.documentRepository = documentRepository;
        }

        public Document saveDocument(Document document) {

            return documentRepository.save(document);
        }

        public Optional<Document> getDocumentById(Long id) {

            return documentRepository.findById(id);
        }

        public List<Document> getAllDocuments() {

            return documentRepository.findAll();
        }

        public void deleteDocument(Long id) {

            documentRepository.deleteById(id);
        }
    }


