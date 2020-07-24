/**
 *	How to use annotation classes with our own defined class.
 *
 *	@author 		ITWorks4U
 */
package children;

import java.lang.annotation.Annotation;

import annotations.AnnotationExample;
import annotations.CustomAnnotation;
import enumerations.EnumExample;

public class Example {

	@SuppressWarnings({ "deprecation", "incomplete-switch" })
	public static void main(String[] args) {
		Cube cube = new Cube(3, 6, 9);
		Pyramid pyramid = new Pyramid(10, 10, 22);	
		
		//	outputs for Cube
		System.out.println("\n ***** CUBE *****\n");
		System.out.println("area = " + cube.calculateArea());
		System.out.println("volume = " + cube.calculateVolume());
		System.out.println("circumference = " + cube.calculateCircumference());
		System.out.println("geometric object's is a = " + cube.getTypeOfGeometricObject());
		
		System.out.println("cube width: " + cube.getWidth());
		System.out.println("cube height: " + cube.getHeight());
		System.out.println("cube depth: " + cube.getDepth());

		//	outputs for Pyramid
		System.out.println("\n ***** PYRAMID *****\n");
		System.out.println("area = " + pyramid.calculateArea());
		System.out.println("volume = " + pyramid.calculateVolume());
		System.out.println("geometric object's is a = " + pyramid.getTypeOfGeometricObject());
		
		//	get the children of the class
		System.out.println("\n ***** COMPARING SOMETHING *****\n");
		System.out.println("Is \"go\" a part of GeometricObject? → " + EnumExample.isPartOfBaseClass(GeometricObject.class));
		System.out.println("Is \"cube\" a part of GeometricObject? → " + EnumExample.isPartOfBaseClass(Cube.class));
		System.out.println("Is \"pyramid\" a part of GeometricObject? → " + EnumExample.isPartOfBaseClass(Pyramid.class));
		System.out.println("Is \"Example\" a part of GeometricObject? → " + EnumExample.isPartOfBaseClass(Example.class));
		
		System.out.println("\n **********\n");
		
		EnumExample ex = EnumExample.UNKNOWN;
		
		switch (ex) {
		}
		
		Class<AnnotationExample> object = AnnotationExample.class;
		Annotation annotation = object.getAnnotation(CustomAnnotation.class);
		CustomAnnotation ca = (CustomAnnotation) annotation;
		
		System.out.println(ca.author());
		System.out.println(ca.value());
		
		for (String tmp : ca.tags()) {
			System.out.println(tmp);
		}
		
	}
}
