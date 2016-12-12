/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package CrossModelClasses;

/**
 *
 * @author jamesmitchell
 */
public class ArrayStringUtils {
        
        public static String arrayToString(int[] characteristic_A_array) {
		StringBuffer output = new StringBuffer(characteristic_A_array.length);
		
		for (int intvalues : characteristic_A_array) {
			output.append((char)(48 + intvalues));
		}
		
		return output.toString();
	}
	
	// new implementation of string to array based on character values
	public static int[] stringToArray(String key) {
		int[] int_array = new int[key.length()];
		
		for (int i = 0; i < key.length(); i++)
			int_array[i] = (int)key.charAt(i) - 48;
		
		return int_array;
	}
}


