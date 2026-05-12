package com.Library.Library_management.controller;

import com.Library.Library_management.dto.ApiResponse;
import com.Library.Library_management.dto.BookRequest;
import com.Library.Library_management.dto.BookResponse;
import com.Library.Library_management.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@Tag(name = "Books", description = "Book management APIs")
public class BookController {

    private final BookService bookService;

    @PostMapping("/add-books")
    @Operation(summary = "Create a new book")
    public ResponseEntity<ApiResponse<BookResponse>> createBook(@Valid @RequestBody BookRequest request) {
        BookResponse response = bookService.createBook(request);
        return new ResponseEntity<>(new ApiResponse<>(true, "Book created successfully", response), HttpStatus.CREATED);
    }

    @GetMapping("/all-books")
    @Operation(summary = "Get all books")
    public ResponseEntity<ApiResponse<List<BookResponse>>> getAllBooks() {
        List<BookResponse> response = bookService.getAllBooks();
        return ResponseEntity.ok(new ApiResponse<>(true, "Books retrieved successfully", response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a book by ID")
    public ResponseEntity<ApiResponse<BookResponse>> getBookById(@PathVariable Long id) {
        BookResponse response = bookService.getBookById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Book retrieved successfully", response));
    }

    @GetMapping("/available")
    @Operation(summary = "Get all available books")
    public ResponseEntity<ApiResponse<List<BookResponse>>> getAvailableBooks() {
        List<BookResponse> response = bookService.getAvailableBooks();
        return ResponseEntity.ok(new ApiResponse<>(true, "Available books retrieved successfully", response));
    }

    @GetMapping("/search")
    @Operation(summary = "Search books by keyword")
    public ResponseEntity<ApiResponse<List<BookResponse>>> searchBooks(@RequestParam String keyword) {
        List<BookResponse> response = bookService.searchBooks(keyword);
        return ResponseEntity.ok(new ApiResponse<>(true, "Books matching keyword retrieved successfully", response));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a book")
    public ResponseEntity<ApiResponse<BookResponse>> updateBook(@PathVariable Long id,
            @Valid @RequestBody BookRequest request) {
        BookResponse response = bookService.updateBook(id, request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Book updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a book")
    public ResponseEntity<ApiResponse<Void>> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Book deleted successfully", null));
    }
}
