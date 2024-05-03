package com.macewan305;

import java.util.*;


public class Statistics {

    /**
     *
     * @param intList: A list of integer values
     * @return The middle most value of the list (median)
     */
    public static int median(List<Integer> intList){

        Collections.sort(intList);
        if((intList.size() % 2) == 0){     // If even amount of values
            int value1 = intList.get(intList.size() / 2);
            int value2 = intList.get((intList.size() / 2) - 1);
            return((value2+value1) / 2);
        }

        else{
            return(intList.get(intList.size() / 2));
        }
    }

    /**
     *
     * Assumed that lowhigh is obtained from the lowhigh function.
     * Index 0 contains the lower number
     * Index 1 contains the higher number
     *
     * @param lowHigh: An integer array of size 2.
     * @return The difference between the 2 integers (return is of type int)
     */
    public static int range(int[] lowHigh){
        return lowHigh[1] - lowHigh[0];
    }


    /**
     *
     * @param intList: A List of integer values
     * @return The average integer value of the list
     */
    public static int mean(List<Integer> intList){
        int line = 0;
        long total = 0; // long because total value gets too big for regular ints
        while (line != intList.size()){
            total += intList.get(line);
            line++;
        }
        return (Math.round((float) total / intList.size()));
    }

    /**
     *
     * @param intList: A List of integer values
     * @return An array of size 2 that contains the lowest and highest integer
     */
    public static int[] lowHighAssess(List<Integer> intList){
        int lowAssess = 0;
        int highAssess = 0;
        int current;
        int index = 0;

        while(index != intList.size()){

            current = intList.get(index); // Assumes that the property value will always be in the 8th column

            if(index == 0){         // Set lowest count to the first checked value, so there is a baseline for comparison.
                lowAssess = current;
            }
            if (current > highAssess) {
                highAssess = current;
            } else if (current < lowAssess) {
                lowAssess = current;
            }
            index++;

        }

        int[] returnVals = new int[2];
        returnVals[0] = lowAssess;
        returnVals[1] = highAssess;

        return returnVals;

    }

}
