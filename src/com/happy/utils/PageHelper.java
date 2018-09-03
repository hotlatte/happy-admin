package com.happy.utils;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLOrderBy;
import com.alibaba.druid.sql.ast.expr.*;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alibaba.druid.sql.dialect.oracle.parser.OracleStatementParser;
import com.alibaba.druid.sql.dialect.sqlserver.parser.SQLServerStatementParser;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Intercepts(@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}))
public class PageHelper implements Interceptor {
    Logger log = Logger.getLogger(getClass());
    public static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
    public static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();
    public static final ReflectorFactory REFLECTOR_FACTORY = new DefaultReflectorFactory();
    public static final MetaObject NULL_META_OBJECT = SystemMetaObject.NULL_META_OBJECT;
    //MetaObject.forObject(null, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY);

    private static class NullObject {
    }

    /**
     * 反射对象，增加对低版本Mybatis的支持
     *
     * @param object
     * @return
     */
    public static MetaObject forObject(Object object) {
        return MetaObject.forObject(object, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY, REFLECTOR_FACTORY);
    }

    private static final String BOUND_SQL = "sqlSource.boundSql.sql";

    private static final ThreadLocal<Page> LOCAL_PAGE = new ThreadLocal<Page>();

    private static final List<ResultMapping> EMPTY_RESULTMAPPING = new ArrayList<ResultMapping>(0);

    //数据库方言
    private static String dialect = "";
    //RowBounds参数offset作为PageNum使用 - 默认不使用
    private static boolean offsetAsPageNum = false;
    //RowBounds是否进行count查询 - 默认不查询
    private static boolean rowBoundsWithCount = false;

    /**
     * 开始分页
     *
     * @param pageNum
     * @param pageSize
     */
    public static void startPage(int pageNum, int pageSize) {
        startPage(pageNum, pageSize, true);
    }


    public static void startPage(Page page) {
        LOCAL_PAGE.set(page);
    }

    /**
     * 开始分页
     *
     * @param pageNum
     * @param pageSize
     */
    public static void startPage(int pageNum, int pageSize, boolean count) {
        LOCAL_PAGE.set(new Page(pageNum, pageSize, count));
    }

    /**
     * 获取分页参数
     *
     * @param rowBounds
     * @return
     */
    private Page getPage(RowBounds rowBounds) {
        Page page = LOCAL_PAGE.get();
        //移除本地变量
        LOCAL_PAGE.remove();

        if (page == null) {
            if (offsetAsPageNum) {
                page = new Page(rowBounds.getOffset(), rowBounds.getLimit(), rowBoundsWithCount);
            } else {
                page = new Page(rowBounds, rowBoundsWithCount);
            }
        }
        return page;
    }

