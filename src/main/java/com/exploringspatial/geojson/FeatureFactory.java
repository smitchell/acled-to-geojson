/*
 * Copyright (c) 2015 Exploring Spatial. The MIT License (MIT)
 */

package com.exploringspatial.geojson;

import com.exploringspatial.dao.CodeCategoryDao;
import com.exploringspatial.dao.CodeDefinitionDao;
import com.exploringspatial.domain.CodeCategory;
import com.exploringspatial.domain.CodeDefinition;
import com.exploringspatial.domain.Conflict;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Steve on 1/17/15.
 */
@Component
public final class FeatureFactory {
    private final GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
    
    @Resource
    private CodeDefinitionDao codeDefinitionDao;


    @Resource
    private CodeCategoryDao codeCategoryDao;

    public DefaultFeatureCollection buildEventCollection(List<Conflict> conflicts) {
        final DefaultFeatureCollection defaultFeatureCollection = new DefaultFeatureCollection("conflicts", getEventSummaryFeatureSchema());
        List<SimpleFeature> eventSummaryFeatures = new ArrayList<SimpleFeature>();
        int i = 0;
        for (final Conflict conflict : conflicts) {
            eventSummaryFeatures.add(buildEventSummaryFeature(conflict));
        }
        defaultFeatureCollection.addAll(eventSummaryFeatures);
        return defaultFeatureCollection;
    }

    /**
     * The purpose of this method is to generate the schema definition for an conflict summary feature.
     * Due to the large volume, only enough information is included for marker placement and filtering.
     * To reduce JSON file size, property names are the column letter from the spreadsheet and where
     * possible repeating values replaced with code definition ids.
     * <br/>
     * The map of properties is as follows:
     * <ul>
     * <li>c - Column C (EVENT_ID_NO_CNTY)</li>
     * <li>e - Column E (YEAR)</li>
     * <li>g - Column G (EVENT_TYPE)*</li>
     * <li>o - Column O (COUNTRY)*</li>
     * <li>s - Column S (LOCATION)*</li>
     * <li>y - Column Y (FATALITIES)</li>
     * <li>path - Look-up code to the GeoJSON location.</li>
     * </ul>
     * * = column is encoded.
     *
     * @return SimpleFeatureType defining the the feature properties.
     */
    public SimpleFeatureType getEventSummaryFeatureSchema() {
        final SimpleFeatureTypeBuilder simpleFeatureType = new SimpleFeatureTypeBuilder();
        simpleFeatureType.setName("conflicts");
        simpleFeatureType.add("geom", Point.class, DefaultGeographicCRS.WGS84);
        simpleFeatureType.add("eventPk", Integer.class);      // Column C - EVENT_ID_NO_CNTY
        simpleFeatureType.add("eventDate", Integer.class);    // Column E - eventDate
        simpleFeatureType.add("eventTypePk", Integer.class);  // Column G - EVENT_TYPE (encoded)
        simpleFeatureType.add("actor1Pk", Integer.class);     // Column I - ACTOR1 (encoded)
        simpleFeatureType.add("actor2Pk", Integer.class);     // Column L - ACTOR2 (encoded)
        simpleFeatureType.add("countryPk", Integer.class);    // Column O - COUNTRY    (encoded)
        simpleFeatureType.add("locationPk", Integer.class);   // Column S - LOCATION   (encoded)
        simpleFeatureType.add("fatalities", Integer.class);   // Column Y - FATALITIES
        simpleFeatureType.add("path", Integer.class);         // Look-up code to the GeoJSON location.
        return simpleFeatureType.buildFeatureType();
    }

