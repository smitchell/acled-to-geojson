/*
 * Copyright (c) 2015 Exploring Spatial. The MIT License (MIT)
 */

package com.exploringspatial.com.exploringspatial.dao.mapping;

import com.exploringspatial.domain.CodeCategory;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Steve on 1/16/15.
 */
public class CodeCategoryRowMapper implements RowMapper<CodeCategory>{
    
    @Override
    public CodeCategory mapRow(ResultSet resultSet, int i) throws SQLException {
        final CodeCategory codeCategory = new CodeCategory();
        codeCategory.setCodeCategoryPk(resultSet.getLong(CodeCategoryColumns.CODE_CATEGORY_PK.name()));
        codeCategory.setCategory(resultSet.getString(CodeCategoryColumns.CATEGORY.name()));
        return codeCategory;
    }
}
