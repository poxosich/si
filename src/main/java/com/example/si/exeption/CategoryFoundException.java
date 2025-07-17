package com.example.si.exeption;

public class CategoryFoundException extends RuntimeException {
    public CategoryFoundException(String ms){
        super(ms);
    }
}
