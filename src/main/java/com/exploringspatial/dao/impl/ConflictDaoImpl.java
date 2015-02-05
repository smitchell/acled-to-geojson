/*
 * Copyright (c) 2015 Exploring Spatial. The MIT License (MIT)
 */

package com.exploringspatial.dao.impl;

import com.exploringspatial.com.exploringspatial.dao.mapping.ConflictRowMapper;
import com.exploringspatial.dao.ConflictDao;
import com.exploringspatial.domain.Conflict;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * This DAO leverages the CSVREAD feature of the H2 Database Driver to read a CSV file directly in a ResultSet.
 * It requires the name of the CSV file downloaded from http://www.aceleddata.com/data. The name is from the
 * root of the classpath. If the file is in /src/main/resources the eventsCsvFile parameter is just the name of
 * the file. If the file is nested in folders, the those need to be included in the parameter (e.g. "previousYears/ACLED_1999.csv").
 * *
 * created 1/16/15
 *
 * @author Steve Mitchell
 */
@Component
public class ConflictDaoImpl implements ConflictDao {
    private String selectSql;
    private String insertSql;
    private String batchUpdateSql;

    @Resource
    private DataSource dataSource;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private JdbcTemplate jdbcTemplate;

    public ConflictDaoImpl() {
        super();
    }

