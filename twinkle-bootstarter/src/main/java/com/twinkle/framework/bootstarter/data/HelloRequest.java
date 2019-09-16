package com.twinkle.framework.bootstarter.data;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-06-17 17:49<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Data
public class HelloRequest {
    private String userName;
    @ApiModelProperty(hidden = true)
    private String password;
    private List<Title> titles;
}
