# aqa-diplom

### [План выполнения дипломного проекта](https://github.com/anmak70/aqa-diplom/blob/master/Plan.md)

### Запуск автотестов

  1. Запускаем Docker командой docker-compoce up. Требуется установка Docker, если не установлен. Ждем подъема БД
  
### Работа с MySQL  
  2. Запускаем приложение командой java -jar aqa-shop.jar --spring.profiles.active=mysql
  3. Запускаем симулятор банковских сервисов, для запуска нужен установленный Node.js. Симулятор расположен в каталоге gate-simulator.          Для запуска необходимо перейти в этот каталог и запустить командой npm start.
  4. После подъема приложений запускаем автотесты командой gradlew clean test allureReport
  5. Для получения отчетов после выполнения автотестов запускаем команду gradlew allureServe
  
### Работа с Postgre
  2. Запускаем приложение командой java -jar aqa-shop.jar --spring.profiles.active=postgresql
  3. Запускаем симулятор банковских сервисов, для запуска нужен установленный Node.js. Симулятор расположен в каталоге gate-simulator.          Для запуска необходимо перейти в этот каталог и запустить командой npm start.
  4. После подъема приложений запускаем автотесты командой gradlew clean test -DurlDb=jdbc:postgresql://localhost:5432/app allureReport
     При работе в Windows10 Home c Docker Quickstart Terminal заменить localhost на 192.168.99.100
  5. Для получения отчетов после выполнения автотестов запускаем команду gradlew allureServe
  
### Issues
  На Issues № #1, #2, #3, #4, #21, #22 авто-тесты не делались. Остальные issues написаны по авто-тестам.
  
### [Отчет о проведенной автоматизации (предварительно)](https://github.com/anmak70/aqa-diplom/blob/master/Report.md)  
