package com.holkem.BookRepoApplication.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.holkem.BookRepoApplication.model.Book;

@Repository
public interface BookJpaRepository extends JpaRepository<Book, Long> {
	Optional<Book> findByTitleAndAuthor(String title, String author);
}
