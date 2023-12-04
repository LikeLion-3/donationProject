package com.myapp.warmwave.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

// Config의 경우 특정 클래스를 상속받는 경우도 있고 아닌 경우도 있다.
@Configuration
public class DatabaseConfig {
	
	//클래스내에서 application.properties 파일에 접근하기위해서  
	@Autowired  
	ApplicationContext applicationContext;

	//hikari 설정하기  : application.properties 파일에서 히카리 정보 알아오기 
	//히카리 : 자바에서 제공하는 디비커넥션풀이다. 이걸 사용해야 다량의 디비접속 처리가 가능하고 속도가 빠르다 
//	@Bean  //@Bean 객체라는 의미임
//	@ConfigurationProperties(prefix = "spring.datasource.hikari")
//	public HikariConfig hikariConfig() {
//		// application.properties 의 hikari 설정을 읽어서 객체를 만들어 던져준다.
//		return new HikariConfig();
//	}
//
//	@Bean
//	public DataSource dataSource() {
//		// 설정 객체값으로 데이터소스를 만들어서 반환한다.
//		return new HikariDataSource(hikariConfig());
//	}
	
	@Bean //객체 생성 
	public SqlSessionFactory makeSqlSessionFactory(DataSource dataSource) throws Exception {
		System.out.println("************************");
		//SqlSessionFactory -  Factory 공장객체를 먼저 만들어서 던진다 
		final SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
		
		//저 객체와 application.properties 파일에 있는 datasource 와 연결
		/*
		 * spring.datasource.url=jdbc:oracle:thin:@127.0.0.1:1521:XE
		 * spring.datasource.driver-class-name=oracle.jdbc.driver.OracleDriver
		 * spring.datasource.username=user01
		 * spring.datasource.password=1234
		 */
		factory.setDataSource(dataSource);
		
		//설정파일과 연동하기(mybatis-config.xml)파일과 연동
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		
		//classpath - src/main/resource
		Resource configLocation = resolver.getResource("classpath:mybatis-config.xml");
		
		// 배포버전 만들 때 ~.xml 로 시작하는 모든 파일은 src/main/resources 에 있어야 한다.
		factory.setConfigLocation(configLocation);
		return factory.getObject();
	}

	@Bean 
	public SqlSessionTemplate makeSqlSession(SqlSessionFactory factory) {
		return new SqlSessionTemplate(factory);
	}
}
