/*
 * Copyright (c) 2015 Exploring Spatial. The MIT License (MIT)
 */

package com.exploringspatial.dao;

import com.exploringspatial.domain.CodeDefinition;

import java.util.List;

/**
 * @author Steve Mitchell
 * created 1/16/15
 */
public interface CodeDefinitionDao {
      
    CodeDefinition get(Long codeDefinitionPk);
    
    CodeDefinition find(Long codeCategoryPk, Integer code);

    CodeDefinition find(Long codeCategoryPk, String definition);   
    
    List<CodeDefinition> find(Long codeCategoryPk);
    
    void insert(CodeDefinition codeDefinition);
    
    int update(CodeDefinition codeDefinition);
    
    int delete(Long codeDefinitionPk);
    
    List<CodeDefinition> list();
    
}