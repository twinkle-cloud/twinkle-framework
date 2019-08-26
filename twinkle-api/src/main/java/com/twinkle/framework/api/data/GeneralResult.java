package com.twinkle.framework.api.data;

import java.io.Serializable;

import lombok.Data;

/**
 * 通用返回
 * */
@Data
public class GeneralResult<T> implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = -634083654601510733L;
    
    /**
     * 返回码
     * */
    private String code;
    /**
     * 返回描述
     * */
    private String desc;
    /**
     * 数据
     * */
    private T data;
    
    public GeneralResult() {
    }
    
    public GeneralResult(String code) {
        this(code, null, null);
    }
    
    public GeneralResult(String code, String desc) {
        this(code, desc,null);
    }
    
    public GeneralResult(String code, String desc, T data) {
        this.code = code;
        this.desc = desc;
        this.data = data;
    }

    public void setCode(String _code) {
        this.code = _code;
    }
    public void setCode(int _code) {
        this.code = String.valueOf(_code);
    }
}

