package com;

import com.handler.WebParser;

public class MainApp {
    // private static BrowserEngine webkit = BrowserFactory.getBrowser(BrowserType.WebKit);

    public static void main(String[] args) {

        WebParser webParser = new WebParser();
        webParser.likeAMain();
    }
}