    @PostConstruct
    public void init() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        selectSql = "SELECT  "
                .concat("GWNO, ")
                .concat("EVENT_ID_CNTY, ")
                .concat("EVENT_ID_NO_CNTY, ")
                .concat("EVENT_DATE, ")
                .concat("YEAR, ")
                .concat("TIME_PRECISION, ")
                .concat("EVENT_TYPE, ")
                .concat("ACTOR1, ")
                .concat("ALLY_ACTOR_1, ")
                .concat("INTER1, ")
                .concat("ACTOR2, ")
                .concat("ALLY_ACTOR_2, ")
                .concat("INTER2, ")
                .concat("INTERACTION, ")
                .concat("COUNTRY, ")
                .concat("ADMIN1, ")
                .concat("ADMIN2, ")
                .concat("ADMIN3, ")
                .concat("LOCATION, ")
                .concat("LATITUDE, ")
                .concat("LONGITUDE, ")
                .concat("GEO_PRECIS, ")
                .concat("SOURCE, ")
                .concat("NOTES, ")
                .concat("FATALITIES FROM CONFLICT ");
        insertSql = "INSERT INTO CONFLICT ("
                .concat("GWNO, ")
                .concat("EVENT_ID_CNTY, ")
                .concat("EVENT_ID_NO_CNTY, ")
                .concat("EVENT_DATE, ")
                .concat("YEAR, ")
                .concat("TIME_PRECISION, ")
                .concat("EVENT_TYPE, ")
                .concat("ACTOR1, ")
                .concat("ALLY_ACTOR_1, ")
                .concat("INTER1, ")
                .concat("ACTOR2, ")
                .concat("ALLY_ACTOR_2, ")
                .concat("INTER2, ")
                .concat("INTERACTION, ")
                .concat("COUNTRY, ")
                .concat("ADMIN1, ")
                .concat("ADMIN2, ")
                .concat("ADMIN3, ")
                .concat("LOCATION, ")
                .concat("LATITUDE, ")
                .concat("LONGITUDE, ")
                .concat("GEO_PRECIS, ")
                .concat("SOURCE, ")
                .concat("NOTES, ")
                .concat("FATALITIES) VALUES (:gwno, :event_id_cnty, :eventId, :eventDate, :year, :timePrecision, :eventType, ")
                .concat(":actor1, :allyActor1, :inter1, :actor2, :allyActor2, :inter2, :interaction, :country, ")
                .concat(":admin1, :admin2, :admin3, :location, :latitude, :longitude, :geoPrecision, :source, ")
                .concat(":notes, :fatalities )");
        batchUpdateSql = "INSERT INTO CONFLICT ("
                .concat("GWNO, ")
                .concat("EVENT_ID_CNTY, ")
                .concat("EVENT_ID_NO_CNTY, ")
                .concat("EVENT_DATE, ")
                .concat("YEAR, ")
                .concat("TIME_PRECISION, ")
                .concat("EVENT_TYPE, ")
                .concat("ACTOR1, ")
                .concat("ALLY_ACTOR_1, ")
                .concat("INTER1, ")
                .concat("ACTOR2, ")
                .concat("ALLY_ACTOR_2, ")
                .concat("INTER2, ")
                .concat("INTERACTION, ")
                .concat("COUNTRY, ")
                .concat("ADMIN1, ")
                .concat("ADMIN2, ")
                .concat("ADMIN3, ")
                .concat("LOCATION, ")
                .concat("LATITUDE, ")
                .concat("LONGITUDE, ")
                .concat("GEO_PRECIS, ")
                .concat("SOURCE, ")
                .concat("NOTES, ")
                .concat("FATALITIES) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )");
    }

    @Override
    public Conflict get(Long eventPk) {
        final String sql = selectSql.concat(" WHERE EVENT_ID_NO_CNTY = :eventPk");
        final MapSqlParameterSource params = new MapSqlParameterSource("eventPk", eventPk);
        List<Conflict> conflicts = namedParameterJdbcTemplate.query(sql, params, new ConflictRowMapper());
        if (conflicts.isEmpty()) {
            return null;
        }
        return conflicts.get(0);
    }

    @Override
    public List<Conflict> listAll() {
        final String sql = selectSql.concat(" ORDER BY COUNTRY_LOWERCASE, EVENT_ID_NO_CNTY");
        return namedParameterJdbcTemplate.query(sql, new ConflictRowMapper());
    }

    @Override
    public List<String> findDistinctCountries() {
        final String sql = "SELECT DISTINCT COUNTRY FROM CONFLICT ORDER BY COUNTRY";
        return jdbcTemplate.queryForList(sql, String.class);
    }

    @Override
    public List<String> findDistinctEventType() {
        final String sql = "SELECT DISTINCT EVENT_TYPE FROM CONFLICT ORDER BY EVENT_TYPE";
        return jdbcTemplate.queryForList(sql, String.class);
    }

    @Override
    public List<String> findDistinctActor1() {
        final String sql = "SELECT DISTINCT ACTOR1 FROM CONFLICT ORDER BY ACTOR1";
        return jdbcTemplate.queryForList(sql, String.class);
    }

    @Override
    public List<String> findDistinctActor2() {
        final String sql = "SELECT DISTINCT ACTOR2 FROM CONFLICT ORDER BY ACTOR2";
        return jdbcTemplate.queryForList(sql, String.class);
    }

    public List<String> findDistinctLocations() {
        final String sql = "SELECT DISTINCT LOCATION FROM CONFLICT ORDER BY LOCATION";
        return jdbcTemplate.queryForList(sql, String.class);
    }
    
    @Override
    public List<String> findDistinctAdministrativeRegions(String country) {
        final String sql = "SELECT DISTINCT ADMIN1 FROM CONFLICT WHERE COUNTRY_LOWERCASE = LOWER(:country) ORDER BY ADMIN1";
        final MapSqlParameterSource params = new MapSqlParameterSource("country", country);
        return namedParameterJdbcTemplate.queryForList(sql, params, String.class);
    }

    @Override
    public List<Conflict> findByCountryAdministrativeRegion(String country, String administrativeRegion) {
        final String sql = selectSql.concat(" WHERE COUNTRY_LOWERCASE = LOWER(:country) AND ADMIN1_LOWERCASE = LOWER(:administrativeRegion) ORDER BY EVENT_ID_NO_CNTY");
        final MapSqlParameterSource params = new MapSqlParameterSource("country", country).addValue("administrativeRegion", administrativeRegion);
        return namedParameterJdbcTemplate.query(sql, params, new ConflictRowMapper());
    }

    @Override
    public void insert(final Conflict instance) {
        final MapSqlParameterSource params = buildMapSqlParameterSource(instance);
        namedParameterJdbcTemplate.update(insertSql, params);
    }

    private MapSqlParameterSource buildMapSqlParameterSource(Conflict instance) {
        final DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        final MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("gwno", instance.getGwno());
        mapSqlParameterSource.addValue("event_id_cnty", instance.getEventIdCountry());
        mapSqlParameterSource.addValue("eventId", instance.getEventPk());
        mapSqlParameterSource.addValue("eventDate", df.format(instance.getEventDate()));
        mapSqlParameterSource.addValue("year", instance.getYear());
        mapSqlParameterSource.addValue("timePrecision", instance.getTimePrecision());
        mapSqlParameterSource.addValue("eventType", instance.getEventType());
        mapSqlParameterSource.addValue("actor1", instance.getActor1());
        mapSqlParameterSource.addValue("allyActor1", instance.getAllyActor1());
        mapSqlParameterSource.addValue("inter1", instance.getInter1());
        mapSqlParameterSource.addValue("actor2", instance.getActor2());
        mapSqlParameterSource.addValue("allyActor2", instance.getAllyActor2());
        mapSqlParameterSource.addValue("inter2", instance.getInter2());
        mapSqlParameterSource.addValue("interaction", instance.getInteraction());
        mapSqlParameterSource.addValue("country", instance.getCountry());
        mapSqlParameterSource.addValue("admin1", instance.getAdmin1());
        mapSqlParameterSource.addValue("admin2", instance.getAdmin2());
        mapSqlParameterSource.addValue("admin3", instance.getAdmin3());
        mapSqlParameterSource.addValue("location", instance.getLocation());
        mapSqlParameterSource.addValue("latitude", instance.getLatitude());
        mapSqlParameterSource.addValue("longitude", instance.getLongitude());
        mapSqlParameterSource.addValue("geoPrecision", instance.getGwno());
        mapSqlParameterSource.addValue("source", instance.getSource());
        mapSqlParameterSource.addValue("notes", instance.getNotes());
        mapSqlParameterSource.addValue("fatalities", instance.getFatalities());
        return mapSqlParameterSource;
    }

    @Override
    public int update(final Conflict instance) {
        final String sql = "UPDATE CONFLICT SET "
                .concat("GWNO = :gwno, ")
                .concat("EVENT_ID_CNTY = :event_id_cnty, ")
                .concat("EVENT_DATE = :eventDate, ")
                .concat("YEAR = :year, ")
                .concat("TIME_PRECISION = :timePrecision, ")
                .concat("EVENT_TYPE :eventType, ")
                .concat("ACTOR1 = :actor1, ")
                .concat("ALLY_ACTOR_1 = :allyActor1, ")
                .concat("INTER1 = :inter1, ")
                .concat("ACTOR2 = :actor2, ")
                .concat("ALLY_ACTOR_2 = :allyActor2, ")
                .concat("INTER2 = :inter2, ")
                .concat("INTERACTION = :interaction, ")
                .concat("COUNTRY = :country, ")
                .concat("ADMIN1 = :admin1, ")
                .concat("ADMIN2 = :admin2, ")
                .concat("ADMIN3 = :admin3, ")
                .concat("LOCATION = :location, ")
                .concat("LATITUDE = :latitude, ")
                .concat("LONGITUDE = :longitude, ")
                .concat("GEO_PRECIS = :geoPrecision, ")
                .concat("SOURCE = :source, ")
                .concat("NOTES = :notes, ")
                .concat("FATALITIES = :fatalities ")
                .concat(" WHERE EVENT_ID_NO_CNTY = :eventId, ");
        final MapSqlParameterSource params = buildMapSqlParameterSource(instance);
        return namedParameterJdbcTemplate.update(sql, params);
    }

    @Override
    public int delete(final Long eventId) {
        final String sql = "DELETE FROM CONFLICT WHERE EVENT_ID_NO_CNTY = :eventId ";
        final MapSqlParameterSource params = new MapSqlParameterSource("eventId", eventId);
        return namedParameterJdbcTemplate.update(sql, params);
    }

    @Override
    public int reloadTableFromCsv(final String csvAbsoluteFilePath) {
        try {
            assert (!csvAbsoluteFilePath.isEmpty());
            jdbcTemplate.update("TRUNCATE TABLE CONFLICT");
            final int batchSize = 1000;
            final List<Conflict> conflicts = jdbcTemplate.query("SELECT * FROM CSVREAD('".concat(csvAbsoluteFilePath).concat("')"), new ConflictRowMapper());
            final List<Conflict> batch = new ArrayList<Conflict>(batchSize);
            for (Conflict conflict : conflicts) {
                batch.add(conflict);
                if (batch.size() == batchSize) {
                    batchUpdate(batch);
                    batch.clear();
                }
            }
            if (!batch.isEmpty()) {
                batchUpdate(batch);
                batch.clear();
            }
            return conflicts.size();
        } catch (Exception e) {
            throw new RuntimeException("Could not load " + csvAbsoluteFilePath, e);
        }
    }

    @Override
    public void batchUpdate(List<Conflict> conflicts) {

        jdbcTemplate.batchUpdate(batchUpdateSql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                final DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                final Conflict instance = conflicts.get(i);
                ps.setLong(1, instance.getGwno());
                ps.setString(2, instance.getEventIdCountry());
                ps.setLong(3, instance.getEventPk());
                ps.setString(4, df.format(instance.getEventDate()));
                ps.setLong(5, instance.getYear());
                ps.setLong(6, instance.getTimePrecision());
                ps.setString(7, instance.getEventType());
                ps.setString(8, instance.getActor1());
                ps.setString(9, instance.getAllyActor1());
                ps.setLong(10, instance.getInter1());
                ps.setString(11, instance.getActor2());
                ps.setString(12, instance.getAllyActor2());
                ps.setLong(13, instance.getInter2());
                ps.setLong(14, instance.getInteraction());
                ps.setString(15, instance.getCountry());
                ps.setString(16, instance.getAdmin1());
                ps.setString(17, instance.getAdmin2());
                ps.setString(18, instance.getAdmin3());
                ps.setString(19, instance.getLocation());
                ps.setDouble(20, instance.getLatitude());
                ps.setDouble(21, instance.getLongitude());
                ps.setLong(22, instance.getGwno());
                ps.setString(23, instance.getSource());
                ps.setString(24, instance.getNotes());
                ps.setLong(25, instance.getFatalities());
            }

            @Override
            public int getBatchSize() {
                return conflicts.size();
            }
        });

    }

}
