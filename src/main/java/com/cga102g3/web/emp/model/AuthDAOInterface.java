package com.cga102g3.web.emp.model;

import java.util.List;

public interface AuthDAOInterface {
	public void insert(AuthVO authVO);
    public void update(AuthVO authVO);
    public void delete(Integer authID);
    public AuthVO findByPrimaryKey(Integer authID);
    public List<AuthVO> getAll();
}
