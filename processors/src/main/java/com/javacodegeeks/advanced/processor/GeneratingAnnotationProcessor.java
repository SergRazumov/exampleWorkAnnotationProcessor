package com.javacodegeeks.advanced.processor;

import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.tools.*;
import java.io.IOException;
import java.io.Writer;
import java.util.*;
@AutoService(Processor.class)
@SupportedAnnotationTypes( "com.javacodegeeks.advanced.processor.Immutable" )
@SupportedSourceVersion( SourceVersion.RELEASE_17 )
public class GeneratingAnnotationProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement annotation : annotations) {
            for (Element element : roundEnv.getElementsAnnotatedWith(annotation)) {
                if (element.getKind() == ElementKind.METHOD) {
                    generateClassForMethod((ExecutableElement) element);
                }
            }
        }
        return true;
    }



    private void generateClassForMethod(ExecutableElement methodElement) {
        String className = "Mutable" + "Generated";
        String packageName = processingEnv.getElementUtils().getPackageOf(methodElement).toString();
        String fullClassName = packageName + "." + className;

        StringBuilder classBuilder = new StringBuilder();
        classBuilder.append("package ").append(packageName).append(";\n\n");
        classBuilder.append("public class ").append(className).append(" {\n\n");
        classBuilder.append("    public void execute() {\n");
        classBuilder.append("        // Code here\n");
        classBuilder.append("    }\n");
        classBuilder.append("}\n");

        try {
            JavaFileObject sourceFile = processingEnv.getFiler().createSourceFile(fullClassName);
            try (Writer writer = sourceFile.openWriter()) {
                writer.write(classBuilder.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
