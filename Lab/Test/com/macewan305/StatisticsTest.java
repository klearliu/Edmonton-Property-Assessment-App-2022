package com.macewan305;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.macewan305.Statistics.*;
import static org.junit.jupiter.api.Assertions.*;

class StatisticsTest {

    private List<Integer> intList = new ArrayList<>();

    @BeforeEach
    void setup() {

        intList.add(20);
        intList.add(3);
        intList.add(15);
        intList.add(80);
        intList.add(-6);
    }

    @Test
    void medianTest() {

        int mid = median(intList);
        assertEquals(15, mid);

        intList.add(1);

        mid = median(intList);
        assertEquals(9, mid);
    }

    @Test
    void rangeTest() {
        int[] lowAndHigh = new int[2];
        lowAndHigh[0] = -6;
        lowAndHigh[1] = 80;

        int difference = range(lowAndHigh);
        assertEquals(86, difference);
    }

    @Test
    void meanTest() {
        int avg = mean(intList);
        assertEquals(22, avg);
    }

    @Test
    void lowHighAssessTest() {

        int[] expected = new int[2];
        expected[0] = -6;
        expected[1] = 80;

        int[] tested = lowHighAssess(intList);

        assertArrayEquals(expected, tested);


    }
}