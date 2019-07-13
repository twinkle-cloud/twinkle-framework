package com.twinkle.framework.core.error;

import java.io.Serializable;

import lombok.Data;

/**
 * 通用异常
 * */
@Data
public class GeneralException<T> implements Serializable{

    
    /**
     * 
     */
    private static final long serialVersionUID = 5519288075111329854L;
    
    /**
     * 异常码
     * */
    private String code;
    /**
     * 异常描述
     * */
    private String desc;
    /**
     * 数据
     * */
    private T data;
    
    public GeneralException(String code) {
        this(code, null, null);
    }
    
    public GeneralException(String code, String desc) {
        this(code, desc,null);
    }
    
    public GeneralException(String code, String desc, T data) {
        this.code = code;
        this.desc = desc;
        this.data = data;
    }
    
}
