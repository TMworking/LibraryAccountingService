package org.example.exception;

public class BookDeletionException extends RuntimeException {
    public BookDeletionException(String message) {
        super(message);
    }
}
