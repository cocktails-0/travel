package cn.qst.travel.service.impl;

import cn.qst.travel.dao.FavoriteDao;
import cn.qst.travel.dao.RouteDao;
import cn.qst.travel.dao.RouteImgDao;
import cn.qst.travel.dao.UserDao;
import cn.qst.travel.dao.impl.FavoriteDaoImpl;
import cn.qst.travel.dao.impl.RouteDaoImpl;
import cn.qst.travel.dao.impl.RouteImgDaoImpl;
import cn.qst.travel.dao.impl.UserDaoImpl;
import cn.qst.travel.domain.*;
import cn.qst.travel.service.UserService;
import cn.qst.travel.util.MailUtils;
import cn.qst.travel.util.UuidUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserServiceImpl implements UserService {

    private UserDao userDao = new UserDaoImpl();

    //7==============================>
    private RouteDao routeDao = new RouteDaoImpl();
    private RouteImgDao routeImgDao = new RouteImgDaoImpl();
    private FavoriteDao favoriteDao = new FavoriteDaoImpl();

    /**
     * 注册用户
     * @param user
     * @return
     */
    @Override
    public boolean regist(User user) {
        //1.根据用户名查询用户对象
        User u = userDao.findByUsername(user.getUsername());
        //判断u是否为null
        if(u != null){
            //用户名存在，注册失败
            return false;
        }
        //2.保存用户信息
        //2.1设置激活码，唯一字符串
        user.setCode(UuidUtil.getUuid());
        //2.2设置激活状态
        user.setStatus("N");
        userDao.save(user);

        //3.激活邮件发送，邮件正文？
        String content="<a href='http://localhost/travel/user/active?code="+user.getCode()+"'>点击激活【青软   旅游网】</a>";

        MailUtils.sendMail(user.getEmail(),content,"激活邮件");

        return true;
    } 

    /**
     * 激活用户
     * @param code
     * @return
     */
    @Override
    public boolean active(String code) {
        //1.根据激活码查询用户对象
        User user = userDao.findByCode(code);
        if(user != null){
            //2.调用dao的修改激活状态的方法
            userDao.updateStatus(user);
            return true;
        }else {
            return false;
        }
    }

    /**
     * 登录方法
     * @param user
     * @return
     */
    @Override
    public User login(User user) {
        return userDao.findByUsernameAndPassword(user.getUsername(),user.getPassword());
    }

    //8  ============================>
    /**
     * 查询`我的收藏`
     * @param uid 用户uid
     * @return 泛型为Route类的PageBean对象，
     *      收藏结果的分页展示
     */
    @Override
    public PageBean<Route> favorPageQuery(int uid, int currentPage, int pageSize) {

        // 创建PageBean<Route>对象
        PageBean<Route> routePageBean = new PageBean<>();

        // 查询总记录数totalCount
        int totalCount = favoriteDao.findCountByUid(uid);
        if (totalCount == 0) {// 没有查到收藏记录
            return routePageBean;
        }

        // 计算起始记录数start，计算总页数totalPage
        int start = (currentPage - 1) * pageSize;
        int totalPage = (totalCount % pageSize == 0) ? (totalCount / pageSize) : (totalCount / pageSize + 1);

        // 分页查询rid列表（封装在MyFavorite类中）
        List<MyFavorite> pageFavoriteList = favoriteDao.findByUidAndPage(uid, start, pageSize);

        // 创新一个空的List<Route>集合
        List<Route> routeList = new ArrayList<>();

        // 遍历pageFavoriteList组装routeList
        for (MyFavorite myFavorite : pageFavoriteList) {
            // 根据其rid属性利用routeDao查route对象
            Route route = routeDao.findOne(myFavorite.getRid());
            // 根据其rid属性利用routeImgDao查routeImgList集合
            List<RouteImg> routeImgList = routeImgDao.findByRid(myFavorite.getRid());
            // 为route对象设置routeImgList属性
            route.setRouteImgList(routeImgList);
            // 向routeList中追加route属性
            routeList.add(route);
        }

        // 并组装PageBean<Route>对象
        routePageBean.setCurrentPage(currentPage);     // 设置当前页码
        routePageBean.setPageSize(pageSize);           // 设置每页显示条数
        routePageBean.setTotalPage(totalPage);         // 设置总页数
        routePageBean.setTotalCount(totalCount);       // 查询并设置总记录数
        routePageBean.setList(routeList);

        return routePageBean;
    }

}