    /**
     * Mybatis拦截器方法
     *
     * @param invocation
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        final Object[] args = invocation.getArgs();
        RowBounds rowBounds = (RowBounds) args[2];
        if (LOCAL_PAGE.get() == null && rowBounds == RowBounds.DEFAULT) {
            return invocation.proceed();
        } else {
            //分页信息
            Page page = getPage(rowBounds);
            if (page.getPageJob() != null) {
                return page.getPageJob().intercept(invocation, this, page);
            }
            //忽略RowBounds-否则会进行Mybatis自带的内存分页
            args[2] = RowBounds.DEFAULT;
            MappedStatement ms = (MappedStatement) args[0];
            Object parameterObject = args[1];
            BoundSql boundSql = ms.getBoundSql(parameterObject);


            //创建一个新的MappedStatement
            MappedStatement qs = newMappedStatement(ms, new BoundSqlSqlSource(boundSql));
            //将参数中的MappedStatement替换为新的qs，防止并发异常
            args[0] = qs;
            MetaObject msObject = forObject(qs);
            String sql = (String) msObject.getValue(BOUND_SQL);
            //简单的通过total的值来判断是否进行count查询
            if (page.isCount()) {
                String countSql = getCountSql(sql, page);
                //求count - 重写sql
                msObject.setValue(BOUND_SQL, countSql);

                //查询总数
                Object result = invocation.proceed();
                //设置总数
                page.setTotal((Integer) ((List) result).get(0));
            }
            //分页sql - 重写sql
            msObject.setValue(BOUND_SQL, getPageSql(sql, page));
            //恢复类型
            msObject.setValue("resultMaps", ms.getResultMaps());
            //执行分页查询
            Object result = invocation.proceed();
            //得到处理结果
            page.addAll((List) result);
            //返回结果
            return page;
        }
    }

    /**
     * 获取总数sql - 如果要支持其他数据库，修改这里就可以
     *
     * @param sql
     * @return
     */
    public String getCountSql(String sql, Page page) {
        if (!StringUtils.isEmpty(page.getCountSql())) {
            return page.getCountSql();
        }
        if (page.isQuickCount()) {
            String countSql = null;
            StringBuilder out = new StringBuilder("SELECT\n\tCOUNT(0)\nFROM ");
            SQLStatementParser parser = null;
            SQLSelectQueryBlock query = null;

            switch (dialect) {
                case "mysql":
                    parser = new MySqlStatementParser(sql);
                    query = (SQLSelectQueryBlock) ((SQLSelectStatement) parser.parseStatement()).getSelect().getQuery();
                    break;
                case "oracle":
                    parser = new OracleStatementParser(sql);
                    query = (SQLSelectQueryBlock) ((SQLSelectStatement) parser.parseStatement()).getSelect().getQuery();
                    break;
                case "sqlserver":
                    parser = new SQLServerStatementParser(sql);
                    query = (SQLSelectQueryBlock) ((SQLSelectStatement) parser.parseStatement()).getSelect().getQuery();
                    break;
            }
            if (query != null) {
                SQLTableSource from = query.getFrom();
                /*if (from instanceof SQLJoinTableSource) {
                    SQLJoinTableSource joinFrom = (SQLJoinTableSource) from;
                    out.append(joinFrom.getLeft()).append(" ").append(joinFrom.getJoinType()).append(" ").append(joinFrom.getRight());
                    out.append(" ").append(joinFrom.getCondition());
                } else {
                    out.append(query.getFrom());
                }*/
                out.append(buildFrom(from));
                if (query.getWhere() != null)
                    out.append("\nWHERE ").append(query.getWhere());
                countSql = out.toString();
            }
            log.debug(countSql);
            System.out.println(countSql);
            return countSql;
        }
        return "select count(0) from (" + sql + ") tmp_count";
    }

    public String buildFrom(SQLTableSource from) {
        StringBuilder sb = new StringBuilder();
        if (from instanceof SQLJoinTableSource) {
            SQLJoinTableSource join = (SQLJoinTableSource) from;

            SQLTableSource left = join.getLeft();
            sb.append(buildFrom(left)).append(" ");
            if (StringUtils.isNotEmpty(left.getAlias()))
                sb.append(left.getAlias()).append(" ");

            sb.append(join.getJoinType().name).append(" ");

            SQLTableSource right = join.getRight();
            sb.append(buildFrom(right)).append(" ");
            if (StringUtils.isNotEmpty(right.getAlias()))
                sb.append(right.getAlias()).append(" ");

            if (join.getCondition() != null)
                sb.append(" on ").append(join.getCondition());

        } else {
            sb.append(from);
        }
        return sb.toString();
    }

    /**
     * 获取分页sql - 如果要支持其他数据库，修改这里就可以
     *
     * @param sql
     * @param page
     * @return
     */
    public String getPageSql(String sql, Page page) {
        if (!StringUtils.isEmpty(page.getPageSql())) {
            return page.getPageSql();
        }
        SQLStatementParser parser = null;
        SQLSelectQueryBlock query = null;
        SQLSelect select = null;

        switch (dialect) {
            case "mysql":
                //parser = new MySqlStatementParser(sql);
                break;
            case "oracle":
                parser = new OracleStatementParser(sql);
                break;
            case "sqlserver":
                parser = new SQLServerStatementParser(sql);
                break;
        }
        if (parser != null) {
            try {
                select = ((SQLSelectStatement) parser.parseStatement()).getSelect();
                query = (SQLSelectQueryBlock) select.getQuery();
            } catch (Exception e) {
                System.out.println(sql);
                e.printStackTrace();
            }
        }
        if (query != null) {
            if ("mysql".equals(dialect)) {
                StringBuilder out = new StringBuilder(sql);
                if (StringUtils.isNotEmpty(page.getOrderBy())) {
                    out.append(" ").append(page.getOrderBy());
                }
                //buildBaseSql(page, query, select);
                out.append(" LIMIT " + page.getStartRow() + "," + page.getPageSize());
                return out.toString();
            } else if ("oracle".equals(dialect)) {
                StringBuilder out = new StringBuilder();
                out.append("SELECT ");
                buildCleanSelectItems(query.getSelectList(), null, out);
                out.append("FROM \n\t(SELECT ");
                buildCleanSelectItems(query.getSelectList(), "temp", out);
                out.deleteCharAt(out.length() - 1);
                out.append(",\n       rownum row_id\n\tFROM ( ");
                out.append(buildBaseSql(page, query, select));
                out.append(" ) temp \n\tWHERE rownum <= ").append(page.getEndRow());
                out.append(") \n\tWHERE row_id > ").append(page.getStartRow());
                System.out.println(out);
                return out.toString();
            } else if ("sqlserver".equals(dialect)) {

            }
        }

        StringBuilder pageSql = new StringBuilder(200);
        if ("mysql".equals(dialect)) {
            StringBuilder out = new StringBuilder(sql);
            if (StringUtils.isNotEmpty(page.getOrderBy())) {
                out.append(" ORDER BY ").append(page.getOrderBy());
            }
            //buildBaseSql(page, query, select);
            out.append(" LIMIT " + page.getStartRow() + "," + page.getPageSize());
            return out.toString();
        } else if ("sqlserver".equals(dialect)) {
            if (!StringUtils.isEmpty(page.getOrderBy())) {
                pageSql.append("SELECT * FROM (SELECT * ,row_number() OVER(ORDER BY " + page.getOrderBy() + ") _rownumber FROM (" +
                        sql +
                        ") _T1) _T2 WHERE _T2._rownumber BETWEEN " + (page.getStartRow() + 1) + " AND " + page.getEndRow());
            } else {

            }
        }
        log.debug(pageSql);
        return pageSql.toString();
    }

