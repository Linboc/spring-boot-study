package com.boc;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootWebApplicationTests {

    @Autowired
    private JdbcTemplate template;
    
	@Test
	public void contextLoads() {
	    List<Map<String, Object>> map = template.queryForList("SELECT * FROM USER;");
	    map.forEach(m -> {
	        m.entrySet().forEach(e -> System.out.print(e.getKey() + ":" + e.getValue() + ","));
	        System.out.println();
	    });
	}

}
