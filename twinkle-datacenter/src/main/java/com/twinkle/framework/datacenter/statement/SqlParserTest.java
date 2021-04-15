package com.twinkle.framework.datacenter.statement;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLLimit;
import com.alibaba.druid.sql.ast.SQLOrderBy;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOperator;
import com.alibaba.druid.sql.ast.expr.SQLInSubQueryExpr;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.util.JdbcConstants;
import com.twinkle.framework.core.lang.UUIDAttribute;

import java.util.List;
import java.util.UUID;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/20/19 3:08 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class SqlParserTest {
    public void enhanceSql(String sql) {
        // 解析
        List<SQLStatement> statements = SQLUtils.parseStatements(sql, JdbcConstants.MYSQL);
        // 只考虑一条语句
        SQLStatement statement = statements.get(0);
        // 只考虑查询语句
        SQLSelectStatement sqlSelectStatement = (SQLSelectStatement) statement;
        SQLSelectQuery     sqlSelectQuery     = sqlSelectStatement.getSelect().getQuery();
        // 非union的查询语句
        if (sqlSelectQuery instanceof SQLSelectQueryBlock) {
            SQLSelectQueryBlock sqlSelectQueryBlock = (SQLSelectQueryBlock) sqlSelectQuery;
            // 获取字段列表
            List<SQLSelectItem> selectItems         = sqlSelectQueryBlock.getSelectList();
            selectItems.forEach(x -> {
                // 处理---------------------
            });
            // 获取表
            SQLTableSource table = sqlSelectQueryBlock.getFrom();
            // 普通单表
            if (table instanceof SQLExprTableSource) {
                // 处理---------------------
                // join多表
            } else if (table instanceof SQLJoinTableSource) {
                // 处理---------------------
                // 子查询作为表
            } else if (table instanceof SQLSubqueryTableSource) {
                // 处理---------------------
            }
            // 获取where条件
            SQLExpr where = sqlSelectQueryBlock.getWhere();
            // 如果是二元表达式
            if (where instanceof SQLBinaryOpExpr) {
                SQLBinaryOpExpr   sqlBinaryOpExpr = (SQLBinaryOpExpr) where;
                SQLExpr           left            = sqlBinaryOpExpr.getLeft();
                SQLBinaryOperator operator        = sqlBinaryOpExpr.getOperator();
                SQLExpr           right           = sqlBinaryOpExpr.getRight();
                // 处理---------------------
                // 如果是子查询
            } else if (where instanceof SQLInSubQueryExpr) {
                SQLInSubQueryExpr sqlInSubQueryExpr = (SQLInSubQueryExpr) where;
                // 处理---------------------
            }
            // 获取分组
            SQLSelectGroupByClause groupBy = sqlSelectQueryBlock.getGroupBy();
            // 处理---------------------
            // 获取排序
            SQLOrderBy orderBy = sqlSelectQueryBlock.getOrderBy();
            // 处理---------------------
            // 获取分页
            SQLLimit limit = sqlSelectQueryBlock.getLimit();
            // 处理---------------------
            // union的查询语句
            System.out.println("limit:" + limit);
        } else if (sqlSelectQuery instanceof SQLUnionQuery) {
            // 处理---------------------
        }
    }
    public static void main(String[] _args) {

        long stamp1 = System.currentTimeMillis();
        for(int i = 0; i< 10000000; i++) {
            UUID.randomUUID().toString();
        }
        long stamp2 = System.currentTimeMillis();
        System.out.println("The cost time:" + (stamp2 - stamp1));

        stamp2 = System.currentTimeMillis();
        for(int i = 0; i< 10000000; i++) {
            UUIDAttribute.toString(UUIDAttribute.getNewUUID());
        }
        long stamp3 = System.currentTimeMillis();
        System.out.println("The cost time:" + (stamp3 - stamp2));

        System.out.println("The UUID is:" + UUIDAttribute.toString(UUIDAttribute.getNewUUID()));
        System.out.println("The UUID is:" + UUID.randomUUID().toString());
//        String tempStr = new String("  SELECT SUM(P.R_MARKED_SCORE), SUM(P.R_AUDIT_SCORE) FROM PER_ASSESS_ANSWER P, PER_ASSESS_ASP_MAP PM\n" +
//                "    WHERE P.ASSESS_PAPER_ID = PM.ASSESS_PAPER_ID AND P.ASSESS_ID = PM.ASSESS_ID " +
//                "AND PM.ASSESS_CATEGORY_ID = '%assessCategoryId' " +
//                "AND P.UnitPrice BETWEEN 10 AND 20 " +
//                "AND P.CREATOR_ID= '%creatorId' LIMIT ?;\n");
//        SqlParserTest tempParser = new SqlParserTest();
//        tempParser.enhanceSql(tempStr);
    }
}
