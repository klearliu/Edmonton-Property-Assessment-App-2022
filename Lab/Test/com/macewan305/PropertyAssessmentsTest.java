package com.macewan305;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.macewan305.PropertyAssessments.*;
import static org.junit.jupiter.api.Assertions.*;
import com.macewan305.CsvPropertyAssessmentDAO.*;

class PropertyAssessmentsTest {

    private List<PropertyAssessment> testProperties;

    @BeforeEach
    void setup() throws Exception {

        Path file = Paths.get("Property_Assessment_Data_2022.csv");
        CsvPropertyAssessmentDAO database = new CsvPropertyAssessmentDAO(file);
        testProperties = database.getAll();

    }

    @Test
    void getAssessmentValuesTest() {
        List<Integer> intList = getAssessmentValues(testProperties);
        assertEquals(416044, intList.size());
    }
}