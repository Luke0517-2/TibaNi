package com.cga102g3.web.mem.model;

import java.util.*;



public interface MemDAOInterface {
	public void signup(MemVO memVO);
	public void insert(MemVO memVO);
    public void update(MemVO memVO); 
    public void delete(Integer mbrID);
    public MemVO findByPrimaryKey(Integer mbrID);
    public MemVO findMemAccountAndPassword(String mbrAccount, String mbrPassword);
    public MemVO findNamePassWordByEmail(String mbrEmail);
    public List<MemVO> getAll();
    public void updateStatus(Integer mbrID);
    public void updateStatusByEmp(Integer mbrID, Integer mbrStatus);
    public MemVO signupStatus(MemVO memVO);
}
