package com.cga102g3.web.emp.model;

import java.util.List;



public interface AdminDAOInterface {
	public void insert(AdminVO adminVO);
    public void update(AdminVO adminVO);
    public void delete(Integer adminID);
    public AdminVO findByPrimaryKey(Integer adminID);
    public AdminVO findAdminAccountAndPassword(String adminAccount, String adminPassword);
    
    public List<AdminVO> getAll();
}