    private StringBuilder buildBaseSql(Page page, SQLSelectQueryBlock query, SQLSelect select) {
        StringBuilder out = new StringBuilder("SELECT ");
        buildSelectItems(query.getSelectList(), out);
        out.append("FROM ");
        out.append(buildFrom(query.getFrom())).append('\n');
        if (query.getWhere() != null)
            out.append("WHERE ").append(query.getWhere()).append('\n');
        SQLSelectGroupByClause group = query.getGroupBy();
        if (group != null) {
            buildGroupBy(group, out);
        }
        if (!StringUtils.isEmpty(page.getOrderBy())) {
            out.append(" ORDER BY ").append(page.getOrderBy());
        } else {
            SQLOrderBy orderBy = select.getOrderBy();
            if (orderBy != null) {
                buildOrderBy(orderBy, out);
            }
        }
        return out;
    }

    private static void buildGroupBy(SQLSelectGroupByClause group, StringBuilder out) {
        out.append("GROUP BY ");
        List<SQLExpr> items = group.getItems();
        for (int i = 0; i < items.size(); i++) {
            SQLExpr item = items.get(i);
            out.append(item);
            if (i < items.size() - 1) {
                out.append(',');
            }
        }
        out.append('\n');
    }

    private static void buildOrderBy(SQLOrderBy orderBy, StringBuilder out) {
        out.append(" ORDER BY ");
        List<SQLSelectOrderByItem> orderByItems = orderBy.getItems();
        for (int i = 0; i < orderByItems.size(); i++) {
            SQLSelectOrderByItem item = orderByItems.get(i);
            out.append(item.getExpr());
            if (item.getType() != null)
                out.append(' ').append(item.getType());
            if (i < orderByItems.size() - 1) {
                out.append(',');
            }
        }
        out.append('\n');
    }

    private static void buildSelectItems(List<SQLSelectItem> items, StringBuilder out) {
        int itemSize = items.size();
        for (int i = 0; i < itemSize; i++) {
            if (i != 0) out.append("       ");
            SQLSelectItem item = items.get(i);
            //SQLSelectQueryBlock ((SQLQueryExpr)item.getExpr()).getSubQuery().toString()
            if (!buildSelectExpr(item.getExpr(), item.getAlias(), out)) {
                out.append(items.get(i).toString());
            }
            if (i < itemSize - 1) {
                out.append(',');
            }
            out.append('\n');
        }
    }

