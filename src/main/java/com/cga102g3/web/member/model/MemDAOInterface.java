package com.cga102g3.web.member.model;

import java.util.*;

public interface MemDAOInterface {
	public void insert(MemVO memVO);
    public void update(MemVO memVO);
    public void delete(Integer mbrID);
    public MemVO findByPrimaryKey(Integer mbrID);
    public List<MemVO> getAll();
}
