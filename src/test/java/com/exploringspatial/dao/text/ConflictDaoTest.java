/*
 * Copyright (c) 2015 Exploring Spatial. The MIT License (MIT)
 */

package com.exploringspatial.dao.text;

import com.exploringspatial.com.exploringspatial.dao.mapping.ConflictRowMapper;
import com.exploringspatial.dao.ConflictDao;
import com.exploringspatial.domain.Conflict;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;

/**
 * Created by Steve on 1/17/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext.xml")
@TransactionConfiguration(defaultRollback = true)
public class ConflictDaoTest {
    private String eventsCsvFile;

    @Resource
    private ConflictDao conflictDao;
    
    @Resource
    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;
    
    @PostConstruct
    public void postConstruct() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        eventsCsvFile = this.getClass().getResource("/ACLEDtestFile.csv").getFile();
    }
    
    @Before
    public void runBefore() {
        jdbcTemplate.update("TRUNCATE TABLE CONFLICT");
    }

    @Test
    public void testBulkLoad() {
        conflictDao.reloadTableFromCsv(eventsCsvFile);
        assertTrue(!conflictDao.listAll().isEmpty());
    }
    
    @Test
    public void testInsert() {
        conflictDao.reloadTableFromCsv(eventsCsvFile);
        final List<Conflict> conflicts = conflictDao.listAll();
        final Conflict conflict = conflicts.get(0);
        jdbcTemplate.update("TRUNCATE TABLE CONFLICT");
        conflictDao.insert(conflict);
        assertNotNull(conflictDao.get(conflict.getEventPk()));
    }
    
    @Test
    public void testDelete() {
        conflictDao.reloadTableFromCsv(eventsCsvFile);
        final List<Conflict> conflicts = conflictDao.listAll();
        final Conflict conflict = conflicts.get(0);
        assertNotNull(conflictDao.get(conflict.getEventPk()));
        conflictDao.delete(conflict.getEventPk());
        assertNull(conflictDao.get(conflict.getEventPk()));
    }
    
    @Test
    public void testGet()  throws ParseException{
        conflictDao.reloadTableFromCsv(eventsCsvFile);
        final List<Conflict> conflicts = conflictDao.listAll();
        final Conflict conflict = conflicts.get(0);
        Conflict match = conflictDao.get(conflict.getEventPk());
        assertNotNull(match);
        assertEquals(new Integer(615), match.getGwno());
        assertEquals("1ALG", match.getEventIdCountry());
        assertEquals(new Long(1), match.getEventPk());
        final DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        assertEquals(df.parse("02/01/1997"), match.getEventDate());
        assertEquals(new Integer(1997), match.getYear());
        assertEquals(new Integer(1), match.getTimePrecision());
        assertEquals("Violence against civilians", match.getEventType());
        assertEquals("GIA: Armed Islamic Group", match.getActor1());
        assertEquals(null, match.getAllyActor1());
        assertEquals(new Integer(2), match.getInter1());
        assertEquals("Civilians (Algeria)", match.getActor2());
        assertEquals(null, match.getAllyActor2());
        assertEquals(new Integer(7), match.getInter2());
        assertEquals(new Integer(27), match.getInteraction());
        assertEquals("Algeria", match.getCountry());
        assertEquals("Blida", match.getAdmin1());
        assertEquals("Blida", match.getAdmin2());
        assertEquals(null, match.getAdmin3());
        assertEquals("Blida", match.getLocation());
        assertEquals(36.4686D, match.getLatitude());
        assertEquals(2.8289, match.getLongitude());
        assertEquals("www.algeria-watch.org", match.getSource());
        assertEquals("4 January: 16 citizens were murdered in the village of Benachour (Blida) by masked men a few hundred meters away from a military camp. It is of note that the citizens of this village had refused to set up armed militias.", conflict.getNotes());
    }
    
    @Test
    public void testSelectDistinctCountry() {
        conflictDao.reloadTableFromCsv(eventsCsvFile);
        List<String> countries = conflictDao.findDistinctCountries();
        assertTrue(!countries.isEmpty());
        assertEquals("Algeria", countries.get(0));
    }


    @Test
    public void testSelectDistinctAdministrativeRegion() {
        conflictDao.reloadTableFromCsv(eventsCsvFile);
        List<String> administrativeRegions = conflictDao.findDistinctAdministrativeRegions("Algeria");
        assertTrue(!administrativeRegions.isEmpty());
        assertEquals("Blida", administrativeRegions.get(0));
    }

    @Test
    public void testFindByCountryAdministrativeRegion() {
        conflictDao.reloadTableFromCsv(eventsCsvFile);
        List<Conflict> conflicts = conflictDao.listAll();
        final Conflict conflict = conflicts.get(0);
        conflicts = conflictDao.findByCountryAdministrativeRegion(conflict.getCountry(), conflict.getAdmin1());
        assertEquals(1, conflicts.size());
        conflicts = conflictDao.findByCountryAdministrativeRegion(conflict.getCountry(), "Made-up Country Name");
        assertTrue(conflicts.isEmpty());
    }


    @Test
    public void testListAll() {
        conflictDao.reloadTableFromCsv(eventsCsvFile);
        final List<Conflict> conflicts = conflictDao.listAll();
        assertEquals(1, conflicts.size());
     }

}
