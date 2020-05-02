package net.hlinfo.cloud.demo.mybatis.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface CoreService {
    /**
     * 查询列表，带参数
     * @param mybitsSqlId
     * @param object
     * @return
     */
    public abstract List queryList(String mybitsSqlId, Object object);
    /**
     * 查询列表，带参数
     * @param mybitsSqlId
     * @param Map<String,Object> map
     * @return
     */
    public abstract List queryList(String mybitsSqlId, Map<String,Object> map);
    /**
     * 查询列表，无参数
     * @param mybitsSqlId
     * @return
     */
    public abstract List queryList(String mybitsSqlId);
    /**
     * <strong>Function Name: queryJson</strong>
     * <li><strong>map参数示例(所有参数都可选)：</strong></li>
     * <li>Map<String, Object> map = new HashMap<String, Object>();</li>
     * <li>map.put("start", start);</li>
     * <li>map.put("limit", limit);</li>
     * <li>map.put("keywords", keywords); //模糊查询的值</li>
     * <li>map.put("keywordsField", "T.userloginid,T.userrealname,T.usertel");//模糊查询的字段多个以英文逗号隔开</li>
     * <li>map.put("orderField", "reg_date");//排序字段,默认是主键</li>
     * <li>map.put("deasc", "desc");//排序方式</li>
     * <li>map.put("leftJoinEntity", new DepartmentInfo());//leftjoin实体类实例化</li>
     * <li>map.put("leftField", "ofdepartmentid");//leftjoin左表字段</li>
     * <li>map.put("rightField", "department_id");//leftjoin右表字段</li>
     * <li>map.put("where", whereJson);//where条件</li>
     * <li>whereJson示例:"[{\"logic\":\"and\",\"children\":[{\"logic\":\"and\",\"condition\":\"eq\",\"field\":\"qqq\",\"value\":\"6666\"}]},{\"logic\":\"and\",\"field\":\"hhhh\",\"value\":\"jjjjj\",\"condition\":\"eq\"}]"</li>
     * <li>whereJson的condition参数可以是：like(模糊查询)、eq(等于)、neq(不等于)、gt(大于)、gte(大于等于)、lt(小于)、lte(小于等于)</li>
     * <li>whereJson有children节点，同级的field，value，condition忽略</li>
     * @param object 实体类实例化
     * @param map 参数
     * @return JSON
     * @author 呐喊
     */
    public abstract JSONObject queryJson(Object object, Map<String, Object> map);
    /**
     * <strong>Function Name: queryList</strong>
     * <li><strong>map参数示例(所有参数都可选)：</strong></li>
     * <li>Map<String, Object> map = new HashMap<String, Object>();</li>
     * <li>map.put("start", 0);</li>
     * <li>map.put("limit", 100);</li>
     * <li>map.put("orderField", "reg_date");//排序字段,默认是主键</li>
     * <li>map.put("deasc", "desc");//排序方式</li>
     * <li>map.put("where", "[{\"field\":\"id\",\"condition\":\"eq\",\"value\":\"11\"},{\"field\":\"name\",\"value\":\"0\",\"logic\":\"and\"}]");//where条件，condition可选（默认为eq），logic可选（默认为and）</li>
     * <li>where条件的condition参数可以是：like(模糊查询)、eq(等于)、neq(不等于)、gt(大于)、gte(大于等于)、lt(小于)、lte(小于等于)</li>
     * @param object 实体类实例化
     * @param map 参数
     * @return List
     * @author 呐喊
     */
    public abstract List queryList(Object object, Map<String, Object> map);
    /**
     * <strong>Function Name: queryCount</strong>
     * <li><strong>map参数示例(所有参数都可选)：</strong></li>
     * <li>Map<String, Object> map = new HashMap<String, Object>();</li>
     * <li>map.put("where", "[{\"field\":\"id\",\"condition\":\"eq\",\"value\":\"11\"},{\"field\":\"name\",\"value\":\"0\",\"logic\":\"and\"}]");//where条件，condition可选（默认为eq），logic可选（默认为and）</li>
     * <li>where条件的condition参数可以是：like(模糊查询)、eq(等于)、neq(不等于)、gt(大于)、gte(大于等于)、lt(小于)、lte(小于等于)</li>
     * @param object 实体类实例化
     * @param map 参数
     * @return List
     * @author 呐喊
     */
    public abstract int queryCount(Object object, Map<String, Object> map);
}
