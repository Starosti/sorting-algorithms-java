package com.company;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Random;

public class Main {
    static final int ARRAY_LEN = 100000;
    static final int MIN = 0;
    static final int MAX = 1000000;
    static final int REPEAT = 100;
    static final String SORTING_METHOD = "mergeSort";
    static final boolean PRINT_ARRAY = false;

    public static void main(String[] args) throws Exception{
        sortTester();
    }

    @SuppressWarnings("PrimitiveArrayArgumentToVarargsMethod")
    public static void sortTester() throws Exception {
        Method sortMethod = Main.class.getMethod(SORTING_METHOD,int[].class);

        int[] array = new int[ARRAY_LEN];

        long avg =0;

        for(int i=0; i<REPEAT;i++){

            createRandomArray(array);

            if (PRINT_ARRAY){
                System.out.println("Unsorted Array:");
                printArray(array);
            }

            long start = System.currentTimeMillis();
            sortMethod.invoke(Main.class,array);
            long end = System.currentTimeMillis();
            avg += end-start;

            if (PRINT_ARRAY){
                System.out.println("Sorted Array:");
                printArray(array);
            }
        }
        long timeDiff = avg/REPEAT;

        System.out.println("Average Time Diff Across "+REPEAT+" Runs: "+timeDiff+" milliseconds");
    }

    private static void createRandomArray(int[] array) {
        Random rand = new Random();
        for(int i = 0; i<ARRAY_LEN;i++){
            array[i] = rand.nextInt(MIN,MAX);
        }
    }

    private static void printArray(int[] array){
        for(int i : array) System.out.print(i+" ");
        System.out.println();
    }

    @SuppressWarnings("unused")
    public static void defaultSort(int[] array){
        // use default java sorting algorithm
        Arrays.sort(array);
    }
    @SuppressWarnings("unused")
    public static void bubbleSort(int[] array){
        boolean swapped = true;
        int cap = array.length;
        while (swapped){
            swapped = false;
            for(int i = 0; i<cap-1;i++){
                if (array[i] > array[i+1]){
                    swapped = true;
                    swap(array,i,i+1);
                }
            }
            if (swapped) cap--;
        }
    }

    private static void swap(int[] array, int index1, int index2){
        int temp = array[index1];
        array[index1] = array[index2];
        array[index2] = temp;
    }

    @SuppressWarnings({"ManualArrayCopy","unused"})
    public static void mergeSort(int[] array){
        int arrayLen = array.length;
        int midPoint = arrayLen/2;

        if (arrayLen < 2){
            return;
        }

        int[] leftHalf = new int[midPoint];
        int[] rightHalf = new int[arrayLen-midPoint];


        for (int i = 0; i < midPoint; i++){
            leftHalf[i] = array[i];
        }

        for (int i = midPoint; i < arrayLen; i++){
            rightHalf[i-midPoint] = array[i];
        }

        mergeSort(leftHalf);
        mergeSort(rightHalf);

        mergeCombineArrays(array,leftHalf,rightHalf);
    }

    private static void mergeCombineArrays(int[] finArr, int[] leftArr, int[] rightArr){
        int i = 0, j = 0, k = 0;

        while (i < leftArr.length && j < rightArr.length){
            if (leftArr[i] < rightArr[j]){
                finArr[k] = leftArr[i];
                i++;
            }
            else {
                finArr[k] = rightArr[j];
                j++;
            }
            k++;
        }
        // add the remaining
        while (i < leftArr.length){
            finArr[k] = leftArr[i];
            i++;
            k++;
        }
        while (j < rightArr.length){
            finArr[k] = rightArr[j];
            j++;
            k++;
        }
    }

    @SuppressWarnings("unused")
    public static void insertionSort(int[] array){
        for (int i = 0; i<array.length ; i++){ // iterate through each element in array
            int currElement = array[i];
            int lastJ = -1;
            for (int j = i-1; j>=0;j--){ // shift each element one right if they are bigger than currElement
                if (array[j] > currElement){
                    array[j+1] = array[j];
                    lastJ = j;
                }
                else break; // if we arrive at an element which is smaller than our element, break
            }
            if (lastJ != -1) array[lastJ] = currElement; // currElement takes the place of last shifted element
        }
    }

    public static void quickSort(int[] array,int lowIndex,int highIndex,boolean random){

        if (lowIndex >= highIndex) return; // for single element arrays

        int partIndex = partitionQSort(array, lowIndex, highIndex, random);

        quickSort(array,lowIndex, partIndex-1,random);
        quickSort(array,partIndex+1,highIndex,random);
    }

    // overloading quicksort
    @SuppressWarnings("unused")
    public static void quickSort(int[] array) {
        quickSort(array,0,array.length-1,false);
    }

    @SuppressWarnings("unused")
    public static void quickSortRandom(int[] array) {
        quickSort(array,0,array.length-1,true);
    }

    private static int partitionQSort(int[] array, int lp, int rp, boolean random) {
        int highIndex = rp;
        int pivot;
        if (random){
            int pivotIndex = new Random().nextInt(rp - lp) + lp;
            pivot = array[pivotIndex];
            swap(array, pivotIndex, highIndex);
        }
        else pivot = array[highIndex];

        while (lp != rp){ // while left pointer < right pointer
            while (array[lp] <= pivot && lp < rp){
                lp++;
                // increase lp until you arrive at a point which is greater than your pivot
            }
            while (array[rp] >= pivot && lp < rp){
                rp--;
                // decrease rp until you arrive at a point which is less than your pivot
            }
            swap(array, lp, rp);
            // swap both and repeat
        }
        swap(array, lp, highIndex); // swapping the last point (lp == rp) with pivot

        if (random){ // this fixes a bug that happens when the pivot is selected randomly
            if(array[lp] > array[highIndex]) {
                swap(array, lp, highIndex);
            }
            else {
                lp = highIndex;
            }
        }
        return lp;
    }

    @SuppressWarnings({"unused", "SpellCheckingInspection"})
    public static void bogoSort(int[] array){
        while (!isSorted(array)){
            shuffle(array);
        }
    }

    private static void shuffle(int[] array) {
        Random rand = new Random();
        int arrayLen = array.length;
        for (int i =0; i<arrayLen; i++){
            int nextIndex = rand.nextInt(i,arrayLen);
            swap(array,i,nextIndex);
        }
    }

    private static boolean isSorted(int[] array) {
        int lastVal = array[0];
        for (int i = 1; i<array.length;i++){
            if (lastVal > array[i]){
                return false;
            }
            lastVal = array[i];
        }
        return true;
    }

}
