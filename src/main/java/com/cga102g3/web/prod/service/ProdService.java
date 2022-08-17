package com.cga102g3.web.prod.service;

import java.util.List;
import java.util.Map;

import com.cga102g3.web.prod.dao.ProdDao;
import com.cga102g3.web.prod.dao.ProdDaoImpl;
import com.cga102g3.web.prod.entity.CarObj;
import com.cga102g3.web.prod.entity.ProdVO;

/**
 * @description: TODO
 * @author: Luke
 * @date: 2022/6/22
 **/
public class ProdService {
    private ProdDao dao;

    public ProdService() {
        dao = new ProdDaoImpl();
    }

    public ProdVO getOne(int prodID) {
        return dao.findOneProd(prodID);
    }

    public List<Map<String, Object>> getALL() {
        return dao.findAll();
    }
    
    public List<Map<String, Integer>> getCategory(String categoryID) {
    	return dao.useCategory(categoryID);
    }

    /** Find products by category **/
    public List<Map<String, Object>> getCategory(int categoryID) {
        return dao.findCategory(categoryID);
    }

    /** Find products by price range **/
    public List<Map<String, Object>> getPrice(int price) {
        return dao.findPrice(price);
    }

    //前台首頁搜尋
    public List<Map<String,Object>> searchProd(String bookTitle) {
        return dao.selectTitle(bookTitle);
    }

    public CarObj forCar(int prodID){return dao.getForCar(prodID);}

    public Integer forDetail(int prodID){return dao.checkSale(prodID);}

    /**
     * @description: TODO
     * @author: Alan
     * @date: 2022/6/22
     **/
    public ProdVO addProd(Integer bookID, Integer price, Integer status) {

        ProdVO prodVO = new ProdVO();

        prodVO.setBookID(bookID);
        prodVO.setPrice(price);
        prodVO.setStatus(status);
        dao.insert(prodVO);

        return prodVO;
    }

    public ProdVO updateProd(Integer price, Integer status, Integer prodID) {

        ProdVO prodVO = new ProdVO();

        prodVO.setPrice(price);
        prodVO.setStatus(status);
        prodVO.setProdID(prodID);
        dao.update(prodVO);

        return prodVO;
    }

    public List<Map<String, Object>> getALLSALE() {
        return dao.findAllSale();
    }
    public List<Map<String, Object>> getALLSALE(Integer i) {
        return dao.showAll();
    }

    public List<Map<String, Object>> getTOPSALE() {
        return dao.findTopSale();
    }

    //後台商品觀禮搜尋
    public List<ProdVO> searchTITLE(String title) {
        return dao.searchTitle(title);
    }
    public List<Map<String, Object>> getALL(Integer no) {
        return dao.findAll(1);
    }



}
