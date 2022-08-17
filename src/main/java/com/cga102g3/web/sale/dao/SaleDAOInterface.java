package com.cga102g3.web.sale.dao;

import java.util.*;

import com.cga102g3.web.category.entity.Category;
import com.cga102g3.web.prod_sale.entity.ProdSaleVO;
import com.cga102g3.web.sale.entity.SaleVO;

public interface SaleDAOInterface {

    public void insert(SaleVO saleVO);
    public void update(SaleVO saleVO);
    public boolean delete(Integer saleID);
    public SaleVO findByPrimaryKey(Integer saleID);
    public List<SaleVO> getAll();
    
    //�U�νƦX�d��(�ǤJ�Ѽƫ��AMap)(�^�� List)
    //public List<SaleVO> getAll(Map<String, String[]> map);

    /**
     * @description: 同時新增促銷專案及產品
     * @param:
     * @return:
     * @auther: Luke
     * @date: 2022/06/29 10:43:24
     */
    public boolean insertWithProdSale(SaleVO saleVO, List<ProdSaleVO> list);
    /**
     * @description:判斷起始時間是否存在專案
     * @param: [saleStart]
     * @return: boolean true可以新增 false區間已存在
     * @auther: Luke
     * @date: 2022/06/29 14:03:56
     */
    public boolean judge1(java.sql.Date saleStart);
    /**
     * @description:判斷區間是否存在專案
     * @param: [saleStart, saleEnd]
     * @return: boolean true可以新增 false區間已存在
     * @auther: Luke
     * @date: 2022/06/29 14:03:56
     */
    public boolean judge2(java.sql.Date saleStart,java.sql.Date saleEnd);

    /**
     * @description: 抓取現有產品中有的種類
     * @param:
     * @return:
     * @auther: Luke
     * @date: 2022/07/13 12:56:36
     */
    public List<Category> getProdCategory();
}
