package com.cga102g3.web.walletrecord2.model;

import java.util.*;

public interface WalletrecordDAOInterface {
          public void insert(WalletrecordVO empVO);
          public void update(WalletrecordVO empVO);
          public void delete(Integer empno);
          public WalletrecordVO findByPrimaryKey(Integer Wallet_rec_no);
          public List<WalletrecordVO> findByMemberId(Integer mbr_ID);
          public List<WalletrecordVO> getAll();
          public void updateMEM(WalletrecordVO empVO);
          //�U�νƦX�d��(�ǤJ�Ѽƫ��AMap)(�^�� List)
//        public List<EmpVO> getAll(Map<String, String[]> map); 
}
