package com.data.migration.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cglib.beans.BeanGenerator;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
@Component
public class DynamicBean {
    private Object object = null; //动态生成的类
    private BeanMap beanMap = null; //存放属性名称以及属性的类型

    public DynamicBean() {
        super();
    }

    public DynamicBean(Map propertyMap) throws JsonProcessingException {
        this.object = generateBean(propertyMap);
        this.beanMap = BeanMap.create(this.object);
    }

    /**
     * @param propertyMap
     * @return
     */
    private Object generateBean(Map propertyMap) throws JsonProcessingException {
        BeanGenerator generator = new BeanGenerator();
        Set keySet = propertyMap.keySet();
        Map<String, Object> map = new HashMap<>();
        map.put("VARCHAR2", String.class);
        map.put("NUMBER", java.math.BigDecimal.class);
        map.put("DATE", java.util.Date.class);
        map.put("BLOB", String.class);
        map.put("CLOB", java.sql.Clob.class);

        for(Iterator i = keySet.iterator(); i.hasNext(); ) {
            String key = i.next().toString();
            generator.addProperty(key, (Class)map.get(propertyMap.get(key)));// CJSJ  Date.class
        }
        return generator.create();
    }

    /**
     * 给bean属性赋值
     * @param property 属性名
     * @param value 值
     */
    public void setValue(Object property, Object value) {
        beanMap.put(property, value);
    }

    /**
     * 通过属性名得到属性值
     * @param property 属性名
     * @return 值
     */
    public Object getValue(String property) {
        return beanMap.get(property);
    }

    /**
     * 得到该实体bean对象
     * @return
     */
    public Object getObject() {
        return this.object;
    }


    public String getValuesByColumn() {
        for(Object key :beanMap.keySet()){

        }
        return "DynamicBean{" +
                "object=" + object +
                ", beanMap=" + beanMap +
                '}';
    }
}
