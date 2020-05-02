package net.hlinfo.cloud.admin.mybatis.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.hlinfo.cloud.admin.mybatis.dao.CoreDao;
import net.hlinfo.cloud.admin.mybatis.service.CoreService;
import net.hlinfo.opt.Funs;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

@Service
public class CoreServiceImpl implements CoreService {
    @Resource
    private CoreDao coreDao;

    public List queryList(String mybitsSqlId) {
        return this.coreDao.queryList(mybitsSqlId);
    }

    public List queryList(String mybitsSqlId, Object object) {
        return this.coreDao.queryList(mybitsSqlId, object);
    }

    public JSONObject queryJson(Object object,Map<String, Object> m){
        Map<String, Object> map=new HashMap<String, Object>();
        map.put("start", m.get("start")==null?0: Funs.string2int(m.get("start")+""));
        map.put("limit", m.get("limit")==null?200:Funs.string2int(m.get("limit")+""));
        //Zqh_info tbgr = new Zqh_info();
        String tableName = Funs.getTableName(object, true);
        map.put("tableName", tableName);
        Field field[] = object.getClass().getDeclaredFields();
        List<Object> fieldList = new ArrayList<Object>();
        Map<String, Object> TF = new HashMap<String, Object>();
        for(Field f:field){
            fieldList.add("T."+f.getName());
            TF.put(f.getName(), f.getName());
        }
        if(m.get("leftJoinEntity")!=null) {
            Field leftJoinfield[] = m.get("leftJoinEntity").getClass().getDeclaredFields();
            for(Field lf:leftJoinfield){
                if(TF.containsKey(lf.getName())) {
                    fieldList.add("R."+lf.getName()+" as R"+lf.getName());
                }else {
                    fieldList.add("R."+lf.getName());
                }
            }
        }
        map.put("fieldList", fieldList.toArray());

        map.put("leftJoinTableName", m.get("leftJoinEntity")==null?null:Funs.getTableName(m.get("leftJoinEntity"),true));
        map.put("leftField", m.get("leftField"));
        map.put("rightField", m.get("rightField"));
        if(m.get("keywords")!=null && m.get("keywordsField")!=null) {
            map.put("keywords", m.get("keywords"));
            List<Object> kwdfieldList = new ArrayList<Object>();
            String[] wdf = m.get("keywordsField").toString().split(",");
            for(int i=0;i<wdf.length;i++){
                kwdfieldList.add(wdf[i]);
            }
            map.put("keywordsField", kwdfieldList.toArray());
        }
        Object pk = null;
        try {
            Method method = object.getClass().getMethod("getPrimaryKey");
            pk = method.invoke(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put("orderField", m.get("orderField")==null?pk:m.get("orderField"));
        map.put("deasc", m.get("deasc")==null?"asc":(("asc".equals(m.get("deasc").toString().toLowerCase()) || "desc".equals(m.get("deasc").toString().toLowerCase()))?m.get("deasc").toString():"asc"));
        map.put("list", m.get("list"));

        if(m.get("where")!=null && !"".equals(m.get("where"))) {
            List<?> wherelist = this.convert2Query(m.get("where").toString());
            map.put("list", wherelist);
        }

        //System.out.println("Map:\n"+map);
        List list = this.coreDao.queryList("query_public_list",map);
        int C = this.coreDao.queryCount("query_public_list_count", map);
        JSONObject json=new JSONObject();
        json.put("code", 0);
        json.put("msg", "");
        json.put("count", C);
        json.put("data", list);
        return json;
    }
    /**
     * 查询列表
     * @param object
     * @param m
     * @return
     */
    public List queryList(Object object,Map<String, Object> m) {
        Map<String, Object> map=new HashMap<String, Object>();
        String tableName = Funs.getTableName(object, true);
        map.put("tableName", tableName);
        Field field[] = object.getClass().getDeclaredFields();
        List<Object> fieldList = new ArrayList<Object>();
        for(Field f:field){
            fieldList.add("T."+f.getName());
        }
        map.put("fieldList", fieldList.toArray());
        HashMap<String, String> condMap = new HashMap<String, String>();
        condMap.put("condi", "like,eq,neq,gt,lt,gte,lte");
        if(m.get("where")!=null && !"".equals(m.get("where"))) {
            //[{"field":"id","condition":"=","value":"11"},{"field":"name","value":"0","logic":"and"}]
            String whereobj = m.get("where").toString();
            List<Object> list = new ArrayList<Object>();
            JSONArray js = JSONArray.parseArray(whereobj);
            for(int i=0;i<js.size();i++) {
                JSONObject jo = JSONObject.parseObject(js.getString(i));
                Map<String, String> mm=new HashMap<String, String>();
                mm.put("field", "T."+jo.getString("field"));
                if(jo.containsKey("condition")) {
                    String ct = jo.getString("condition");
                    if(!Arrays.<String>asList(condMap.get("condi").split(",")).contains(jo.getString("condition"))) {
                        ct="eq";
                    }
                    mm.put("condition", ct);
                }else {
                    mm.put("condition", "eq");
                }
                mm.put("value", jo.getString("value"));

                if(i>0 && jo.containsKey("logic")) {
                    if(jo.getString("logic")=="and" || "and".equals(jo.getString("logic"))) {
                        mm.put("logic", jo.getString("logic"));
                    }else if(jo.getString("logic")=="or" || "or".equals(jo.getString("logic"))) {
                        mm.put("logic", jo.getString("logic"));
                    }else {
                        mm.put("logic", "and");
                    }
                }

                list.add(mm);
            }
            map.put("whereList", list.toArray());
        }
        Object pk = null;
        try {
            Method method = object.getClass().getMethod("getPrimaryKey");
            pk = method.invoke(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put("orderField", m.get("orderField")==null?pk:m.get("orderField"));
        map.put("deasc", m.get("deasc")==null?"asc":(("asc".equals(m.get("deasc").toString().toLowerCase()) || "desc".equals(m.get("deasc").toString().toLowerCase()))?m.get("deasc").toString():"asc"));
        if((m.get("start")!=null || !"".equals(m.get("start"))) && (m.get("limit")!=null || !"".equals(m.get("limit")))) {
            map.put("start", m.get("start")==null?null:Funs.string2int(m.get("start")+""));
            map.put("limit", m.get("limit")==null?null:Funs.string2int(m.get("limit")+""));
        }
        List resultlist = this.coreDao.queryList("query_public_where_list",map);
        return resultlist;
    }
    /**
     * 查询数量
     * @param object
     * @param m
     * @return
     */
    public int queryCount(Object object,Map<String, Object> m) {
        Map<String, Object> map=new HashMap<String, Object>();
        String tableName = Funs.getTableName(object, true);
        map.put("tableName", tableName);

        HashMap<String, String> condMap = new HashMap<String, String>();
        condMap.put("condi", "like,eq,neq,gt,lt,gte,lte");
        if(m.get("where")!=null && !"".equals(m.get("where"))) {
            //[{"field":"id","condition":"eq","value":"11"},{"field":"name","value":"0","logic":"and"}]
            String whereobj = m.get("where").toString();
            List<Object> list = new ArrayList<Object>();
            JSONArray js = JSONArray.parseArray(whereobj);
            for(int i=0;i<js.size();i++) {
                JSONObject jo = JSONObject.parseObject(js.getString(i));
                Map<String, String> mm=new HashMap<String, String>();
                mm.put("field", "T."+jo.getString("field"));
                if(jo.containsKey("condition")) {
                    String ct = jo.getString("condition");
                    if(!Arrays.<String>asList(condMap.get("condi").split(",")).contains(jo.getString("condition"))) {
                        ct="eq";
                    }
                    mm.put("condition", ct);
                }else {
                    mm.put("condition", "eq");
                }
                mm.put("value", jo.getString("value"));

                if(i>0 && jo.containsKey("logic")) {
                    if(jo.getString("logic")=="and" || "and".equals(jo.getString("logic"))) {
                        mm.put("logic", jo.getString("logic"));
                    }else if(jo.getString("logic")=="or" || "or".equals(jo.getString("logic"))) {
                        mm.put("logic", jo.getString("logic"));
                    }else {
                        mm.put("logic", "and");
                    }
                }
                list.add(mm);
            }
            map.put("whereList", list.toArray());
        }
        int resultCount = this.coreDao.queryCount("query_public_where_count",map);
        return resultCount;
    }

    private List<Object> convert2Query(String json){
        JSONArray j = JSONArray.parseArray(json);
        //System.out.println(j.toJSONString());
        List<Object> list = new ArrayList<Object>();
        for(int i=0;i<j.size();i++){
            JSONObject obj = j.getJSONObject(i);
            Map<String, Object> map=new HashMap<String, Object>();
            if(i!=0) {
                map.put("logic", obj.getString("logic")==null?"":obj.getString("logic").trim());
            }
            if(obj.containsKey("children")){
                JSONArray o = JSONArray.parseArray(obj.getString("children"));
                List<Map<String, Object>> childList=new ArrayList<Map<String, Object>>();
                for(int k=0;k<o.size();k++){
                    JSONObject _o = o.getJSONObject(k);
                    Map<String, Object> mm=new HashMap<String, Object>();
                    mm.put("f", _o.getString("field"));
                    mm.put("v", _o.getString("value"));
                    mm.put("c", _o.getString("condition"));
                    if(k!=0) {
                        mm.put("l", _o.getString("logic")==null?"":_o.getString("logic"));
                    }
                    childList.add(mm);
                }
                map.put("children", childList);
            }else{
                map.put("field", obj.getString("field"));
                map.put("value", obj.getString("value"));
                map.put("condition", (obj.getString("condition")==null || "".equals(obj.getString("condition")))?"eq":obj.getString("condition"));
            }
            list.add(map);
        }
        return list;
    }
}
