package net.hlinfo.cloud.admin.mybatis.dao.impl;

import net.hlinfo.cloud.admin.mybatis.dao.CoreDao;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CoreDaoImpl implements CoreDao {
    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    public List queryList(String mybitsSqlId) {
        return this.sqlSessionTemplate.selectList(mybitsSqlId);
    }
    public List queryList(String mybitsSqlId, Object object) {
        return this.sqlSessionTemplate.selectList(mybitsSqlId, object);
    }
    public int queryCount(String mybitsSqlId){
        Integer qc = this.sqlSessionTemplate.selectOne(mybitsSqlId);
        return qc==null?0:qc;
    }
    public int queryCount(String mybitsSqlId, Object object){
        Integer qc = this.sqlSessionTemplate.selectOne(mybitsSqlId,object);
        return qc==null?0:qc;
    }
    public  Object queryOne(String mybitsSqlId){
        return this.sqlSessionTemplate.selectOne(mybitsSqlId);
    }
    public  Object queryOne(String mybitsSqlId, Object object){
        return this.sqlSessionTemplate.selectOne(mybitsSqlId,object);
    }
    public boolean save(String mybitsSqlId, Object object){
        int rs = this.sqlSessionTemplate.insert(mybitsSqlId,object);
        return rs>0?true:false;
    }
    public boolean update(String mybitsSqlId, Object object){
        int rs = this.sqlSessionTemplate.update(mybitsSqlId,object);
        return rs>0?true:false;
    }

}
