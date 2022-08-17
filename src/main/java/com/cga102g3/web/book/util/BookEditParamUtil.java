package com.cga102g3.web.book.util;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.cga102g3.web.book.entity.Book;
import com.cga102g3.web.book.service.BookService;
import com.cga102g3.web.book.service.impl.BookServiceImpl;

/**
 * @Description
 * @Author Robert
 * @Version
 * @Date 2022-06-18 下午 07:48
 */
public class BookEditParamUtil {
    private final static BookService service = new BookServiceImpl();

    /**
     * 添加書目時，傳送過來的書籍資料進行參數驗證
     * @param book
     * @return
     */
    public static Set<String> chkInsertBookParams(Book book) {
        Set<String> errMsgs = new HashSet<>();
        boolean ISBNStat = true;
        // 驗證isbn有效性，若可以越過前端防護，表示為異常添加數據
        if (!ISBNValidation.validate(book.getISBN())) {
            errMsgs.add("isbn參數錯誤");
            ISBNStat = false;
        }

        if (book.getEdition() == 0) {
            errMsgs.add("未填入版次");
        }

        // ISBN有效時再查詢
        if (ISBNStat && service.findEditionsByISBN(book.getISBN())
        					   .contains(book.getEdition())) {       
                errMsgs.add("版次重複");        
        }

        if (book.getAuthor() == null || "".equals(book.getAuthor())){
            errMsgs.add("未輸入作者");
        }

        return errMsgs;
    }

    public static Set<String> chkUpdateBookParams(Book book) {
        Set<String> errMsgs = new HashSet<>();
        boolean ISBNStat = true;
        if(book.getBookID() == -1) {
            errMsgs.add("錯誤的bookID");
        }

        if (!ISBNValidation.validate(book.getISBN())) {
            errMsgs.add("isbn參數錯誤");
            ISBNStat = false;
        }

        if (book.getEdition() == 0) {
            errMsgs.add("未填入版次");
        }

        // ISBN有效時再查詢
        if(ISBNStat) {
            // 當查找到的書不為空 且 對應的書目標號不是自身，表示更新後造相同版次的isbn書目，此要避免
            Book surveyBook = service.getBookByISBNAndEdition(book.getISBN(), book.getEdition());
            if (surveyBook != null && !book.equals(surveyBook)) {
                errMsgs.add("版次重複");
            }
        }

        if (book.getAuthor() == null || "".equals(book.getAuthor())){
            errMsgs.add("未輸入作者");
        }

        return errMsgs;
    }

    public static Map<String, String[]> handleParams(Map<String, String[]> parameterMap) {
        // 將request.getParameterMap()重新裝進parameterMap，request.getParameterMap()是不可變map
//        Set<Map.Entry<String, String[]>> entries = parameterMap.entrySet();
//        HashMap<String, String[]> map = (HashMap<String, String[]>) entries.stream()
//                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        HashMap<String, String[]> map = new HashMap<>(parameterMap);
        // bookID, edition, pubdate, pages 都要檢查轉換格式，避免500

        String[] bookIDStrArr = map.get("bookID");
        String[] bookID = new String[1];
        if (bookIDStrArr != null && bookIDStrArr.length == 1) {
            try {
                Integer.parseInt(bookIDStrArr[0]);
            } catch (NumberFormatException e) {
                bookID[0] = "-1";
                map.put("bookID", bookID);
            }
        } else {
            // 避免bookID為空，檢測時報空指針異常
            bookID[0] = "-1";
            map.put("bookID", bookID);
        }

        String[] pubdateStrArr = map.get("pubdate");
        String[] pubdate = new String[1];
        if (pubdateStrArr != null && pubdateStrArr.length == 1) {
            try {
                java.sql.Date.valueOf(pubdateStrArr[0]);
            } catch (IllegalArgumentException e) {
                pubdate[0] = "0000-01-01";
                map.put("pubdate", pubdate);
            }
        } else {
            pubdate[0] = "0000-01-01";
            map.put("pubdate", pubdate);
        }

        String[] editionStrArr = map.get("edition");
        String[] edition = new String[1];
        if (editionStrArr != null && editionStrArr.length == 1) {
            try {
                Integer.parseInt(editionStrArr[0]);
            } catch (NumberFormatException e) {
                edition[0] = "0";
                map.put("edition", edition);
            }
        }

        String[] pagesStrArr = map.get("pages");
        String[] pages = new String[1];
        if (pagesStrArr != null && pagesStrArr.length == 1) {
            try {
                Integer.parseInt(pagesStrArr[0]);
            } catch (NumberFormatException e) {
                pages[0] = "0";
                map.put("pages", pages);
            }
        }

        return map;
    }


}
