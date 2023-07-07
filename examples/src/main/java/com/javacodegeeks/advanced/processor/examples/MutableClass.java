package com.javacodegeeks.advanced.processor.examples;

import com.javacodegeeks.advanced.processor.Immutable;

@Immutable
public class MutableClass {
    private String name;
    
    public MutableClass( final String name ) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
}
