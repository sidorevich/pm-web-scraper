package com.util;

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
     * Атрибут стиля
     */
    public static final String STYLE_ATTRIBUTE = "style";

    /**
     * Атрибут класса
     */
    public static final String CLASS_ATTRIBUTE = "class";

    /**
     * Атрибут проставки
     */
    public static final String SPACE_ATTRIBUTE = "spacer";

    /**
     * Атрибут сокрытия отображения
     */
    public static final String NON_DISPLAY_ATTRIBUTE = "display:none";

    /**
     * Паттерн даты
     */
    public static final String DATE_FORMAT_PATTERN = "yyyy-dd-MM hh:mm";


    /**
     * Сообщение ошибки
     */
    public static final String ERROR_MSG = "Error message:";

    /**
     * Путь и наименование конечного файла
     */
    public static final String REPORT_FILE_PATH = "report/report.html";

    /**
     * Атрибут ссылки
     */
    public static final String HREF_ATTRIBUTE = "href";

    /**
     * Атрибут рамки
     */
    public static final String BORDER_ATTRIBUTE = "border";

    /**
     * Заголовок вида спорта
     */
    public static final String KIND_SPORT_TITLE = "Вид спорта";

    /**
     * Заголовок турнира
     */
    public static final String EVENT_TABLE_TITLE = "Турнир";

    /**
     * Заголовок домашней команды
     */
    public static final String HOME_TEAM_TABLE_TITLE = "Домашная команда";

    /**
     * Заголовок гостевой команды
     */
    public static final String VISIT_TEAM_TABLE_TITLE = "Команда гостей";

    /**
     * Заголовок начала событий
     */
    public static final String DATE_START_TABLE_TITLE = "Начало события";

    /**
     * Заголовок ссылки
     */
    public static final String LINK_TABLE_TITLE = "Ссылка";

    /**
     * Значение отсутвует
     */
    public static final String MISSING_VALUE = "-";

    /**
     * Tag span
     */
    public static final String SPAN_TAG = "span";


    private Const() {
    }
}
