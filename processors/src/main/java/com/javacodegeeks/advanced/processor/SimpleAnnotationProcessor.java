package com.javacodegeeks.advanced.processor;

import java.util.Set;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic;

//Используется для указания аннотаций, которые будут обрабатываться этим процессором.
@SupportedAnnotationTypes( "com.javacodegeeks.advanced.processor.Immutable" )

//Используется для указания используемой версии Java, если необходимо придерживаться какой-либо конкретной версии и указания аннотаций
@SupportedSourceVersion( SourceVersion.RELEASE_17 )

public class SimpleAnnotationProcessor extends AbstractProcessor {

	public SimpleAnnotationProcessor() {

	}

	//Каждый класс процессора аннотаций должен иметь пустой конструктор.
	//Однако существует специальный метод init() для того, добавлять какую-либо логику при инициализации класса.
	@Override
	public synchronized void init(ProcessingEnvironment processingEnv) {
		super.init(processingEnv);
	}

	//Это своего рода метод main() каждого процессора.
	//Здесь вы пишете свой код для сканирования, оценки и обработки аннотаций и создания java-файлов.
	@Override
	public boolean process(final Set< ? extends TypeElement > annotations, 
	        final RoundEnvironment roundEnv) {
	    
		for( final Element element: roundEnv.getElementsAnnotatedWith( Immutable.class ) ) {
			if( element instanceof TypeElement ) {
				final TypeElement typeElement = ( TypeElement )element;
				
				for( final Element eclosedElement: typeElement.getEnclosedElements() ) {
					if( eclosedElement instanceof VariableElement ) {
						final VariableElement variableElement = ( VariableElement )eclosedElement;
						if( !variableElement.getModifiers().contains( Modifier.FINAL ) ) {
							processingEnv.getMessager().printMessage( Diagnostic.Kind.ERROR,
								String.format( 
									"Class '%s' is annotated as @Immutable, but field '%s' is not declared as final",
									    typeElement.getSimpleName(), variableElement.getSimpleName() 
								) 
							);
						}
					}
				}
			}
		}
		
		// Claiming that annotations have been processed by this processor 
		return true;
	}
}
