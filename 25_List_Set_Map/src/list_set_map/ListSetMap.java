/**
 *	How to compare a list with a set and map in Java.
 *
 *	@author 		ITWorks4U
 */
package list_set_map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ListSetMap {
	public static void main(String[] args) {
		
		/*	a simple array to use	*/
		int[] integerArray = {
			1, 3 , 9, -7, 16, 12, 999, 123456, 0x123ABC, 753, 42, 0b1001001, 3, 11, 16, 999
		};
		
		/*	list	*/
		List<Integer> listOfIntegers = new ArrayList<>();
		for (Integer tmp : integerArray) {
			listOfIntegers.add(tmp);
		}
		
		/*	set		*/
		Set<Integer> setOfIntegers = new HashSet<>();
		for (Integer tmp : integerArray) {
			setOfIntegers.add(tmp);
		}
				
		/*	map		*/
		int key = 0;
		Map<Integer, Integer> mapOfIntegers = new HashMap<>();
		
		for (Integer tmp : integerArray) {
//			listOfIntegers.add(tmp);
			mapOfIntegers.put(key, tmp);
		}
				
		/*	output	*/
		System.out.println(" *** list output ***");
		for (Integer tmp : listOfIntegers) {
			System.out.print(tmp.toString() + " ");
		}
		System.out.println();
		
		System.out.println(" *** set output ***");
		for (Integer tmp : setOfIntegers) {
			System.out.print(tmp.toString() + " ");
		}
		System.out.println();
				
		System.out.println(" *** map output ***");
//		for (Integer tmp : mapOfIntegers) {
//			System.out.print(tmp.toString() + " ");
//		}
		
		Iterator<Integer> mapIterator = mapOfIntegers.keySet().iterator();
		while (mapIterator.hasNext()) {
			int keyOfMap = (int) mapIterator.next();
			
			System.out.print(mapOfIntegers.get(keyOfMap) + " ");
		}
		
		System.out.println();
	}
}
