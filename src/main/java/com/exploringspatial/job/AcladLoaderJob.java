/*
 * Copyright (c) 2015 Exploring Spatial. The MIT License (MIT)
 */

package com.exploringspatial.job;

import com.exploringspatial.dao.CodeCategoryDao;
import com.exploringspatial.dao.CodeDefinitionDao;
import com.exploringspatial.dao.ConflictDao;
import com.exploringspatial.domain.CodeCategory;
import com.exploringspatial.domain.CodeDefinition;
import com.exploringspatial.domain.Conflict;
import com.exploringspatial.geojson.FeatureFactory;
import org.apache.log4j.Logger;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.geojson.feature.FeatureJSON;
import org.opengis.feature.simple.SimpleFeature;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.*;
import java.util.*;

/**
 * Created by Steve on 1/16/15.
 */
@Component
public class AcladLoaderJob {
    private final Logger log = Logger.getLogger(AcladLoaderJob.class);
    
    
    @Resource
    private ConflictDao conflictDao;
    
    @Resource
    private CodeCategoryDao codeCategoryDao;
    
    @Resource
    private CodeDefinitionDao codeDefinitionDao;
    
    @Resource
    private FeatureFactory featureFactory;
    
    @PostConstruct
    public void postConstruct() {
        assert(conflictDao != null);
        assert(codeCategoryDao != null);
        assert(codeDefinitionDao != null);

    }
    
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Error: missing output directory path.");
            System.out.println("Usage: java AcladLoaderJob outputPath eventsCsvFileName");
        }
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("/applicationContext.xml");
        AcladLoaderJob acladLoaderJob = (AcladLoaderJob)applicationContext.getBean("acladLoaderJob");
        try {
            acladLoaderJob.run(args);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void run(String[] args) throws UnsupportedEncodingException {
        final File parentDir = new File(args[0]);
        // First make sure the path they passed in exists.
        if (!parentDir.exists()) {
            throw new IllegalArgumentException("Output directory does not exist: '" + args[1] + "'");
        }
        final File acledDir = new File(parentDir, "acled");
        final String absolutePath = this.getClass().getResource("/".concat(args[1])).getFile();
        conflictDao.reloadTableFromCsv(absolutePath);
        loadCodeDefinitionsTable();
        List<Conflict> countryConflicts;
        File countryDir, adminDir;
        String countryPath, administrativeRegionFilePath, path;
        for (final String countryName: conflictDao.findDistinctCountries()) {
            countryConflicts = new ArrayList<Conflict>();
            countryPath = parsePath(countryName);
            // Then create the sub director if necessary.
            countryDir = new File(acledDir, countryPath);
            if (!countryDir.exists()) {
                countryDir.mkdirs();
            }
            for (final String administrativeRegion : conflictDao.findDistinctAdministrativeRegions(countryName)) {
                administrativeRegionFilePath = parsePath(administrativeRegion);
                path = countryPath.concat("/").concat(administrativeRegionFilePath);
                log.info("Processing path ".concat(path));
                adminDir = new File(acledDir, path);
                if (!adminDir.exists()) {
                    adminDir.mkdirs();
                }
                for (final Conflict conflict : conflictDao.findByCountryAdministrativeRegion(countryName, administrativeRegion)) {
                    conflict.setPath(acledDir.getName().concat("/").concat(path));
                    writeEventGeoJson(conflict, adminDir);
                    countryConflicts.add(conflict);
                }
            }
            writeFeatureCollection(countryConflicts, countryDir);
        }
        writeCodeDefinitions(acledDir);
    }

    private void loadCodeDefinitionsTable() {
        CodeCategory codeCategory = codeCategoryDao.find("COUNTRY");
        for (final String country : conflictDao.findDistinctCountries()) {
            addCodeDefinition(codeCategory, country);
        }
        codeCategory = codeCategoryDao.find("LOCATION");
        for (final String location : conflictDao.findDistinctLocations()) {
            addCodeDefinition(codeCategory, location);
        }
        codeCategory = codeCategoryDao.find("EVENT_TYPE");
        for (final String eventType : conflictDao.findDistinctEventType()) {
            addCodeDefinition(codeCategory, eventType);
        }
        codeCategory = codeCategoryDao.find("ACTOR_TYPE");
        for (final String actor : conflictDao.findDistinctActor1()) {
            addCodeDefinition(codeCategory, actor);
        }
        for (final String actor : conflictDao.findDistinctActor2()) {
            addCodeDefinition(codeCategory, actor);
        }
    }

    private void addCodeDefinition(final CodeCategory codeCategory, final String value) {
        final String definition = parseUnicode(value);
        CodeDefinition codeDefinition = codeDefinitionDao.find(codeCategory.getCodeCategoryPk(), definition);
        if (codeDefinition == null) {
            codeDefinition = new CodeDefinition();
            codeDefinition.setCodeCategoryPk(codeCategory.getCodeCategoryPk());
            codeDefinition.setDefinition(definition);
            codeDefinitionDao.insert(codeDefinition);
        }
    }

    private void writeCodeDefinitions(final File acledDir) {
        for (final CodeCategory codeCategory : codeCategoryDao.list()) {
            writeCodeDefinitions(acledDir, codeCategory);
        }
    }

    private void writeCodeDefinitions(final File acledDir, CodeCategory eventCategory) {
        
        PrintWriter out = null;
        try {
            out = new PrintWriter(new BufferedWriter(new FileWriter(new File(acledDir, eventCategory.getCategory().concat(".json")))));
            out.print("{\"category\": \"".concat(eventCategory.getCategory()).concat("\", \"definitions\": ["));
            
            StringBuilder json;
            int count = 0;
            for (final CodeDefinition codeDefinition : codeDefinitionDao.find(eventCategory.getCodeCategoryPk())) {
                if (count++ > 0) {
                    out.println(",");
                }
                json = new StringBuilder("{");
                json.append("\"codeDefinitionPk\":".concat(codeDefinition.getCodeDefinitionPk().toString()).concat(", "));
                json.append("\"codeCategoryPk\":".concat(codeDefinition.getCodeCategoryPk().toString()));
                if (codeDefinition.getCode() != null) {
                    json.append(", \"code\":".concat(codeDefinition.getCode().toString()));
                }
                if (codeDefinition.getDefinition() != null) {
                    json.append(", \"definition\":\"".concat(codeDefinition.getDefinition()) + "\"");
                }
                json.append("}");;
                out.print(json.toString());
            }
            out.print("]}");
        } catch (IOException e) {
            log.error(e);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    private void writeFeatureCollection(final List<Conflict> conflicts,final File countryDir) {
        final DefaultFeatureCollection defaultFeatureCollection = featureFactory.buildEventCollection(conflicts);
        final FeatureJSON fj = new FeatureJSON();
        OutputStream out = null;
        try {
            out = new BufferedOutputStream(new FileOutputStream(new File(countryDir, "index")));
            fj.writeFeatureCollection(defaultFeatureCollection, out);
        } catch (IOException e) {
            log.error(e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                log.error(e);
            }
        }
    }

    private void writeEventGeoJson(final Conflict conflict, final File countryDir) {
        final SimpleFeature feature = featureFactory.buildEventFeature(conflict);
        final FeatureJSON fj = new FeatureJSON();
        OutputStream out = null;
        try {
            final File jsonFile = new File(countryDir, "" + conflict.getEventPk());
            out = new BufferedOutputStream(new FileOutputStream(jsonFile));
            fj.writeFeature(feature, out);
        } catch (IOException e) {
            log.error(e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                log.error(e);
            }
        }
    }

    private String parsePath(final String s) {
        String stripped = parseUnicode(s);
        return stripped.replaceAll("\\s+", "");
    }

    private String parseUnicode(final String s) {
        String u = null;
        if (s != null) {
            final String unknown = Character.toString((char) 65533);
            u = s.replaceAll(unknown, "");
            // Strip ? \ / : | < > * " linefeed return
            u = u.replaceAll("[\\?\\\\/:|<>\\*\"\n\r]", "");
        }
        return u;
    }
}
