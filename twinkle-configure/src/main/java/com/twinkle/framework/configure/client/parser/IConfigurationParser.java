package com.twinkle.framework.configure.client.parser;

import com.twinkle.framework.configure.client.data.ConfigurationEnvironment;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-15 22:33<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface IConfigurationParser {
    ConfigurationEnvironment doParser(String _text);
}
