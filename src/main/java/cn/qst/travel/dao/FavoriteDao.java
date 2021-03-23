package cn.qst.travel.dao;

import cn.qst.travel.domain.Favorite;
import cn.qst.travel.domain.MyFavorite;

import java.util.List;

public interface FavoriteDao {

    /**
     * 根据rid和uid查询收藏信息
     * @param rid
     * @param uid
     * @return
     */
    public Favorite findByRidAndUid(int rid, int uid);

    /**
     * 根据rid 查询收藏次数
     * @param rid
     * @return
     */
    public int findCountByRid(int rid);

    /**
     * 添加收藏
     * @param i
     * @param uid
     */
    void add(int i, int uid);

    // 1===================>
    /**
     * 查询用户uid的收藏总数
     * @param uid 用户uid
     * @return 收藏路线总数
     */
    int findCountByUid(int uid);

    //2====================>
    /**
     * 分页查询用户uid的收藏记录
     * @param uid 用户uid
     * @param start 起始条目
     * @param pageSize 每页显示条数
     * @return MyFavorite Bean 对象构成的 List 集合
     */
    List<MyFavorite> findByUidAndPage(int uid, int start, int pageSize);
}
