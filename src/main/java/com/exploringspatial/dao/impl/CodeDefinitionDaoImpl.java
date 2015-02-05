/*
 * Copyright (c) 2015 Exploring Spatial. The MIT License (MIT)
 */

package com.exploringspatial.dao.impl;

import com.exploringspatial.com.exploringspatial.dao.mapping.CodeDefinitionRowMapper;
import com.exploringspatial.dao.CodeDefinitionDao;
import com.exploringspatial.domain.CodeDefinition;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.List;

/**
 * @author Steve Mitchell
 * created 1/16/15
 */
@Component
public class CodeDefinitionDaoImpl implements CodeDefinitionDao {
    
    private final String selectSQL = "SELECT CODE_DEFINITION_PK, CODE_CATEGORY_PK, CODE, DEFINITION FROM CODE_DEFINITION ";
    
    @Resource
    private DataSource dataSource;
    private NamedParameterJdbcTemplate jdbcTemplate;
    
    @PostConstruct
    public void init() {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public CodeDefinition get(final Long codeDefinitionPk) {
        final String sql = selectSQL.concat("WHERE CODE_DEFINITION_PK = :codeDefinitionPk");
        final MapSqlParameterSource params = new MapSqlParameterSource("codeDefinitionPk", codeDefinitionPk);
        final List<CodeDefinition> results = jdbcTemplate.query(sql, params, new CodeDefinitionRowMapper());
        if (results != null && !results.isEmpty()) {
            return results.get(0);
        }
        return null;
    }

    @Override
    public CodeDefinition find(final Long codeCategoryPk, final Integer code) {
        final String sql = selectSQL.concat("WHERE CODE_CATEGORY_PK = :codeCategoryPk AND CODE = :code");
        final MapSqlParameterSource params = new MapSqlParameterSource("codeCategoryPk", codeCategoryPk).addValue("code", code);
        final List<CodeDefinition> results = jdbcTemplate.query(sql, params, new CodeDefinitionRowMapper());
        if (results != null && !results.isEmpty()) {
            return results.get(0);
        }
        return null;
    }

    @Override
    public     CodeDefinition find(Long codeCategoryPk, String definition) {
        final String sql = selectSQL.concat("WHERE CODE_CATEGORY_PK = :codeCategoryPk AND DEFINITION = :definition");
        final MapSqlParameterSource params = new MapSqlParameterSource("codeCategoryPk", codeCategoryPk).addValue("definition", definition);
        final List<CodeDefinition> results = jdbcTemplate.query(sql, params, new CodeDefinitionRowMapper());
        if (results != null && !results.isEmpty()) {
            return results.get(0);
        }
        return null;
    }

    @Override
    public     List<CodeDefinition> find(Long codeCategoryPk) {
        final String sql = selectSQL.concat("WHERE CODE_CATEGORY_PK = :codeCategoryPk ORDER BY CODE");
        final MapSqlParameterSource params = new MapSqlParameterSource("codeCategoryPk", codeCategoryPk).addValue("codeCategoryPk", codeCategoryPk);
        return jdbcTemplate.query(sql, params, new CodeDefinitionRowMapper());
    }

    @Override
    public void insert(final CodeDefinition instance) {
        final String sql = "INSERT INTO CODE_DEFINITION (CODE_CATEGORY_PK, CODE, DEFINITION) VALUES (:codeCategoryPk, :code, :definition)";
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        final MapSqlParameterSource params = new MapSqlParameterSource("codeCategoryPk", instance.getCodeCategoryPk())
                .addValue("code", instance.getCode())
                .addValue("definition", instance.getDefinition());
        jdbcTemplate.update(sql, params, keyHolder);
        instance.setCodeDefinitionPk(keyHolder.getKey().longValue());
    }

    @Override
    public int update(final CodeDefinition instance) {
        final String sql = "UPDATE CODE_DEFINITION SET CODE_CATEGORY_PK = :codeCategoryPk, CODE = :code, DEFINITION = :definition WHERE CODE_DEFINITION_PK = :codeDefinitionPk";
        final MapSqlParameterSource params = new MapSqlParameterSource("codeCategoryPk", instance.getCodeCategoryPk())
                .addValue("code", instance.getCode())
                .addValue("definition", instance.getDefinition())
                .addValue("codeDefinitionPk", instance.getCodeDefinitionPk());

        return jdbcTemplate.update(sql, params);
    }

    @Override
    public int delete(final Long codeDefinitionPk) {
        final String sql = "DELETE FROM CODE_DEFINITION WHERE CODE_DEFINITION_PK = :codeDefinitionPk";
        final MapSqlParameterSource params = new MapSqlParameterSource("codeDefinitionPk", codeDefinitionPk);
        return jdbcTemplate.update(sql, params);
    }

    @Override
    public List<CodeDefinition> list() {
        final String sql = selectSQL.concat("ORDER BY CODE_CATEGORY_PK, CODE");
        return jdbcTemplate.query(sql, new CodeDefinitionRowMapper());
    }
}
