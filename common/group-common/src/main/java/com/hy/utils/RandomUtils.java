package com.hy.utils;

import java.util.Random;

/**
 * @Description: $-  RandomUtils-$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/11/17 15:01
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/11/17 15:01
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public class RandomUtils {

    //生成一个6位随机数
    public static Integer generateSixNum() {

        int radom = new Random().nextInt(999999);
        if (radom < 100000) {
            radom += 100000;
        }
        return radom;
    }

    private static char[] constant =
            {
                    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                    'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                    'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
            };

    public static String getCharAndNumr(int length) {
        String val = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            // 输出字母还是数字
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            // 字符串
            if ("char".equalsIgnoreCase(charOrNum)) {
                // 取得大写字母还是小写字母
                int choice = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (choice + random.nextInt(26));
            } else if ("num".equalsIgnoreCase(charOrNum)) { // 数字
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }


    public static int createRandomNoOfOneToNine() {

        return (int) (Math.random() * 10);
    }

    public static void main(String[] args) {

        for (int i = 0; i <100 ; i++) {
            int random = (int) (Math.random() * 10);
            if(random % 2 == 0){

                System.out.println("是偶数:"+Integer.valueOf(random).toString());

            }else{

                System.out.println("是奇数:"+Integer.valueOf(random).toString());
            }
        }
    }


}
