package com.fosun.hani.vo;


public class Result {
    /**
     * 代码 1 - 成功，0 - 失败  其他待定
     */
    private final int code;

    /**
     * 消息描述
     */
    private final String msg;

    private Result(int code, String msg) {
        super();
        this.code = code;
        this.msg = msg;
    }
    public static Result build(int code, String msg) {
        return new Result(code, msg);
    }

    public static Result success(String msg) {
        return Result.build(1, msg);
    }

    public static Result fail(String msg){
        return Result.build(0,msg);
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
