/*
 * Copyright (c) 2015 Exploring Spatial. The MIT License (MIT)
 */

package com.exploringspatial.dao.text;

import com.exploringspatial.dao.CodeCategoryDao;
import com.exploringspatial.domain.CodeCategory;
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
public class CodeCategoryDaoTest {

    @Resource
    private CodeCategoryDao codeCategoryDao;

    @Before
    public void setUp() {
        assertNotNull(codeCategoryDao);
    }

    @Test
    public void testInsert() {
        final CodeCategory codeCategory = new CodeCategory();
        codeCategory.setCategory("TEST_1");
        codeCategoryDao.insert(codeCategory);
        assertNotNull(codeCategory.getCodeCategoryPk());
        assertNotNull(codeCategoryDao.get(codeCategory.getCodeCategoryPk()));
    }

    @Test
    public void testGet() {
        final CodeCategory codeCategory = new CodeCategory();
        codeCategory.setCategory("TEST_2");
        codeCategoryDao.insert(codeCategory);
        assertNotNull(codeCategory.getCodeCategoryPk());
        final CodeCategory match = codeCategoryDao.get(codeCategory.getCodeCategoryPk());
        assertEquals(codeCategory.getCodeCategoryPk(), match.getCodeCategoryPk());
        assertEquals(codeCategory.getCategory(), match.getCategory());
    }

    @Test
    public void testFind() {
        final CodeCategory codeCategory = new CodeCategory();
        codeCategory.setCategory("TEST_3");
        codeCategoryDao.insert(codeCategory);
        assertNotNull(codeCategory.getCodeCategoryPk());
        assertNotNull(codeCategoryDao.find(codeCategory.getCategory()));
    }
    
    @Test
    public void testUpdate() {
        final CodeCategory codeCategory = new CodeCategory();
        codeCategory.setCategory("TEST_4");
        codeCategoryDao.insert(codeCategory);
        assertNotNull(codeCategoryDao.get(codeCategory.getCodeCategoryPk()));
        codeCategory.setCategory("TEST_5");
        codeCategoryDao.update(codeCategory);
        final CodeCategory match = codeCategoryDao.get(codeCategory.getCodeCategoryPk());
        assertEquals(codeCategory.getCodeCategoryPk(), match.getCodeCategoryPk());
        assertEquals(codeCategory.getCategory(), match.getCategory());
    }

    @Test
    public void testDelete() {
        final CodeCategory codeCategory = new CodeCategory();
        codeCategory.setCategory("TEST_6");
        codeCategoryDao.insert(codeCategory);
        assertNotNull(codeCategoryDao.get(codeCategory.getCodeCategoryPk()));
        codeCategoryDao.delete(codeCategory.getCodeCategoryPk());
        assertNull(codeCategoryDao.get(codeCategory.getCodeCategoryPk()));
    }
}
