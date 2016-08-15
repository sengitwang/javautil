package com.senit.javautil.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by sen on 2016/8/15.
 */
public class CookieUtil {
    public static final Cookie findCookie(HttpServletRequest req, String name) {
        for (Cookie ck : req.getCookies()) {
            if (ck.getName().equals(name))
                return ck;
        }
        return null;
    }
}
