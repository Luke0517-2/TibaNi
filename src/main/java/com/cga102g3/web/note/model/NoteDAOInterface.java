package com.cga102g3.web.note.model;

import java.util.*;

public interface NoteDAOInterface {
          public void insert(NoteVO noteVO);
          public void update(NoteVO noteVO);
          public void delete(Integer noteno);
          public NoteVO findByPrimaryKey(Integer note_ID);
          public List<NoteVO> getAll();
          //�U�νƦX�d��(�ǤJ�Ѽƫ��AMap)(�^�� List)
//        public List<EmpVO> getAll(Map<String, String[]> map); 
}
