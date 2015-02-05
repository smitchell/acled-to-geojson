/*
 * Copyright (c) 2015 Exploring Spatial. The MIT License (MIT)
 */

package com.exploringspatial.dao.text;

import com.exploringspatial.dao.CodeDefinitionDao;
import com.exploringspatial.domain.CodeDefinition;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import javax.annotation.Resource;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext.xml")
@TransactionConfiguration(defaultRollback = true)
public class CodeDefinitionDaoTest {

    @Resource
    private CodeDefinitionDao codeDefinitionDao;

    @Before
    public void setUp() {
        assertNotNull(codeDefinitionDao);
    }

    @Test
    public void testInsert() {
        final CodeDefinition codeDefinition = new CodeDefinition();
        codeDefinition.setCodeCategoryPk(1L);
        codeDefinition.setCode(1);
        codeDefinition.setDefinition("TEST_1");
        codeDefinitionDao.insert(codeDefinition);
        assertNotNull(codeDefinition.getCodeDefinitionPk());
        assertNotNull(codeDefinitionDao.get(codeDefinition.getCodeDefinitionPk()));
    }

    @Test
    public void testGet() {
        final CodeDefinition codeDefinition = new CodeDefinition();
        codeDefinition.setCodeCategoryPk(1L);
        codeDefinition.setCode(1);
        codeDefinition.setDefinition("TEST_2");
        codeDefinitionDao.insert(codeDefinition);
        assertNotNull(codeDefinition.getCodeDefinitionPk());
        final CodeDefinition match = codeDefinitionDao.get(codeDefinition.getCodeDefinitionPk());
        assertEquals(codeDefinition.getCodeDefinitionPk(), match.getCodeDefinitionPk());
        assertEquals(codeDefinition.getDefinition(), match.getDefinition());
    }

    @Test
    public void testFind() {
        final CodeDefinition codeDefinition = new CodeDefinition();
        codeDefinition.setCodeCategoryPk(1L);
        codeDefinition.setCode(1);
        codeDefinition.setDefinition("TEST_3");
        codeDefinitionDao.insert(codeDefinition);
        assertNotNull(codeDefinition.getCodeDefinitionPk());
        assertNotNull(codeDefinitionDao.find(codeDefinition.getCodeCategoryPk(), codeDefinition.getCode()));
    }
    
    @Test
    public void testUpdate() {
        final CodeDefinition codeDefinition = new CodeDefinition();
        codeDefinition.setCodeCategoryPk(1L);
        codeDefinition.setCode(1);
        codeDefinition.setDefinition("TEST_4");
        codeDefinitionDao.insert(codeDefinition);
        assertNotNull(codeDefinitionDao.get(codeDefinition.getCodeDefinitionPk()));
        codeDefinition.setDefinition("TEST_5");
        codeDefinitionDao.update(codeDefinition);
        final CodeDefinition match = codeDefinitionDao.get(codeDefinition.getCodeDefinitionPk());
        assertEquals(codeDefinition.getCodeDefinitionPk(), match.getCodeDefinitionPk());
        assertEquals(codeDefinition.getDefinition(), match.getDefinition());
    }

    @Test
    public void testDelete() {
        final CodeDefinition codeDefinition = new CodeDefinition();
        codeDefinition.setCodeCategoryPk(1L);
        codeDefinition.setCode(1);
        codeDefinition.setDefinition("TEST_6");
        codeDefinitionDao.insert(codeDefinition);
        assertNotNull(codeDefinitionDao.get(codeDefinition.getCodeDefinitionPk()));
        codeDefinitionDao.delete(codeDefinition.getCodeDefinitionPk());
        assertNull(codeDefinitionDao.get(codeDefinition.getCodeDefinitionPk()));
    }
}
