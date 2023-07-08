package com.javacodegeeks.advanced.processor;

import java.util.*;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic;


@SupportedAnnotationTypes( "com.javacodegeeks.advanced.processor.Immutable" )
@SupportedSourceVersion( SourceVersion.RELEASE_17 )

public class SimpleAnnotationProcessor extends AbstractProcessor {

	public SimpleAnnotationProcessor() {

	}


	@Override
	public synchronized void init(ProcessingEnvironment processingEnv) {
		super.init(processingEnv);
	}

	@Override
	public boolean process(final Set< ? extends TypeElement > annotations, 
	        final RoundEnvironment roundEnv) {

		final Queue<TypeElement> queue = new LinkedList<>();

		roundEnv.getElementsAnnotatedWith( Immutable.class )
				.stream()
				.filter(element -> element instanceof TypeElement)
				.map(o ->  ( TypeElement )o)
				.peek(queue::add)
				.flatMap(element -> element.getEnclosedElements().stream())
				.filter(typeElement -> typeElement instanceof VariableElement)
				.map(variableElement -> ( VariableElement )variableElement)
				.filter(variableElement -> !variableElement.getModifiers().contains( Modifier.FINAL ))
				.forEach(element-> processingEnv.getMessager().printMessage( Diagnostic.Kind.ERROR,
						String.format(
								"Class '%s' is annotated as @Immutable, but field '%s' is not declared as final",
								Objects.requireNonNull(queue.poll()).getSimpleName(), element.getSimpleName()
						)
				));


//		for( final Element element: roundEnv.getElementsAnnotatedWith( Immutable.class ) ) {
//			if( element instanceof TypeElement ) {
//				final TypeElement typeElement = ( TypeElement )element;
//
//				for( final Element eclosedElement: typeElement.getEnclosedElements() ) {
//					if( eclosedElement instanceof VariableElement ) {
//						final VariableElement variableElement = ( VariableElement )eclosedElement;
//						if( !variableElement.getModifiers().contains( Modifier.FINAL ) ) {
//							processingEnv.getMessager().printMessage( Diagnostic.Kind.ERROR,
//								String.format(
//									"Class '%s' is annotated as @Immutable, but field '%s' is not declared as final",
//									    typeElement.getSimpleName(), variableElement.getSimpleName()
//								)
//							);
//						}
//					}
//				}
//			}
//		}
		
		// Claiming that annotations have been processed by this processor 
		return true;
	}
}
