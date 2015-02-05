/*
 * Copyright (c) 2015 Exploring Spatial. The MIT License (MIT)
 */

package com.exploringspatial.dao;

import com.exploringspatial.domain.Conflict;

import java.util.List;

/**
 * This DAO leverages the CSVREAD feature of the H2 Database Driver to read a CSV file directly in a ResultSet.
 * It requires the name of the CSV file downloaded from http://www.acleddata.com/data. The name is from the
 * root of the classpath. If the file is in /src/main/resources the eventsCsvFile parameter is just the name of 
 * the file. If the file is nested in folders, the those need to be included in the parameter (e.g. "previousYears/ACLED_1999.csv").
 * * 
 * created 1/16/15
 * @author Steve Mitchell 
 */
public interface ConflictDao {
    List<Conflict> listAll();
    List<String> findDistinctCountries();
    List<String> findDistinctEventType();
    List<String> findDistinctActor1();
    List<String> findDistinctActor2();
    List<String> findDistinctLocations();
    List<String> findDistinctAdministrativeRegions(String country);
    List<Conflict> findByCountryAdministrativeRegion(String country, String administrativeRegion);
    Conflict get(Long eventPk);
    void insert(Conflict conflict);
    int update(Conflict conflict);
    int delete(Long eventId);
    void batchUpdate(List<Conflict> conflicts);
    int reloadTableFromCsv(String csvAbsoluteFilePath);
}
