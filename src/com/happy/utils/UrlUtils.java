package com.happy.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sylar on 2017-08-15.
 */
public class UrlUtils {
    private final static String appkey = "7755";
    private final static String trackingId = "hotwish";
    private final static String aliexpressApiBaseUrl = "http://gw.api.alibaba.com/openapi/param2/2/portals.open/api.";
    private final static String api_product_detail = "getPromotionProductDetail";
    private final static String api_product_promotion = "getPromotionLinks";

    private final static String aliexpressPromotionUrlPerfix = "http://s.click.aliexpress.com/deep_link.htm?dl_target_url=";
    private final static String aliexpressPromotionUrlSuffix = "&aff_short_key=FiQNfYv";


    public static String getBaseHref(HttpServletRequest request) {
        String base = request.getContextPath();
        StringBuffer url = new StringBuffer("//").append(request.getServerName());
        if (request.getServerPort() != 80) {
            url.append(":").append(request.getServerPort());
        }
        if (StringUtils.isNotEmpty(base))
            url.append('/').append(base);
        return url.toString();
    }

    public static String toAbsoluteUrl(String url, HttpServletRequest request) {
        if (StringUtils.isEmpty(url)) {
            return url;
        }
        if (!url.matches("^\\w+://")) {
            try {
                if (url.startsWith("/")) {
                    String base = request.getContextPath();
                    if (StringUtils.isNotEmpty(base) && !url.startsWith(base)) {
                        url = base + url;
                    }
                }
                return new URL(new URL(request.getRequestURL().toString()), url).toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return url;
    }

    public static void toAbsoluteUrlForList(HttpServletRequest request, List list, String... fields) {
        for (Object o : list) {
            toAbsoluteUrlForObject(request, o, fields);
        }
    }

    public static void toAbsoluteUrlForObject(HttpServletRequest request, Object object, String... fields) {
        if (fields != null && fields.length > 0) {
            if (object instanceof Map) {
                Map map = (Map) object;
                for (String field : fields) {
                    if (StringUtils.isEmpty(field)) {
                        map.put(field, toAbsoluteUrl(String.valueOf(map.get(field)), request));
                    }
                }
                return;
            }
            Class clazz = object.getClass();
            for (String field : fields) {
                if (StringUtils.isEmpty(field)) continue;

                char first = field.charAt(0);
                if ((first > 'a' && first < 'z') || (first > 'A' && first < 'Z')) {
                    String setter;
                    String getter;
                    if (field.length() == 1) {
                        setter = "set" + StringUtils.upperFirst(field);
                        getter = "get" + StringUtils.upperFirst(field);
                    } else {
                        char sec = field.charAt(1);
                        if ((sec > 'A' && sec < 'Z')) {
                            setter = "set" + field;
                            getter = "get" + field;
                        } else {
                            setter = "set" + StringUtils.upperFirst(field);
                            getter = "get" + StringUtils.upperFirst(field);
                        }
                    }
                    try {
                        Method method = clazz.getMethod(setter, String.class);
                        Method method1 = clazz.getMethod(getter);
                        Object r = method1.invoke(object);
                        method.invoke(object, toAbsoluteUrl(r == null ? null : String.valueOf(r), request));
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    public static String toAbsoluteHtml(HttpServletRequest request, String html) {
        if (StringUtils.isEmpty(html)) {
            return html;
        }
        Document doc = Jsoup.parse(html);
        toAbsoluteHtmlAttr(request, doc, "href");
        toAbsoluteHtmlAttr(request, doc, "src");
        if (html.contains("<html") && html.contains("</html>")) {
            return doc.toString();
        } else {
            return doc.body().html();
        }
    }

    private static void toAbsoluteHtmlAttr(HttpServletRequest request, Document doc, String attrName) {
        Elements elements = doc.select("[" + attrName + "]");
        for (Element e : elements) {
            String attr = e.attr(attrName);
            if (StringUtils.isNotEmpty(attr)) {
                e.attr(attrName, toAbsoluteUrl(attr, request));
            }
        }
    }


}
