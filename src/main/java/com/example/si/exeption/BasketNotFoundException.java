package com.example.si.exeption;

public class BasketNotFoundException extends RuntimeException{
    public BasketNotFoundException(String message) {
        super(message);
    }
}
