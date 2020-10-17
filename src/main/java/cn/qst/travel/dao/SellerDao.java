package cn.qst.travel.dao;

import cn.qst.travel.domain.Seller;

public interface SellerDao {
    /**
     * 根据id查询
     * @param id
     * @return
     */
    public Seller findById(int id);
}