    public SimpleFeatureType getEventDetailFeatureSchema() {
        final SimpleFeatureTypeBuilder simpleFeatureType = new SimpleFeatureTypeBuilder();
        simpleFeatureType.setName("conflict");
        simpleFeatureType.add("geom", Point.class, DefaultGeographicCRS.WGS84);
        simpleFeatureType.add("gwno", Integer.class);         // Column A - GWNO
        simpleFeatureType.add("eventId", Integer.class);      // Column C - EVENT_ID_NO_CNTY
        simpleFeatureType.add("eventDate", Long.class);       // Column D - EVENT_DATE
        simpleFeatureType.add("timePrecision", String.class); // Column F - TIME_PRECISION
        simpleFeatureType.add("year", Integer.class);         // Column E - YEAR
        simpleFeatureType.add("eventType", String.class);     // Column G - EVENT_TYPE
        simpleFeatureType.add("actor1", String.class);        // Column H - ACTOR1
        simpleFeatureType.add("allyActor1", String.class);    // Column I - ALLY_ACTOR1
        simpleFeatureType.add("inter1", Integer.class);       // Column J - INTER1
        simpleFeatureType.add("actor2", String.class);        // Column K - ACTOR2
        simpleFeatureType.add("allyActor2", String.class);    // Column L  - ALLY_ACTOR2
        simpleFeatureType.add("inter2", Integer.class);       // Column M - INTER2
        simpleFeatureType.add("interaction", Integer.class);  // Column N - INTERACTION
        simpleFeatureType.add("country", String.class);       // Column O - COUNTRY
        simpleFeatureType.add("admin1", String.class);        // Column P - ADMIN1 
        simpleFeatureType.add("admin2", String.class);        // Column Q - ADMIN2 
        simpleFeatureType.add("admin3", String.class);        // Column R - ADMIN3
        simpleFeatureType.add("location", String.class);      // Column S - LOCATION 
        simpleFeatureType.add("latitude", Double.class);      // Column T - LATITUDE
        simpleFeatureType.add("longitude", Double.class);     // Column U - LONGITUDE
        simpleFeatureType.add("geoPrecision", Integer.class); // Column V - GEO_PRECIS
        simpleFeatureType.add("source", String.class);        // Column W - SOURCE
        simpleFeatureType.add("notes", String.class);         // Column X - NOTES
        simpleFeatureType.add("fatalities", Integer.class);   // Column Y - FATALITIES
        simpleFeatureType.add("path", String.class); // Look-up code to the GeoJSON location.
        return simpleFeatureType.buildFeatureType();
    }

    /**
     * The purpose of this method is to build a SimpleFeature containing just enough conflict
     * information to allow filtering in the Web browser;
     *
     * @param conflict
     * @return
     */
    public SimpleFeature buildEventFeature(final Conflict conflict) {
        final SimpleFeatureType featureSchema = getEventDetailFeatureSchema();
        final SimpleFeatureBuilder builder = new SimpleFeatureBuilder(featureSchema);
        builder.set("gwno", conflict.getGwno());                         // Column A - GWNO
        builder.set("eventId", conflict.getEventIdCountry());            // Column C - EVENT_ID_NO_CNTY
        if (conflict.getEventDate() != null) {
            builder.set("eventDate", conflict.getEventDate());           // Column D - EVENT_DATE
        }
        builder.set("timePrecision", conflict.getTimePrecision());       // Column F - TIME_PRECISION
        builder.set("year", conflict.getYear());                         // Column E - YEAR
        builder.set("eventType", conflict.getEventType());               // Column G - EVENT_TYPE
        builder.set("actor1", conflict.getActor1());                     // Column H - ACTOR1
        builder.set("allyActor1", conflict.getAllyActor1());             // Column I - ALLY_ACTOR1
        builder.set("inter1", conflict.getInter1());                     // Column J - INTER1
        builder.set("actor2", conflict.getActor2());                     // Column K - ACTOR2
        builder.set("allyActor2", conflict.getAllyActor2());             // Column L  - ALLY_ACTOR2
        builder.set("inter2", conflict.getInter2());                     // Column M - INTER2
        builder.set("interaction", conflict.getInteraction());           // Column N - INTERACTION
        builder.set("country", conflict.getCountry());                   // Column O - COUNTRY
        builder.set("admin1", conflict.getAdmin1());                     // Column P - ADMIN1
        builder.set("admin2", conflict.getAdmin2());                     // Column Q - ADMIN2
        builder.set("admin3", conflict.getAdmin3());                     // Column R - ADMIN3
        builder.set("location", conflict.getLocation());                 // Column S - LOCATION
        builder.set("latitude", conflict.getLatitude());                 // Column T - LATITUDE
        builder.set("longitude", conflict.getLongitude());               // Column U - LONGITUDE
        builder.set("geoPrecision", conflict.getGeoPrecision());         // Column V - GEO_PRECIS
        builder.set("source", conflict.getSource());                     // Column W - SOURCE
        builder.set("notes", conflict.getNotes());                       // Column X - NOTES
        builder.set("fatalities", conflict.getFatalities());             // Column Y - FATALITIES
        builder.set("path", conflict.getPath());                   // Column Y - FATALITIES
        builder.add(geometryFactory.createPoint(new Coordinate(conflict.getLongitude(), conflict.getLatitude())));
        return builder.buildFeature("" + conflict.getEventPk());
    }

