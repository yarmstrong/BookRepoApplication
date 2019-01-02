package com.holkem.BookRepoApplication.rest;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.holkem.BookRepoApplication.exception.CustomErrorType;
import com.holkem.BookRepoApplication.model.Book;
import com.holkem.BookRepoApplication.repository.BookJpaRepository;

@RestController
@RequestMapping("/api/books")
public class BookController {
	
	public static final Logger logger = LoggerFactory.getLogger(BookController.class);
	
	@Autowired
	private BookJpaRepository bookJpaRepo;
	
	// IMPORTANT: use value={"","/"} to work with both non-/ or with-/ URIs
	
	// @RequestMapping(value={"","/"}, method=RequestMethod.GET)
	@GetMapping(value={"","/"})
	public ResponseEntity<List<Book>> getAllBooks() {
		List<Book> books = bookJpaRepo.findAll();
		if (books.isEmpty()) {
			return new ResponseEntity<List<Book>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<Book>>(books, HttpStatus.OK);
	}
	
	// @RequestMapping(value="/{bookId}", method=RequestMethod.GET)
	@GetMapping("/{bookId}")
	public ResponseEntity<Book> getBookById(@PathVariable("bookId") final Long id) {
		Optional<Book> book = bookJpaRepo.findById(id);
		if (!book.isPresent()) {
			return new ResponseEntity<Book>(
					new CustomErrorType("Book with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Book>(book.get(), HttpStatus.OK);
	}
	
	// @RequestMapping(value={"","/"}, method=RequestMethod.POST)
	@PostMapping(value={"","/"}, consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Book> addBook(@RequestBody final Book book) {
		if (bookJpaRepo.findByTitleAndAuthor(book.getTitle(), book.getAuthor()).isPresent()) {
			return new ResponseEntity<Book>(
					new CustomErrorType("Unable to add. Book titled \"" + book.getTitle() 
						+ "\" by \"" + book.getAuthor() + "\" already existing."),
					HttpStatus.CONFLICT);
		}
		bookJpaRepo.save(book);
		return new ResponseEntity<Book>(book, HttpStatus.CREATED);
	}
	
	// @RequestMapping(value="/books/{bookId}", method=RequestMethod.PUT)
	@PutMapping(value="/{bookId}", consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Book> updateBook(@PathVariable("bookId") Long id, @RequestBody Book book) {
		Optional<Book> currentBook = bookJpaRepo.findById(id);
		if (!currentBook.isPresent()) {
			return new ResponseEntity<Book>(
					new CustomErrorType("Unable to update. Book with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		Book newBook = currentBook.get();
		newBook.setTitle(book.getTitle());
		newBook.setAuthor(book.getAuthor());
		newBook.setPublisher(book.getPublisher());
		bookJpaRepo.saveAndFlush(newBook);
		return new ResponseEntity<Book>(newBook, HttpStatus.OK);
	}
	
	// @RequestMapping(value="/books/{bookId}", method=RequestMethod.DELETE)
	@DeleteMapping("/{bookId}")
	public ResponseEntity<Book> deleteBook(@PathVariable("bookId") Long id) {
		Optional<Book> currentBook = bookJpaRepo.findById(id);
		if (!currentBook.isPresent()) {
			return new ResponseEntity<Book>(
					new CustomErrorType("Unable to delete. Book with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		bookJpaRepo.deleteById(id);
		return new ResponseEntity<Book>(HttpStatus.OK);
	}
}
