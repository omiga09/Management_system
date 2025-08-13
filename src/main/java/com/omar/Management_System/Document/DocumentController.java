package com.omar.Management_System.Document;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

        private final DocumentService documentService;

        public DocumentController(DocumentService documentService) {
            this.documentService = documentService;
        }

        @GetMapping
        public List<Document> getAllDocuments() {
            return documentService.getAllDocuments();
        }

        @GetMapping("/{id}")
        public ResponseEntity<Document> getDocumentById(@PathVariable Long id) {
            return documentService.getDocumentById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        }

        @PostMapping
        public Document createDocument(@RequestBody Document document) {
            return documentService.saveDocument(document);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
            documentService.deleteDocument(id);
            return ResponseEntity.noContent().build();
        }
    }


