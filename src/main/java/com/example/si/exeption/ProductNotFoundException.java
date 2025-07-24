package com.example.si.exeption;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(String ms){
        super(ms);
    }
}
