/*
 * Copyright (c) 2015 Exploring Spatial. The MIT License (MIT)
 */

package com.exploringspatial.dao.impl;

import com.exploringspatial.com.exploringspatial.dao.mapping.CodeCategoryRowMapper;
import com.exploringspatial.dao.CodeCategoryDao;
import com.exploringspatial.domain.CodeCategory;
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
public class CodeCategoryDaoImpl implements CodeCategoryDao {
    private final String selectSQL = "SELECT CODE_CATEGORY_PK, CATEGORY FROM CODE_CATEGORY ";
    
    @Resource
    private DataSource dataSource;
    private NamedParameterJdbcTemplate jdbcTemplate;
    
    @PostConstruct
    public void init() {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public CodeCategory get(final Long codeCategoryPk) {
        final String sql = selectSQL.concat("WHERE CODE_CATEGORY_PK = :codeCategoryPk");
        final MapSqlParameterSource params = new MapSqlParameterSource("codeCategoryPk", codeCategoryPk);
        final List<CodeCategory> results = jdbcTemplate.query(sql, params, new CodeCategoryRowMapper());
        if (results != null && !results.isEmpty()) {
            return results.get(0);
        }
        return null;
    }

    @Override
    public CodeCategory find(final String category) {
        final String sql = selectSQL.concat("WHERE CATEGORY = :category");
        final MapSqlParameterSource params = new MapSqlParameterSource("category", category);
        final List<CodeCategory> results = jdbcTemplate.query(sql, params, new CodeCategoryRowMapper());
        if (results != null && !results.isEmpty()) {
            return results.get(0);
        }
        return null;
    }

    @Override
    public void insert(final CodeCategory instance) {
        final String sql = "INSERT INTO CODE_CATEGORY (CATEGORY) VALUES (:category)";
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        final MapSqlParameterSource params = new MapSqlParameterSource("category", instance.getCategory());
        jdbcTemplate.update(sql, params, keyHolder);
        instance.setCodeCategoryPk(keyHolder.getKey().longValue());
    }

    @Override
    public int update(final CodeCategory instance) {
        final String sql = "UPDATE CODE_CATEGORY SET CATEGORY = :category WHERE CODE_CATEGORY_PK = :codeCategoryPk";
        final MapSqlParameterSource params = new MapSqlParameterSource("codeCategoryPk", instance.getCodeCategoryPk())
                .addValue("category", instance.getCategory());
        return jdbcTemplate.update(sql, params);
    }

    @Override
    public int delete(final Long codeCategoryPk) {
        final String sql = "DELETE FROM CODE_CATEGORY WHERE CODE_CATEGORY_PK = :codeCategoryPk";
        final MapSqlParameterSource params = new MapSqlParameterSource("codeCategoryPk", codeCategoryPk);
        return jdbcTemplate.update(sql, params);
    }

    @Override
    public List<CodeCategory> list() {
        final String sql = selectSQL.concat("ORDER BY CATEGORY");
        return jdbcTemplate.query(sql, new CodeCategoryRowMapper());
    }
}
