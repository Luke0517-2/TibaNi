package com.cga102g3.web.walletrecord2.model;
import java.sql.Timestamp;

public class WalletrecordVO implements java.io.Serializable{
	private Integer walletRecNo;
	private Integer mbrID;
	private Integer note;
	private Integer amount;
	private Timestamp recTime;
	public Integer getWalletRecNo() {
		return walletRecNo;
	}
	public void setWalletRecNo(Integer walletRecNo) {
		this.walletRecNo = walletRecNo;
	}
	public Integer getMbrID() {
		return mbrID;
	}
	public void setMbrID(Integer mbrID) {
		this.mbrID = mbrID;
	}
	public Integer getNote() {
		return note;
	}
	public void setNote(Integer note) {
		this.note = note;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public Timestamp getRecTime() {
		return recTime;
	}
	public void setRecTime(Timestamp recTime) {
		this.recTime = recTime;
	}
		
}
