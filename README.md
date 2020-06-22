# pm-web-scraper
## Зависимости 
- ###### Apache Commons IO  
The Apache Commons IO library contains utility classes, stream implementations, file filters, file comparators, endian transformation classes, and much more.
- ###### Apache Commons Lang
Apache Commons Lang, a package of Java utility classes for the classes that are in java.lang's hierarchy, or are considered to be so standard as to justify existence in java.lang.-
 - ###### Wffweb  
A java framework to develop web pages
- ###### HtmlUnit 
It is typically used for testing purposes or to retrieve information from web sites.

## Запуск 
- В maven команда  : clean install exec:java

 - Если не в maven ,в папке lib находятся все зависимые библиотеки.
  Idea: 
    1) В конфигурации Main,указать Use classpath of module-> pm-web-scraper
    2) File->ProjectStructure->Problems , для 4-ех библиотек применить Fix->Add to Dependencies
    3) Далее вкаладка Modules->Dependencies , отмечаем checked на выбранные библиотки, применить-> ок.
    4) Запускаем
 
## Проверка
- В папке report,сгенерируется файл report.html с полученными данными.