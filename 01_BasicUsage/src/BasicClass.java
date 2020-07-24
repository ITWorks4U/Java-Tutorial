/**
 *	Contains a basic class with an
 *	empty constructor.
 *
 *	@author 		ITWorks4U
 */
public class BasicClass {
	
	private int value;
	
	public BasicClass() {
		
	}
	

	public static void main(String[] args) {
		BasicClass bc = new BasicClass();
		bc.value = 10;
		
//		value = 10;		
		System.out.println(bc.value);
	}
}
