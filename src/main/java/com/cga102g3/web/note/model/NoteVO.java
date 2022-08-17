package com.cga102g3.web.note.model;
import java.sql.Timestamp;

public class NoteVO implements java.io.Serializable{
	private Integer noteID;
	private Integer mbrID;
	private Timestamp noteTime;
	private Integer noteContentType;
	private String noteContent;
	public Integer getNoteID() {
		return noteID;
	}
	public void setNoteID(Integer noteID) {
		this.noteID = noteID;
	}
	public Integer getMbrID() {
		return mbrID;
	}
	public void setMbrID(Integer mbrID) {
		this.mbrID = mbrID;
	}
	public Timestamp getNoteTime() {
		return noteTime;
	}
	public void setNoteTime(Timestamp noteTime) {
		this.noteTime = noteTime;
	}
	public Integer getNoteContentType() {
		return noteContentType;
	}
	public void setNoteContentType(Integer noteContentType) {
		this.noteContentType = noteContentType;
	}
	public String getNoteContent() {
		return noteContent;
	}
	public void setNoteContent(String noteContent) {
		this.noteContent = noteContent;
	}
	
	

	
}
