package com.javacodegeeks.advanced.processor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// означает что она может быть объявлена перед классом, интерфейсом или enum.
@Target( ElementType.TYPE )

//аннотоация будет записана в class-файл компилятором, но не должна быть доступна во время выполнения (runtime);
@Retention( RetentionPolicy.CLASS )
public @interface Immutable {
}
