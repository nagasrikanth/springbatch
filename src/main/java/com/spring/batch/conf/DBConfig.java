package com.spring.batch.conf;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class DBConfig {

	@Bean(name = "oracleDb")
	@ConfigurationProperties(prefix = "spring.datasource.oracle")
	public DataSource oracleDataSource() {
		return  DataSourceBuilder.create().build();
	}

	@Bean(name = "oracleJdbcTemplate")
	public JdbcTemplate oracleJdbcTemplate(@Qualifier("oracleDb") DataSource dsOracle) {
		return new JdbcTemplate(dsOracle);
	}
	
}
