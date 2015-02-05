/*
 * Copyright (c) 2015 Exploring Spatial. The MIT License (MIT)
 */

package com.exploringspatial.domain;

/**
 * @author Steve Mitchell
 *         created 1/16/15
 */
public class CodeDefinition {
    private Long codeDefinitionPk;
    private Long codeCategoryPk;
    private Integer code;
    private String definition;

    public Long getCodeDefinitionPk() {
        return codeDefinitionPk;
    }

    public void setCodeDefinitionPk(Long codeDefinitionPk) {
        this.codeDefinitionPk = codeDefinitionPk;
    }

    public Long getCodeCategoryPk() {
        return codeCategoryPk;
    }

    public void setCodeCategoryPk(Long codeCategoryPk) {
        this.codeCategoryPk = codeCategoryPk;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(final Integer code) {
        this.code = code;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(final String definition) {
        this.definition = definition;
    }
}
