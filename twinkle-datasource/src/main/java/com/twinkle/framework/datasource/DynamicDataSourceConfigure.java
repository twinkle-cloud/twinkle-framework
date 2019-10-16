package com.twinkle.framework.datasource;

import com.twinkle.framework.datasource.matcher.ExpressionMatcher;
import com.twinkle.framework.datasource.matcher.Matcher;
import com.twinkle.framework.datasource.matcher.RegexMatcher;
import lombok.Getter;

import java.util.LinkedList;
import java.util.List;

/**
 * Auto switch the datasource based on the strategies.
 *
 * @author chenxj
 * @since
 */
public class DynamicDataSourceConfigure {
    @Getter
    private List<Matcher> matchers = new LinkedList<>();

    private DynamicDataSourceConfigure() {
    }

    public static DynamicDataSourceConfigure config() {
        return new DynamicDataSourceConfigure();
    }

    public DynamicDataSourceConfigure addRegexMatcher(String _pattern, String _dataSource) {
        this.matchers.add(new RegexMatcher(_pattern, _dataSource));
        return this;
    }

    public DynamicDataSourceConfigure addExpressionMatcher(String _expression, String _dataSource) {
        this.matchers.add(new ExpressionMatcher(_expression, _dataSource));
        return this;
    }
}
