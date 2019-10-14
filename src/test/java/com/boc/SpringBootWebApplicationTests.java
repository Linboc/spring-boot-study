package com.boc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.javassist.expr.NewArray;
import org.apache.tomcat.util.digester.Digester;
import org.apache.tomcat.util.digester.RuleSetBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.xml.sax.SAXException;

//@RunWith(SpringRunner.class)
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

    @Test
    public void shutdownTest() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            File file = new File("C:\\Users\\我什么都不知道\\Desktop\\shutdown.txt"); 
            try {
                OutputStream os = new FileOutputStream(file);
                os.write("hello my's shutdown hook".getBytes());
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
        System.exit(1);
    }

    @Test
    public void digesterTest() throws IOException, SAXException {
        String path = System.getProperty("user.dir") + File.separator + "src/main/resources";
        File file = new File(path, "activiti.cfg.xml");
        Digester d = new Digester();
        d.addRuleSet(new BeansRuleSet());
        Beans beans = (Beans) d.parse(file);
        System.out.println(beans);
    }

    public class BeansRuleSet extends RuleSetBase {

        @Override
        public void addRuleInstances(Digester d) {
            d.addObjectCreate("beans", Beans.class.getName());
            d.addSetProperties("beans");
            d.addObjectCreate("beans/bean", null, "clazz");
            d.addSetProperties("beans/bean");
            d.addSetNext("beans/bean", "setBean", Bean.class.getName());
            d.addObjectCreate("beans/bean/property", Property.class.getName());
            d.addSetProperties("beans/bean/property");
            d.addSetNext("beans/bean/property", "addProperty", Property.class.getName());
        }

    }

    public static class Beans {
        String xmlns;
        Bean bean;
        public String getXmlns() {
            return xmlns;
        }
        public void setXmlns(String xmlns) {
            this.xmlns = xmlns;
        }
        public Bean getBean() {
            return bean;
        }
        public void setBean(Bean bean) {
            this.bean = bean;
        }
        @Override
        public String toString() {
            return "Beans [xmlns=" + xmlns + ", bean=" + bean + "]";
        }
    }

    public static class Bean {
        String id;
        String clazz;
        List<Property> propertys = new ArrayList<>();
        public String getId() {
            return id;
        }
        public void setId(String id) {
            this.id = id;
        }
        public String getClazz() {
            return clazz;
        }
        public void setClazz(String clazz) {
            this.clazz = clazz;
        }
        public void addProperty(Property p) {
            propertys.add(p);
        }
        @Override
        public String toString() {
            return "Bean [id=" + id + ", clazz=" + clazz + ", propertys=" + propertys + "]";
        }
    }

    public static class Property {
        String name;
        String value;
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getValue() {
            return value;
        }
        public void setValue(String value) {
            this.value = value;
        }
        @Override
        public String toString() {
            return "Property [name=" + name + ", value=" + value + "]";
        }
    }

}
