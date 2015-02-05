/*
 * Copyright (c) 2015 Exploring Spatial. The MIT License (MIT)
 */

package com.exploringspatial.com.exploringspatial.dao.mapping;

import com.exploringspatial.domain.Conflict;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Steve on 1/16/15.
 */
public class ConflictRowMapper implements RowMapper<Conflict>{
    private final Logger log = Logger.getLogger(ConflictRowMapper.class);
    
    @Override
    public Conflict mapRow(ResultSet resultSet, int i) throws SQLException {
        final Conflict conflict = new Conflict();
        conflict.setActor1(resultSet.getString(ConflictColumns.ACTOR1.name()));
        conflict.setActor2(resultSet.getString(ConflictColumns.ACTOR2.name()));
        conflict.setAdmin1(resultSet.getString(ConflictColumns.ADMIN1.name()));
        conflict.setAdmin2(resultSet.getString(ConflictColumns.ADMIN2.name()));
        conflict.setAdmin3(resultSet.getString(ConflictColumns.ADMIN3.name()));
        conflict.setAllyActor1(resultSet.getString(ConflictColumns.ALLY_ACTOR_1.name()));
        conflict.setAllyActor2(resultSet.getString(ConflictColumns.ALLY_ACTOR_2.name()));
        conflict.setCountry(resultSet.getString(ConflictColumns.COUNTRY.name()));
        final String date = resultSet.getString(ConflictColumns.EVENT_DATE.name());
        if (date != null && !date.isEmpty()) {
            final DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            try {
                conflict.setEventDate(dateFormat.parse(date));
            } catch (ParseException e) {
                log.warn("Could not parse Date: '" + date + "'");
            }
        }
        conflict.setEventIdCountry(resultSet.getString(ConflictColumns.EVENT_ID_CNTY.name()));
        conflict.setEventPk(resultSet.getLong(ConflictColumns.EVENT_ID_NO_CNTY.name()));
        conflict.setEventType(resultSet.getString(ConflictColumns.EVENT_TYPE.name()));
        conflict.setFatalities(resultSet.getInt(ConflictColumns.FATALITIES.name()));
        conflict.setGeoPrecision(resultSet.getInt(ConflictColumns.GEO_PRECIS.name()));
        conflict.setGwno(resultSet.getInt(ConflictColumns.GWNO.name()));
        conflict.setInter1(resultSet.getInt(ConflictColumns.INTER1.name()));
        conflict.setInter2(resultSet.getInt(ConflictColumns.INTER2.name()));
        conflict.setInteraction(resultSet.getInt(ConflictColumns.INTERACTION.name()));
        conflict.setLatitude(resultSet.getDouble(ConflictColumns.LATITUDE.name()));
        conflict.setLongitude(resultSet.getDouble(ConflictColumns.LONGITUDE.name()));
        conflict.setLocation(resultSet.getString(ConflictColumns.LOCATION.name()));
        conflict.setNotes(resultSet.getString(ConflictColumns.NOTES.name()));
        conflict.setSource(resultSet.getString(ConflictColumns.SOURCE.name()));
        conflict.setTimePrecision(resultSet.getInt(ConflictColumns.TIME_PRECISION.name()));
        conflict.setYear(resultSet.getInt(ConflictColumns.YEAR.name()));
        return conflict;
    }
}
