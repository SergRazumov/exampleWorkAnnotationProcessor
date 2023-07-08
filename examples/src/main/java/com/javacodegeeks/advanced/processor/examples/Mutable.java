package com.javacodegeeks.advanced.processor.examples;

import com.javacodegeeks.advanced.processor.Immutable;


public interface Mutable {

    @Immutable
    public Mutable get();

}
