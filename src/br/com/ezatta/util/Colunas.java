package br.com.ezatta.util;

/**
 * 
 *
 */
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Colunas {
	String nome();
	double size();	
	int numCasaDecimal() default 2;
}
