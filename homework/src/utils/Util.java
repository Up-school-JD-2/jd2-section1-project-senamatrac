package utils;

public class Util {
    public static boolean contains(Object item, Object[] items) {
        for (Object baseData : items) {
            if (baseData != null && baseData.equals(item)) {
                return true;
            }
        }
        return false;
    }


    public static int getIndex(Object item, Object[] items) {
        for (int i = 0; i < items.length; i++) {
            if (items[i].equals(item)) {
                return i;
            }
        }
        return -1;
    }
    public static int getAvailableIndex(Object[] items) {
        for (int i = 0; i < items.length; i++) {
            if (items[i] == null) {
                return i;
            }
        }
        return -1;
    }
    public static boolean allSlotsTaken(Object[] items) {
        boolean full = true;
        for (Object baseData : items) {
            if (baseData == null) {
                return false; // empty slot
            }
        }
        return full;
    }


    public static boolean isDate(String date, String split){
        String[] dateSplit = date.split(split);
        boolean flag = true;
        if (dateSplit.length < 3) {
            System.out.println("Eksik giriş yaptınız");
            return false;
        }
        else{
            String stringYear = dateSplit[0];
            String stringMonth = dateSplit[1];
            String stringDay = dateSplit[2];

            if (!isDigitInt(stringYear) || stringYear.length() != 4){
                System.out.println("Hatalı yıl girdiniz");
                flag = false;
            }
            if (isDigitInt(stringMonth)){
                int month = Integer.parseInt(stringMonth);
                if (month > 12 || month < 1) {
                    System.out.println("Hatalı ay girdiniz");
                    flag = false;
                }
            }
            if (isDigitInt(stringDay)){
                int day = Integer.parseInt(stringDay);
                if (day > 31 || day < 1) {
                    System.out.println("Hatalı gün girdiniz");
                    flag = false;
                }
            }
        }
        return flag;
    }

    public static boolean isDigitInt(String input){
        boolean flag = true;
        for (int i = 0; i < input.length() ;i++){
            if (!Character.isDigit(input.charAt(i)))
                flag = false;
        }
        return flag;
    }
}
