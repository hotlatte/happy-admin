package com.happy.utils;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sylar on 2017-08-15.
 */
public class PageInfo {

    private Page page;

    private int pageNum;
    private int pageSize;
    private long total;
    private int pages;

    private String urlFormat;
    private HttpServletRequest request;
    private int pageSpan = 5;
    private boolean showFirst = true;
    private boolean showLast = true;
    private boolean showPrev = true;
    private boolean showNext = true;

    private PageInfo() {
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
        this.pages = (int) (total / this.pageSize + ((total % this.pageSize == 0) ? 0 : 1));
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public String getUrlFormat() {
        return urlFormat;
    }

    public void setUrlFormat(String urlFormat) {
        this.urlFormat = urlFormat;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public int getPageSpan() {
        return pageSpan;
    }

    public void setPageSpan(int pageSpan) {
        this.pageSpan = pageSpan;
    }

    public boolean isShowFirst() {
        return showFirst;
    }

    public void setShowFirst(boolean showFirst) {
        this.showFirst = showFirst;
    }

    public boolean isShowLast() {
        return showLast;
    }

    public void setShowLast(boolean showLast) {
        this.showLast = showLast;
    }

    public boolean isShowPrev() {
        return showPrev;
    }

    public void setShowPrev(boolean showPrev) {
        this.showPrev = showPrev;
    }

    public boolean isShowNext() {
        return showNext;
    }

    public void setShowNext(boolean showNext) {
        this.showNext = showNext;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public String getPageHtml() {
        if (request != null) {
            Map<String, String[]> map = buildRequestMap();
            int start = Math.max(1, getPageNum() - getPageSpan());
            int end = Math.min(getPages(), getPageNum() + getPageSpan());
            int current = getPageNum();
            Document doc = new Document("");
            Element body = doc.createElement("body");
            Element div = doc.createElement("span");
            div.append("共 " + getTotal() + " 条数据,每页显示 "
                    + getPageSize() +
                    " 条数据,当前第 " + current + "/" + getPages() + "页 &nbsp;&nbsp;");
            body.appendChild(div);
            if (total == 0) {
                body.append("<div class='no-data'>没有数据</div>");
            }
            if (showFirst && getPageNum() > 1) {
                Element a = doc.createElement("a");
                a.attr("href", buildurl(map, 1));
                a.html("首页");
                body.appendChild(a);
            }
            if (showPrev && getPageNum() > 1) {
                Element a = doc.createElement("a");
                a.attr("href", buildurl(map, Math.max(1, current - 1)));
                a.html("上一页");
                body.appendChild(a);
            }
            for (int i = start; i <= end; i++) {
                if (i == current) {
                    Element span = doc.createElement("em");
                    span.html(String.valueOf(i));
                    body.appendChild(span);
                } else {
                    Element a = doc.createElement("a");
                    a.html(String.valueOf(i));
                    a.attr("href", buildurl(map, i));
                    body.appendChild(a);
                }
            }
            if (showNext && getPageNum() < getPages()) {
                Element a = doc.createElement("a");
                a.attr("href", buildurl(map, Math.min(getPages(), current + 1)));
                a.html("下一页");
                body.appendChild(a);
            }
            if (showLast && getPageNum() < getPages()) {
                Element a = doc.createElement("a");
                a.attr("href", buildurl(map, getPages()));
                a.html("尾页");
                body.appendChild(a);
            }
            return body.html();
        }
        return "";
    }

    private String buildurl(Map<String, String[]> map, int page) {
        if (StringUtils.isEmpty(urlFormat)) {
            map.put("pn", new String[]{String.valueOf(page)});

            StringBuffer sb = new StringBuffer();
            for (Map.Entry<String, String[]> entry : map.entrySet()) {
                for (String s : entry.getValue()) {
                    sb.append(entry.getKey()).append("=").append(StringUtils.encodeURI(s)).append("&");
                }
            }
            String url = sb.toString();
            if (url.endsWith("&")) {
                url = url.substring(0, url.length() - 1);
            }
            return request.getRequestURI() + "?" + url;
        } else {
            StringBuffer sb = new StringBuffer();
            sb.append(urlFormat.replace("{pageNum}", String.valueOf(page)).replace("{pageSize}", String.valueOf(pageSize)));
            String query = request.getQueryString();
            if (StringUtils.isNotEmpty(query)) {
                sb.append("?").append(query);
            }
            return UrlUtils.toAbsoluteUrl(sb.toString(), getRequest());
        }
    }

    private Map<String, String[]> buildRequestMap() {
        Map<String, String[]> map = new HashMap<String, String[]>();
        Map<String, String[]> paramMap = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
            String name = entry.getKey();
            String[] value = entry.getValue();
            map.put(name, value);
        }
        return map;
    }


    public static PageInfo fromPage(Page page) {
        PageInfo info = new PageInfo();

        info.setPageNum(page.getPageNum());
        info.setPageSize(page.getPageSize());
        info.setTotal(page.getTotal());
        info.setPage(page);

        return info;
    }
}
