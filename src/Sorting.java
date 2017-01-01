
public class Sorting {
	
	public static void sort(int[][] inputArr) {
        if (inputArr == null || inputArr.length == 0) {
            return;
        }
        int length = inputArr.length;
        quickSort(inputArr, 0, length - 1);
    }
 
	/**
	 * Credit goes to
	 * http://www.java2novice.com/java-sorting-algorithms/quick-sort/
	 * @param array
	 * @param lowerIndex
	 * @param higherIndex
	 */
    public static void quickSort(int[][] array, int lowerIndex, int higherIndex) {
         
        int i = lowerIndex;
        int j = higherIndex;
        
        int pivot = array[lowerIndex+(higherIndex-lowerIndex)/2][0];
        
        while (i <= j) {
        	
            while (array[i][0] < pivot) {
                i++;
            }
            while (array[j][0] > pivot) {
                j--;
            }
            if (i <= j) {
                swap(array, i, j);
                //move index to next position on both sides
                i++;
                j--;
            }
        }
        // call quickSort() method recursively
        if (lowerIndex < j)
            quickSort(array, lowerIndex, j);
        if (i < higherIndex)
            quickSort(array, i, higherIndex);
    }
 
    private static void swap(int[][] array, int i, int j) {
        int temp = array[i][0];
        array[i][0] = array[j][0];
        array[j][0] = temp;
        
        int index = array[i][1];
        array[i][1] = array[j][1];
        array[j][1] = index;
    }
    
    public static void sort(int[] inputArr) {
        if (inputArr == null || inputArr.length == 0) {
            return;
        }
        int length = inputArr.length;
        quickSort(inputArr, 0, length - 1);
    }
 
	/**
	 * Credit goes to
	 * http://www.java2novice.com/java-sorting-algorithms/quick-sort/
	 * @param array
	 * @param lowerIndex
	 * @param higherIndex
	 */
    public static void quickSort(int[] array, int lowerIndex, int higherIndex) {
         
        int i = lowerIndex;
        int j = higherIndex;
        
        int pivot = array[lowerIndex+(higherIndex-lowerIndex)/2];
        
        while (i <= j) {
         
            while (array[i] < pivot) {
                i++;
            }
            while (array[j] > pivot) {
                j--;
            }
            if (i <= j) {
                swap(array, i, j);
                //move index to next position on both sides
                i++;
                j--;
            }
        }
        // call quickSort() method recursively
        if (lowerIndex < j)
            quickSort(array, lowerIndex, j);
        if (i < higherIndex)
            quickSort(array, i, higherIndex);
    }
 
    private static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
        
    }
}
