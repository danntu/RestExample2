package com.danntu.doa;

import com.danntu.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
@Component
public class UserDaoImpl implements UserDao {
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) throws DataAccessException{
        this.namedParameterJdbcTemplate=namedParameterJdbcTemplate;
    }

    private SqlParameterSource getSqlParameterByModel(User user, String id){
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        if (user !=null){
            if (id.length()>0){
                paramSource.addValue("id",id);
            } else {
                paramSource.addValue("id",user.getId());
            }
            paramSource.addValue("name",user.getName());
            paramSource.addValue("statusid",user.getStatusid());
        }
        return paramSource;
    }

    private static final class UserMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet arg0, int arg1) throws SQLException {
            User user = new User();
            user.setId(arg0.getString("id"));
            user.setName(arg0.getString("name"));
            user.setStatusid(arg0.getInt("statusid"));
            switch (user.getStatusid()){
                case 0: user.setCurrentStatus("none"); break;
                case 1: user.setCurrentStatus("online"); break;
                case 2: user.setCurrentStatus("offline"); break;
            }
            return user;
        }
    }

    @Override
    public String createUser(User user) {
        String id = UUID.randomUUID().toString();
        String sql = "INSERT INTO users (id,name,statusid) "
                +"VALUES (:id,:name,:statusid)";
        namedParameterJdbcTemplate.update(sql,getSqlParameterByModel(user,id));
        return id;
    }

    @Override
    public User getUserById(String id) {
        String sql = "SELECT * FROM users where id = :id";
        Map<String,String> namedParam = new HashMap<>();
        namedParam.put("id",id);
        return namedParameterJdbcTemplate.queryForObject(sql,namedParam,new UserMapper());
    }

    @Override
    public User changeStatus(String id, int newStatus) {
        String sql1 = "UPDATE users SET statusid = "+newStatus+" where id = :id";
        String sql2 = "SELECT * FROM USERS WHERE id= :id";
        Map<String,String> namedParam1 = new HashMap<>();
        Map<String,Integer> namedParam2 = new HashMap<>();
        namedParam1.put("id",id);
        namedParam2.put("statusid",newStatus);
        User userWithPresStatus = (User) namedParameterJdbcTemplate.queryForObject(sql2,namedParam1,new UserMapper());
        namedParameterJdbcTemplate.update(sql1,namedParam1);
        User user = namedParameterJdbcTemplate.queryForObject(sql2,namedParam1, new UserMapper());
        user.setPrevStatus(userWithPresStatus.getPrevStatus());
        return user;
    }

    @Override
    public List<User> listInfoByStatus(String id, String status) {
        List<User> list = new ArrayList<User>();
        String sql = "SELECT a.* from users a, status b where a.statusid = b.id and b.statusname = :status";
        Map<String, String> namedParam = new HashMap<>();
        namedParam.put("status", status);
        list = namedParameterJdbcTemplate.query(sql, namedParam, new UserMapper());
        return list;
    }
}