    /**
     * The purpose of this method is to build a SimpleFeature containing just enough conflict
     * information to allow filtering in the Web browser;
     *
     * @param conflict
     * @return
     */
    public SimpleFeature buildEventSummaryFeature(final Conflict conflict) {
        final SimpleFeatureType featureSchema = getEventSummaryFeatureSchema();
        final SimpleFeatureBuilder builder = new SimpleFeatureBuilder(featureSchema);
        builder.set("eventPk", conflict.getEventPk());
        builder.set("eventDate", conflict.getEventDate());
        final CodeCategory actorCategory = codeCategoryDao.find("ACTOR_TYPE");
        CodeDefinition codeDefinition = codeDefinitionDao.find(actorCategory.getCodeCategoryPk(), conflict.getActor1());
        if (codeDefinition != null) {
            builder.set("actor1Pk", codeDefinition.getCodeDefinitionPk());
        } else {
            builder.set("actor1Pk", -1);
        }
        codeDefinition = codeDefinitionDao.find(actorCategory.getCodeCategoryPk(), conflict.getActor2());
        if (codeDefinition != null) {
            builder.set("actor2Pk", codeDefinition.getCodeDefinitionPk());
        } else {
            builder.set("actor2Pk", -1);
        }
        final CodeCategory eventCategory = codeCategoryDao.find("EVENT_TYPE");
        
        codeDefinition = codeDefinitionDao.find(eventCategory.getCodeCategoryPk(), conflict.getEventType());
        if (codeDefinition != null) {
            builder.set("eventTypePk", codeDefinition.getCodeDefinitionPk());
        } else {
            builder.set("eventTypePk", -1);
        }
        
        final CodeCategory countryCategory = codeCategoryDao.find("COUNTRY");
        codeDefinition = codeDefinitionDao.find(countryCategory.getCodeCategoryPk(), conflict.getCountry());
        if (codeDefinition != null) {
            builder.set("countryPk", codeDefinition.getCodeDefinitionPk());
        } else {
            builder.set("countryPk", -1);
        }
       
        final CodeCategory locationCategory = codeCategoryDao.find("LOCATION");
        codeDefinition = codeDefinitionDao.find(locationCategory.getCodeCategoryPk(), conflict.getLocation());
        if (codeDefinition != null) {
            builder.set("locationPk", codeDefinition.getCodeDefinitionPk());
        } else {
            builder.set("locationPk", -1);
        }
        if (conflict.getFatalities() != null) {
            builder.set("fatalities", conflict.getFatalities());
        } else {
            builder.set("fatalities", 0);
        }
        final CodeCategory pathCategory = codeCategoryDao.find("PATH");
        codeDefinition = codeDefinitionDao.find(pathCategory.getCodeCategoryPk(), conflict.getPath());
        if (codeDefinition == null) {
            codeDefinition = new CodeDefinition();
            codeDefinition.setCodeCategoryPk(pathCategory.getCodeCategoryPk());
            codeDefinition.setDefinition(conflict.getPath());
            List<CodeDefinition> existingDefinitions = codeDefinitionDao.find(pathCategory.getCodeCategoryPk());
            if (existingDefinitions.isEmpty()) {
                codeDefinition.setCode(1);
            } else {
                codeDefinition.setCode(existingDefinitions.get(existingDefinitions.size() - 1).getCode() + 1);
            }
            codeDefinitionDao.insert(codeDefinition);
        }
        builder.set("path", codeDefinition.getCodeDefinitionPk());
        builder.add(geometryFactory.createPoint(new Coordinate(conflict.getLongitude(), conflict.getLatitude())));
        return builder.buildFeature("" + conflict.getEventPk());
    }
}
