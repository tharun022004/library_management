package com.Library.Library_management.service;

import com.Library.Library_management.dto.BookRequest;
import com.Library.Library_management.dto.BookResponse;
import com.Library.Library_management.entity.Book;
import com.Library.Library_management.exception.ResourceNotFoundException;
import com.Library.Library_management.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public BookResponse createBook(BookRequest request) {
        Book book = Book.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .available(true)
                .build();
        book = bookRepository.save(book);
        return mapToResponse(book);
    }

    public List<BookResponse> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public BookResponse getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        return mapToResponse(book);
    }

    public List<BookResponse> getAvailableBooks() {
        return bookRepository.findByAvailable(true).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<BookResponse> searchBooks(String keyword) {
        return bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(keyword, keyword).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public BookResponse updateBook(Long id, BookRequest request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book = bookRepository.save(book);
        return mapToResponse(book);
    }

    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Book not found with id: " + id);
        }
        bookRepository.deleteById(id);
    }

    public BookResponse mapToResponse(Book book) {
        return BookResponse.builder()
                .bookId(book.getBookId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .available(book.isAvailable())
                .createdAt(book.getCreatedAt())
                .build();
    }
}
