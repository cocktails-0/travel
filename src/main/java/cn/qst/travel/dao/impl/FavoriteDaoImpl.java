package cn.qst.travel.dao.impl;

import cn.qst.travel.dao.FavoriteDao;
import cn.qst.travel.domain.Favorite;
import cn.qst.travel.domain.MyFavorite;
import cn.qst.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;
import java.util.List;

public class FavoriteDaoImpl implements FavoriteDao {

    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public Favorite findByRidAndUid(int rid, int uid) {
        Favorite favorite = null;
        try {
            String sql = " select * from tab_favorite where rid = ? and uid = ?";
            favorite = template.queryForObject(sql, new BeanPropertyRowMapper<Favorite>(Favorite.class), rid, uid);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return favorite;
    }

    @Override
    public int findCountByRid(int rid) {
        String sql = "SELECT COUNT(*) FROM tab_favorite WHERE rid = ?";

        return template.queryForObject(sql,Integer.class,rid);
    }

    @Override
    public void add(int rid, int uid) {
        String sql = "insert into tab_favorite values(?,?,?)";

        template.update(sql,rid,new Date(),uid);
    }

    //4===========================>
    /**
     * 查询用户uid的收藏总数
     * @param uid 用户uid
     * @return 收藏路线总数
     */
    @Override
    public int findCountByUid(int uid) {
        String sql = "SELECT COUNT(*) FROM tab_favorite WHERE uid = ?";
        return template.queryForObject(sql, Integer.class, uid);
    }

    //5===================>
    /**
     * 分页查询用户uid的收藏记录
     * @param uid 用户uid
     * @param start 起始条目
     * @param pageSize 每页显示条数
     * @return MyFavorite Bean 对象构成的 List 集合
     */
    @Override
    public List<MyFavorite> findByUidAndPage(int uid, int start, int pageSize) {
        String sql = "SELECT * FROM tab_favorite WHERE uid = ? LIMIT ? , ?";
        return template.query(sql, new BeanPropertyRowMapper<>(MyFavorite.class), uid, start, pageSize);
    }

}
