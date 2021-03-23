package cn.qst.travel.service;

import cn.qst.travel.domain.PageBean;
import cn.qst.travel.domain.Route;
import cn.qst.travel.domain.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UserService {
    /**
     * 注册用户
     *
     * @param user
     * @return
     */
    boolean regist(User user);

    boolean active(String code);

    User login(User user);

    //6=========================>
    /**
     * 查询`我的收藏`
     * @param uid 用户uid
     * @return 泛型为Route类的PageBean对象，
     *      收藏结果的分页展示
     */
    PageBean<Route> favorPageQuery(int uid, int currentPage, int pageSize);
}