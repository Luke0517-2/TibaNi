package com.cga102g3.web.note.model;

import java.util.List;

public class NoteService {

	private NoteDAOInterface dao;

	public NoteService() {
		//dao = new EmpDAO();
		dao = new NoteJDBCDAO();
	}

	public NoteVO addNote(Integer mbr_ID, Integer note_content_type, String note_content) {

		NoteVO noteVO = new NoteVO();
		noteVO.setMbrID(mbr_ID);
		noteVO.setNoteContentType(note_content_type);
		noteVO.setNoteContent(note_content);
		
		dao.insert(noteVO);

		return noteVO;
	}

	public NoteVO updateNote(Integer mbr_ID, Integer note_content_type, String note_content, Integer note_id) {

		NoteVO noteVO = new NoteVO();

		noteVO.setMbrID(mbr_ID);
		noteVO.setNoteContentType(note_content_type);
		noteVO.setNoteContent(note_content);
		noteVO.setNoteID(note_id);
		dao.update(noteVO);

		return noteVO;
	}

	public void deleteNote(Integer note_ID) {
		dao.delete(note_ID);
	}

	public NoteVO getOneNote(Integer note_ID) {
		return dao.findByPrimaryKey(note_ID);
	}

	public List<NoteVO> getAll() {
		return dao.getAll();
	}


}
