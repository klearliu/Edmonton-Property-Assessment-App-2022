package com.macewan305;

import java.util.*;


public class PropertyAssessments {



    /**
     * This function cycles through an array of PropertyAssessment objects and adds their assessment values to
     * an Integer List.
     *
     * @param loadedProperties: An array of Property Assessment objects.
     * @return A list of the collected integers
     */
    public static List<Integer> getAssessmentValues(List<PropertyAssessment> loadedProperties){
        List<Integer> intList = new ArrayList<>();

        for (PropertyAssessment property: loadedProperties){
            intList.add(property.getAssessmentVal());
        }
        return intList;
    }

    /**
     *
     * Each list of property assessments (in the overall List of lists) are assumed to be from different filters. This will
     * create a single List of intersection of all those filters (i.e the ones they have in common).
     *
     * @param listOfProps: A list that contains lists of property assessments
     * @return A list of all the properties that each list (filter type) have in common
     */
    public static List<PropertyAssessment> intersectProperties(List<List<PropertyAssessment>> listOfProps) {

        // Convert to Sets, since Sets are much faster with the retainAll() function

        if (listOfProps.size() == 0) {
            return new ArrayList<>();
        }

        Set<PropertyAssessment> base = new HashSet<>(listOfProps.get(0));

        for(int i = 1; i < listOfProps.size(); i++) {
            Set<PropertyAssessment> compare = new HashSet<>(listOfProps.get(i));
            base.retainAll(compare);
        }

        List<PropertyAssessment> intersection = new ArrayList<>(base);

        return intersection;

    }

    /**
     * Method that removes duplicates of filtered results being copied to export list and displayed in tableview2
     * @param table1List: List of PropertyAssessment objects that is from propData to be displayed in tableview1
     * @param table2List: List of PropertyAssessment objects that is from table2List to be displayed in tableview2
     * @return List of PropertyAssessment objects that contain no duplicates.
     *
     */
    public static List<PropertyAssessment> removeFilteredDuplicates(List<PropertyAssessment> table1List, List<PropertyAssessment> table2List)
    {
        // Put into a set to remove duplicates
        Set<PropertyAssessment> hashSet = new HashSet<>(table2List);
        for (int i = 0; i < table1List.size(); i++)
        {
            hashSet.add(table1List.get(i));
        }
        // Return the set as a list of PropertyAssessment objects
        List<PropertyAssessment> finalTable2List = new ArrayList<>(hashSet);
        return finalTable2List;
    }

    /**
     * Method that adds a SINGLE PropertyAssessment object to 2nd tableview and table2List
     * @param table1Prop: PropertyAssessment object from double-clicking in first tablview
     * @param table2List: List of PropertyAssessment objects for table2List being displayed in 2nd tableview
     * @return List of PropertyAssessment objects that contain no duplicates
     */
    public static List<PropertyAssessment> removeFilteredDuplicates2(PropertyAssessment table1Prop, List<PropertyAssessment> table2List)
    {
        // Put into a set to remove duplicates
        Set<PropertyAssessment> hashSet = new HashSet<>(table2List);

        hashSet.add(table1Prop);

        // Return the set as a list of PropertyAssessment objects
        List<PropertyAssessment> finalTable2List = new ArrayList<>(hashSet);
        return finalTable2List;
    }
}
