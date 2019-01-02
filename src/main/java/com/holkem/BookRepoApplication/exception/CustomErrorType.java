package com.holkem.BookRepoApplication.exception;

import com.holkem.BookRepoApplication.model.Book;

public class CustomErrorType extends Book {
	private String errorMessage;

	public CustomErrorType(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
}
