# pm-web-scraper

## Запуск 
- В maven команда  : clean install exec:java

 - Если не в maven ,в папке lib находятся все зависимые библиотеки.
  Idea: 
    1) В конфигурации Main,указать Use classpath of module-> pm-web-scraper
    2) File->ProjectStructure->Problems , для библиотеки(lib) применить Fix->Add to Dependencies
    3) Далее вкаладка Modules->Dependencies , отмечаем checked на выбранные библиотки, применить-> ок.
    4) Запускаем
 
## Проверка
- В папке report,сгенерируется файл report.html с полученными данными.