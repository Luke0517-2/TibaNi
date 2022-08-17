package com.cga102g3.web.emp.model;

import java.util.List;

public interface AdminAuthDAOInterface {
	public void insert(AdminAuthVO adminAuthVO);
    public void update(AdminAuthVO adminAuthVO);
    public void delete(AdminAuthVO adminAuthVO);
    public List<AdminAuthVO> findByPrimaryKey(Integer adminID);
    public List<AdminAuthVO> getAll();
}
