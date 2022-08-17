package com.cga102g3.web.emp.model;

import java.util.List;

public class AdminService {
	
	private AdminJDBCDAO dao;
	
	public AdminService() {
		dao = new AdminJDBCDAO();
	}
	
	
	public AdminVO addEmp(String adminAccount, String adminPassword, String adminName, String adminPhone) {

		AdminVO adminVO = new AdminVO();

		adminVO.setAdminAccount(adminAccount);
		adminVO.setAdminPassword(adminPassword);
		adminVO.setAdminName(adminName);
		adminVO.setAdminPhone(adminPhone);
		dao.insert(adminVO);
		
		return adminVO;
	}
	
	public AdminVO updateEmp(Integer AdminID,String adminAccount, String adminPassword, String adminName, String adminPhone) {

		AdminVO adminVO = new AdminVO();
		
		adminVO.setAdminID(AdminID);
		adminVO.setAdminAccount(adminAccount);
		adminVO.setAdminPassword(adminPassword);
		adminVO.setAdminName(adminName);
		adminVO.setAdminPhone(adminPhone);
		dao.update(adminVO);
		
		return adminVO;
	}
	
	public void deleteEmp(Integer adminID) {
		dao.delete(adminID);
	}
	
	
	public List<AdminVO> getAll() {
		return dao.getAll();
	}
	
	public AdminVO getOneEmp(Integer adminID) {
		return dao.findByPrimaryKey(adminID);
	}
	
	public AdminVO login(String adminAccount, String adminPassword) {
		return dao.findAdminAccountAndPassword(adminAccount, adminPassword);
	}
	
}
