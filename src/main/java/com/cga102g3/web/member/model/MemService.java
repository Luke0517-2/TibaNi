package com.cga102g3.web.member.model;

import java.sql.Date;
import java.util.List;

public class MemService {
	
	private MemDAOInterface dao;
	
	public MemService() {
		dao = new MemJDBCDAO();
	}
	
	public MemVO insert(MemVO memVO) {
		dao.insert(memVO);
		return memVO;
	}
	
	
	public MemVO update(Integer mbr_ID, String mbr_account, String mbr_password,Integer mbr_status,
			Integer reviewStatus, String mbr_name, Integer mbr_gender, String mbr_mobile, String mbr_addr,
			String mbr_email, Date mbr_birth, Integer Tcoin_bal) {
		
	MemVO memVO2 = new MemVO();

	memVO2.setMbrID(mbr_ID);
	memVO2.setMbrAccount(mbr_account);
	memVO2.setMbrPassword(mbr_password);
	memVO2.setMbrStatus(mbr_status);
	memVO2.setMbrName(mbr_name);
	memVO2.setMbrGender(mbr_gender);
	memVO2.setMbrMobile(mbr_mobile);
	memVO2.setMbrAddr(mbr_addr);
	memVO2.setMbrEmail(mbr_email);
	memVO2.setMbrBirth(mbr_birth);
	memVO2.setTcoinBal(Tcoin_bal);
	dao.update(memVO2);
	
	return memVO2;
	}
	
	public void delete(Integer mbrID) {
		dao.delete(mbrID);
	}

	public MemVO findByPrimaryKey(Integer mbrID) {
		return dao.findByPrimaryKey(mbrID);
	}

	public List<MemVO> getAll() {
		return dao.getAll();
	}
}
