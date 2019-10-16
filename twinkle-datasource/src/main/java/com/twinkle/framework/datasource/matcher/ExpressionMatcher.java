package com.twinkle.framework.datasource.matcher;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/12/19 6:31 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@AllArgsConstructor
@Data
public class ExpressionMatcher implements Matcher {
  /**
   * Expression.
   */
  private String expression;
  /**
   * Data source's name.
   */
  private String dataSource;
}