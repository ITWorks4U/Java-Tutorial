/**
 *	How to get access to a private field or private method in Java.
 *
 *	@author 		ITWorks4U
 */
package outer;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import inner.PrivateClass;

public class PrivateClassAccess {
	public static void main(String... strings) {
		PrivateClass pc = new PrivateClass();
		System.out.println(" Normal access from getter method: " + pc.getContent());
		
		/*	usually impossible	*/
		//System.out.println(pc.readOnlyContent);
		//pc.aPrivateMethod();
		
		try {
			Field fieldAccess = pc.getClass().getDeclaredField("readOnlyContent");
			fieldAccess.setAccessible(true);
			
			Object privateMember = fieldAccess.get(pc);
			System.out.println(" Your private member contains: " + privateMember);
			
			privateMember = " We're also able to modify the content of the private member...";
			System.out.println(privateMember);
			
			/*	-----------------------------------	*/
			Method methodAccess = pc.getClass().getDeclaredMethod("aPrivateMethod");
			methodAccess.setAccessible(true);
			
			methodAccess.invoke(pc);
			
		} catch (NoSuchFieldException e) {				/*	if the field name was incorrect or not found	*/
			e.printStackTrace();
		} catch (SecurityException e) {					/*	if the security access was incorrect			*/
			e.printStackTrace();
		} catch (IllegalArgumentException e) {			/*	if the argument was incorrect or incomplete		*/
			e.printStackTrace();
		} catch (IllegalAccessException e) {			/*	if you don't have access rights to this method	*/
			e.printStackTrace();
		} catch (NoSuchMethodException e) {				/*	if the given method as name was not found		*/
			e.printStackTrace();
		} catch (InvocationTargetException e) {			/*	if the given target was incorrect or not found	*/
			e.printStackTrace();
		}
	}
}
