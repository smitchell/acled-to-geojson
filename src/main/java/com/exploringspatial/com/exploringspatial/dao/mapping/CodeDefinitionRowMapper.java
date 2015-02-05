/*
 * Copyright (c) 2015 Exploring Spatial. The MIT License (MIT)
 */

package com.exploringspatial.com.exploringspatial.dao.mapping;

import com.exploringspatial.domain.CodeDefinition;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Steve on 1/16/15.
 */
public class CodeDefinitionRowMapper implements RowMapper<CodeDefinition>{
    
    @Override
    public CodeDefinition mapRow(ResultSet resultSet, int i) throws SQLException {
        final CodeDefinition codeDefinition = new CodeDefinition();
        codeDefinition.setCodeDefinitionPk(resultSet.getLong(CodeDefinitionColumns.CODE_DEFINITION_PK.name()));
        codeDefinition.setCodeCategoryPk(resultSet.getLong(CodeDefinitionColumns.CODE_CATEGORY_PK.name()));
        codeDefinition.setCode(resultSet.getInt(CodeDefinitionColumns.CODE.name()));
        codeDefinition.setDefinition(resultSet.getString(CodeDefinitionColumns.DEFINITION.name()));
        return codeDefinition;
    }
}
