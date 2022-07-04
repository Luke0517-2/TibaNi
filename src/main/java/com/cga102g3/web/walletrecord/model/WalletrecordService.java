package com.cga102g3.web.walletrecord.model;

import java.util.List;

public class WalletrecordService {

	private WalletrecordDAO_interface dao;

	public WalletrecordService() {
		dao = new WalletrecordJDBCDAO();
	}

	public WalletrecordVO addWalletrecord(WalletrecordVO walletrecordVO) {

//		WalletrecordVO walletrecordVO = new WalletrecordVO();
//		
//		walletrecordVO.setMbr_ID(mbr_ID);
//		walletrecordVO.setNote(note);
//		walletrecordVO.setAmount(amount);
		
		dao.insert(walletrecordVO);

		return walletrecordVO;
	}

	public WalletrecordVO updateWalletrecord(Integer note, Integer amount, Integer wallet_rec_no) {

		WalletrecordVO walletrecordVO = new WalletrecordVO();

		walletrecordVO.setNote(note);
		walletrecordVO.setAmount(amount);
		walletrecordVO.setWallet_rec_no(wallet_rec_no);
		
		dao.update(walletrecordVO);

		return walletrecordVO;
	}

	public void deleteWalletrecord(Integer wallet_rec_no) {
		dao.delete(wallet_rec_no);
	}

	public WalletrecordVO getOneWalletrecord(Integer wallet_rec_no) {
		return dao.findByPrimaryKey(wallet_rec_no);
	}
	
	public List<WalletrecordVO> getOneWalletrecord2(Integer mbr_ID) {
		return dao.findByMemberId(mbr_ID);
	}

	public List<WalletrecordVO> getAll() {
		return dao.getAll();
	}

	public WalletrecordVO updateWalletrecord(Integer note, Integer mbr_ID) {

		WalletrecordVO walletrecordVO = new WalletrecordVO();

		walletrecordVO.setNote(note);
		walletrecordVO.setMbr_ID(mbr_ID);
		
		dao.updateMEM(walletrecordVO);

		return walletrecordVO;
	}

}
