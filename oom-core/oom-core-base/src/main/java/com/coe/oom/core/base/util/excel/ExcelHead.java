package com.coe.oom.core.base.util.excel;

/**
 * Created by -AiYuan on 2018/6/29.
 */
public class ExcelHead {

    private  String filedCn;  //excel 中文名称

    private  String filedEn; //excel 英文名称

    private  String filedCnOld;//未进行驼峰转换之前的名称

    public String getFiledCn() {
        return filedCn;
    }

    public void setFiledCn(String filedCn) {
        this.filedCn = filedCn;
    }

    public String getFiledEn() {
        return filedEn;
    }

    public void setFiledEn(String filedEn) {
        this.filedEn = filedEn;
    }

    public String getFiledCnOld() {
        return filedCnOld;
    }

    public void setFiledCnOld(String filedCnOld) {
        this.filedCnOld = filedCnOld;
    }
}
