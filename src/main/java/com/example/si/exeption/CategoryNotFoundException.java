package com.example.si.exeption;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(String msg){
        super(msg);
    }
}
