package com.macewan305;

import java.io.UnsupportedEncodingException;
import java.util.*;

public interface PropertyAssessmentDAO {

    PropertyAssessment getAccountNum(int accountNumber) throws UnsupportedEncodingException;
    List<PropertyAssessment> getNeighbourhood(String nameOfNeighbourhood) throws UnsupportedEncodingException;
    List<PropertyAssessment>  getAssessClass(String nameOfAssessClass) throws UnsupportedEncodingException;
    List<PropertyAssessment> getSuite(String nameOfSuite) throws UnsupportedEncodingException;
    List<PropertyAssessment> getStreet(String streetName) throws UnsupportedEncodingException;
    List<PropertyAssessment> getHouse(String houseNum) throws UnsupportedEncodingException;
    List<PropertyAssessment>  getWard(String nameOfWard) throws UnsupportedEncodingException;
    List<PropertyAssessment> getRange(int lowerVal, int higherVal) throws UnsupportedEncodingException;
    List<PropertyAssessment>  getAll() throws UnsupportedEncodingException;
    List<PropertyAssessment> getData(int limit) throws UnsupportedEncodingException;
    List<PropertyAssessment> getData(int limit, int offset) throws UnsupportedEncodingException;
}
