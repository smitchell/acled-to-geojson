package com.exploringspatial.dao;

import com.exploringspatial.domain.CodeCategory;

import java.util.List;

/**
 * @author Steve Mitchell
 * created 1/16/15
 */
public interface CodeCategoryDao {
      
    CodeCategory get(Long codeCategoryPk);
    
    CodeCategory find(String category);
    
    void insert(CodeCategory codeCategory);
    
    int update(CodeCategory codeCategory);
    
    int delete(Long codeCategoryPk);
    
    List<CodeCategory> list();
    
}