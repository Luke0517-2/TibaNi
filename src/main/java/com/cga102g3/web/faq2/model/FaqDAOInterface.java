package com.cga102g3.web.faq2.model;

import java.util.*;

public interface FaqDAOInterface {
          public void insert(FaqVO faqVO);
          public void update(FaqVO faqVO);
          public void delete(Integer FAQ_ID);
          public FaqVO findByPrimaryKey(Integer FAQ_ID);
          public List<FaqVO> getAll();
          //�U�νƦX�d��(�ǤJ�Ѽƫ��AMap)(�^�� List)
//        public List<FaqVO> getAll(Map<String, String[]> map); 
}
