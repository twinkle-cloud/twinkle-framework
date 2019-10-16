package com.twinkle.framework.datasource.aop;

import org.springframework.aop.support.JdkRegexpMethodPointcut;

import java.util.Map;

/**
 * Function: JDK Regex point cut.. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/12/19 6:31 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class DynamicJdkRegexpMethodPointcut extends JdkRegexpMethodPointcut {
    private Map<String, String> matchesCache;
    private String dataSourceName;

    public DynamicJdkRegexpMethodPointcut(String _pattern, String _dataSourceName,
                                          Map<String, String> _matchesCache) {
        this.dataSourceName = _dataSourceName;
        this.matchesCache = _matchesCache;
        setPattern(_pattern);
    }

    @Override
    protected boolean matches(String _pattern, int _patternIndex) {
        boolean tempMatchFlag = super.matches(_pattern, _patternIndex);
        if (tempMatchFlag) {
            this.matchesCache.put(_pattern, this.dataSourceName);
        }
        return tempMatchFlag;
    }
}