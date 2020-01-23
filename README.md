# aqa-diplom

### [План выполнения дипломного проекта](https://github.com/anmak70/aqa-diplom/blob/master/Plan.md)

### Запуск автотестов

  1. Запускаем Docker командой docker-compoce up. Требуется установка Docker, если не установлен. Ждем подъема БД
  2. Запускаем приложение командой java -jar aqa-shop.jar
  3. Запускаем симулятор банковских сервисов, для запуска нужен установленный Node.js. Симулятор расположен в каталоге gate-simulator.          Для запуска необходимо перейти в этот каталог и запустить командой npm start.
  4. После подъема приложений запускаем автотесты командой gradlew clean test allureReport
  5. Для получения отчетов после выполнения автотестов запускаем команду gradlew allureServe
  
  
