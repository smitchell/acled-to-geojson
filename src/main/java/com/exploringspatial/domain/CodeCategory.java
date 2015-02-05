/*
 * Copyright (c) 2015 Exploring Spatial. The MIT License (MIT)
 */

package com.exploringspatial.domain;

/**
 * created 1/16/15
 * @author Steve Mitchell
 */
public class CodeCategory {
    private Long codeCategoryPk;
    private String category;
    
    public CodeCategory() {
        super();
    }
   
    public CodeCategory(final Long codeCategoryPk, final String category) {
        super();
        this.codeCategoryPk = codeCategoryPk;
        this.category = category;
    }

    public Long getCodeCategoryPk() {
        return codeCategoryPk;
    }

    public void setCodeCategoryPk(Long codeCategoryPk) {
        this.codeCategoryPk = codeCategoryPk;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
