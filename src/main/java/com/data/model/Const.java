package com.data.model;

public final class Const {

    /**
     * Победа хозяев
     */
    public static final String P1_TITLE = "П1";

    /**
     * Победа гостей
     */
    public static final String P2_TITLE = "П2";

    /**
     * Ничья X(кирилица)
     */
    public static final String X_TITLE = "Х";

    /**
     * Ничья X(Латиница)
     */
    public static final String X_TITLE_LAT = "X";


    /**
     * Дата
     */
    public static final String DATE_TITLE = "Дата";


    /**
     * Событие
     */
    public static final String EVENT_TITLE = "Событие";

    /**
     * URL сайта для парсинга
     */
    public static final String SEARCH_URL = "https://pm.by";

    /**
     * Наименование аттрибута хранящего id(ссылка на контент,id таблицы)
     */
    public static final String HD_ATTRIBUTE = "hd";

    /**
     * Путь к таблице меню
     */
    public static final String XPATH_NAVIGATIONAL_MENU = "//ul[@id='lobbySportsHolder']//ul[1]//a[1]";

    /**
     * Путь к таблице
     */
    public static final String HD_CONTENT_URL = "/sbet.content.html?hd=";

    /**
     * HS параметр
     */
    public static final String HS_PARAM_URL = "&hs=1&lang=ru";

    /**
     * Tag таблицы
     */
    public static final String TABLE_TAG_VALUE = "table";

    /**
     * Ид атрибут
     */
    public static final String ID_ATTRIBUTE = "id";

    /**
     * Стиль атрибут
     */
    public static final String STYLE_ATTRIBUTE = "style";

    /**
     * Стиль атрибут
     */
    public static final String CLASS_ATTRIBUTE = "class";

    /**
     * Стиль атрибут
     */
    public static final String SPACE_ATTRIBUTE = "spacer";

    /**
     * Стиль атрибут
     */
    public static final String NON_DISPLAY_ATTRIBUTE = "spacer";

    /**
     * Стиль атрибут
     */
    public static final String DATE_FORMAT_PATTERN = "yyyy-dd-MM hh:mm";


    /**
     * Стиль атрибут
     */
    public static final String ERROR_MSG = "Error message:";

    /**
     * Стиль атрибут
     */
    public static final String REPORT_FILE_PATH = "report/report.html";


    private Const() {
    }
}
