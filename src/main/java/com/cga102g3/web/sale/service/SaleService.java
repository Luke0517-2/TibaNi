package com.cga102g3.web.sale.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cga102g3.web.category.entity.Category;
import com.cga102g3.web.prod.service.ProdService;
import com.cga102g3.web.prod_sale.entity.ProdSaleVO;
import com.cga102g3.web.sale.dao.SaleDAOInterface;
import com.cga102g3.web.sale.dao.impl.SaleJDBCDAO;
import com.cga102g3.web.sale.entity.SaleVO;

public class SaleService {

	private SaleDAOInterface dao;

	public SaleService() {
		dao = new SaleJDBCDAO();
	}

	public SaleVO insert(SaleVO saleVO) {
		dao.insert(saleVO);
		return saleVO;
	}

	public SaleVO update(Date saleStart, Date saleEnd) {

		SaleVO saleVO2 = new SaleVO();

		saleVO2.setSaleStart(saleStart);
		saleVO2.setSaleEnd(saleEnd);
		dao.update(saleVO2);

		return saleVO2;
	}

//	public void delete(Integer saleID) {
//		dao.delete(saleID);
//	}

	public SaleVO findByPrimaryKey(Integer saleID) {
		return dao.findByPrimaryKey(saleID);
	}

	public List<SaleVO> getAll() {
		return dao.getAll();
	}

	/**
	 * @description: 新增促銷專案同時新增促銷產品
	 * @param: [saleStart, saleEnd, category, discount]
	 * @return: void
	 * @auther: Luke
	 * @date: 2022/06/29 15:21:36
	 */
	public boolean insertWithProdSale(Date saleStart,Date saleEnd,String category,double discount){

		//新增saleVO
		SaleVO saleVO = new SaleVO();
		saleVO.setSaleStart(saleStart);
		saleVO.setSaleEnd(saleEnd);

		//新增List存放prodSaleVO
		ProdService service = new ProdService();
		List<ProdSaleVO> list = new ArrayList<>();
		List<Map<String, Integer>> category1 = service.getCategory(category);
		for(int i = 0; i < category1.size(); i++){
			ProdSaleVO prodSaleVO = new ProdSaleVO();
			Map<String, Integer> stringIntegerMap = category1.get(i);
			Integer prodID = stringIntegerMap.get("prodID");
			Integer price = stringIntegerMap.get("price");
			int salePrice = (int) (price * discount);
			prodSaleVO.setProdID(prodID);
			prodSaleVO.setSalePrice(salePrice);
			list.add(prodSaleVO);
		}
		return dao.insertWithProdSale(saleVO,list);
	}
	/**
	 * @description: 判斷起始時間是否衝突
	 * @param: [start]
	 * @return: boolean
	 * @auther: Luke
	 * @date: 2022/06/29 15:22:11
	 */
	public boolean judge1(Date start){
		return dao.judge1(start);
	}
	/**
	 * @description: 判斷區間是否衝突
	 * @param: [start, end]
	 * @return: boolean
	 * @auther: Luke
	 * @date: 2022/06/29 15:22:36
	 */
	public boolean judge2(Date start,Date end){
		return dao.judge2(start,end);
	}

	/**
	 * @description: 刪除 By saleID
	 * @param:
	 * @return:
	 * @auther: Luke
	 * @date: 2022/07/12 11:05:16
	 */
	public boolean delete(Integer saleID){return dao.delete(saleID);}

	/**
	 * @description: 抓取現有產品的種類
	 * @param:
	 * @return:
	 * @auther: Luke
	 * @date: 2022/07/13 13:04:30
	 */
	public List<Category> getProdCategory(){return dao.getProdCategory();}
}
