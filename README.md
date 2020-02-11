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
  
  [Issues #1 - Не верный заголовок страницы в браузере](https://github.com/anmak70/aqa-diplom/issues/1#issue-554391611)
  
  [Issues #2 - Ошибка в названии страны тура](https://github.com/anmak70/aqa-diplom/issues/2#issue-554394677)
  
  [Issues #3 - Ошибочно появляется всплывающее окно](https://github.com/anmak70/aqa-diplom/issues/3#issue-554400788)
  
  [Issues #4 - После активации не изменяется цвет кнопок](https://github.com/anmak70/aqa-diplom/issues/4#issue-554403387)
  
  [Issues #21 - Не исчезают placeholder с ошибками после ввода валидных данных (карта)](https://github.com/anmak70/aqa-diplom/issues/21#issue-554970183)
  
  [Issues #22 - Не исчезают placeholder с ошибками после ввода валидных данных (кредит)](https://github.com/anmak70/aqa-diplom/issues/22#issue-554979690)
  
### [Отчет о проведенном тестировании](https://github.com/anmak70/aqa-diplom/blob/master/Report.md) 

### [Отчёт о проведённой автоматизации](Summary.md)
