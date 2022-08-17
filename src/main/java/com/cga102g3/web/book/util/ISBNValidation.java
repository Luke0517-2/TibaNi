package com.cga102g3.web.book.util;

/**
 * @Description
 * @Author Robert
 * @Version
 * @Date 2022-06-16 下午 10:03
 */
public class ISBNValidation {
    public static boolean validate(String ISBN) {
        if (ISBN == null || ISBN.length() != 13) {
            return false;
        }

        return isAValidISBN(Long.parseLong(ISBN));
    }

    private static int getSum(long isbn) {
        int count = 0;
        int sum = 0;
        long newIsbn = 0;
        do {
            sum += count % 2 == 0 ? isbn % 10 : 3 * (isbn % 10);
            count++;
            newIsbn = isbn / 10;
        } while (newIsbn > 0);
        return sum;
    }

    private static boolean isAValidISBN(long isbn) {
        return getSum(isbn) % 10 == 0;
    }

    public static void main(String[] args) {
        System.out.println(ISBNValidation.validate("9780137334681"));
    }
}
