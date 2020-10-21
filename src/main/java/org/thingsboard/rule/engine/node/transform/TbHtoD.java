package org.thingsboard.rule.engine.node.transform;

import java.util.List;

public class TbHtoD {
    /**
     *
     * @param start  开始的字节数
     * @param length 字节长度
     * @param value  要转换的十六进制字符串
     * @param format  转换成几进制的就填几
     * @return  将转换成功的数字以字符串形式传出去
     */
    public String trans(int start, int length, String value, int format){
        String armLengthValue = "";
        for (int i = start/8 + length /4;i > start; i-=2){
            armLengthValue = armLengthValue + value.substring(i-2,i);
        }
        String i = Long.parseLong(armLengthValue, format)+"";
        return i;
    }
}
