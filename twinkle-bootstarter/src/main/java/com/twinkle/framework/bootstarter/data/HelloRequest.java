package com.twinkle.framework.bootstarter.data;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "HelloWorldRequest",name = "HelloWorld")
public class HelloRequest {
    private String userName;
    @Schema(description = "Password",name = "Password", hidden = true)
    private String password;
    private List<Title> titles;
}
