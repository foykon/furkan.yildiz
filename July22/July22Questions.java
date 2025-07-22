package July22;

public class July22Questions {

    /*
        Returns a new array with the elements of the input array reversed
     */
    public static int[] reverseArray(int[] arr) {
        int[] tempArr = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            tempArr[i] = arr[arr.length - 1 - i];
        }
        return tempArr;
    }


}