    private static boolean buildSelectExpr(SQLExpr expr, String alias, StringBuilder out) {
        if (expr instanceof SQLQueryExpr) {
            out.append('(').append(((SQLQueryExpr) expr).getSubQuery()).append(')');
            if (StringUtils.isNotEmpty(alias)) {
                out.append(" as ").append(alias);
            }
            return true;
        } else if (expr instanceof SQLCaseExpr) {
            SQLCaseExpr caseExpr = (SQLCaseExpr) expr;
            out.append("(CASE");
            if (caseExpr.getValueExpr() != null) {
                out.append(" ").append(caseExpr.getValueExpr()).append('\n');
            }
            for (SQLCaseExpr.Item caseItem : caseExpr.getItems()) {
                out.append("       WHEN ").append(caseItem.getConditionExpr()).append(" THEN\n");
                out.append("       ");
                if (!buildSelectExpr(caseItem.getValueExpr(), null, out)) {
                    out.append(caseItem.getValueExpr());
                }
                out.append('\n');
            }
            if (caseExpr.getElseExpr() != null) {
                out.append("       ELSE\n").append(caseExpr.getElseExpr()).append('\n');
            }
            out.append("       END) AS ").append(alias);
            return true;
        } else if (expr instanceof SQLAggregateExpr) {
            SQLAggregateExpr sumExpr = (SQLAggregateExpr) expr;
            out.append(sumExpr.getMethodName()).append("(");
            List<SQLExpr> list = sumExpr.getArguments();
            for (int i = 0; i < list.size(); i++) {
                SQLExpr e = list.get(i);
                out.append(e.toString());
                if (i < list.size() - 1) {
                    out.append(",");
                }
            }
            out.append(")");
            if (StringUtils.isNotEmpty(alias)) {
                out.append(" as ").append(alias);
            }
            return true;
        } else {
            return false;
        }
    }

    private static void buildCleanSelectItems(List<SQLSelectItem> items, String prefix, StringBuilder out) {
        int itemSize = items.size();
        for (int i = 0; i < itemSize; i++) {
            if (i != 0) out.append("       ");
            if (StringUtils.isNotEmpty(prefix)) {
                out.append(prefix).append(".");
            }
            SQLSelectItem item = items.get(i);
            SQLExpr expr = item.getExpr();
            if (StringUtils.isNotEmpty(item.getAlias())) {
                out.append(item.getAlias());
            } else if (expr instanceof SQLPropertyExpr) {
                out.append(((SQLPropertyExpr) expr).getName());
            } else if (expr instanceof SQLIdentifierExpr) {
                out.append(((SQLIdentifierExpr) expr).getName());
            } else if (expr instanceof SQLCaseExpr) {
                System.out.println(expr);
            }
            if (i < itemSize - 1) {
                out.append(',');
            }
            out.append('\n');
        }
    }

    private class BoundSqlSqlSource implements SqlSource {
        BoundSql boundSql;

        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        @Override
        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }
    }

    /**
     * 由于MappedStatement是一个全局共享的对象，因而需要复制一个对象来进行操作，防止并发访问导致错误
     *
     * @param ms
     * @param newSqlSource
     * @return
     */
    private MappedStatement newMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
        MappedStatement.Builder builder =
                new MappedStatement.Builder(ms.getConfiguration(), ms.getId() + "_PageHelper", newSqlSource, ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null && ms.getKeyProperties().length != 0) {
            StringBuilder keyProperties = new StringBuilder();
            for (String keyProperty : ms.getKeyProperties()) {
                keyProperties.append(keyProperty).append(",");
            }
            keyProperties.delete(keyProperties.length() - 1, keyProperties.length());
            builder.keyProperty(keyProperties.toString());
        }
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        //由于resultMaps第一次需要返回int类型的结果，所以这里需要生成一个resultMap - 防止并发错误
        List<ResultMap> resultMaps = new ArrayList<ResultMap>();
        ResultMap resultMap = new ResultMap.Builder(ms.getConfiguration(), ms.getId(), int.class, EMPTY_RESULTMAPPING).build();
        resultMaps.add(resultMap);
        builder.resultMaps(resultMaps);
        builder.resultSetType(ms.getResultSetType());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());

        return builder.build();
    }

    /**
     * 只拦截Executor
     *
     * @param target
     * @return
     */
    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    /**
     * 设置属性值
     *
     * @param p
     */
    @Override
    public void setProperties(Properties p) {
        dialect = p.getProperty("dialect");
        if (dialect == null || "".equals(dialect)) {
            throw new RuntimeException("Mybatis分页插件PageHelper无法获取dialect参数!");
        }
        //offset作为PageNum使用
        String offset = p.getProperty("offsetAsPageNum");
        if (offset != null && "TRUE".equalsIgnoreCase(offset)) {
            offsetAsPageNum = true;
        }
        //RowBounds方式是否做count查询
        String withcount = p.getProperty("rowBoundsWithCount");
        if (withcount != null && "TRUE".equalsIgnoreCase(withcount)) {
            rowBoundsWithCount = true;
        }
    }

    public static String getDialect() {
        return dialect;
    }
}
