package page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$$;
import static org.openqa.selenium.By.cssSelector;

public class PageUtil {

    public static int timeOut = 40000;

    private By inputLocator = cssSelector(".input__inner");
    private By buttonLocator = cssSelector(".button__text");
    private By approvedLocator = cssSelector(".notification__content");
    private By errorLocator = cssSelector(".input__sub");

    private ElementsCollection inputCardData = $$(inputLocator);
    private SelenideElement buttonContinue = $$(buttonLocator).findBy(Condition.text("Продолжить"));
    private SelenideElement approvedOperation = $$(approvedLocator).findBy(Condition.text("Операция одобрена Банком."));
    private SelenideElement errorOperation = $$(approvedLocator).findBy(Condition.text("Банк отказал в проведении операции"));
    private SelenideElement inputCard = inputCardData.findBy(Condition.text("Номер карты")).$("[class=input__control]");
    private SelenideElement inputMonth = inputCardData.findBy(Condition.text("Месяц")).$("[class=input__control]");
    private SelenideElement inputYear = inputCardData.findBy(Condition.text("Год")).$("[class=input__control]");
    private SelenideElement inputName = inputCardData.findBy(Condition.text("Владелец")).$("[class=input__control]");
    private SelenideElement inputCVC = inputCardData.findBy(Condition.text("CVC/CVV")).$("[class=input__control]");
    private ElementsCollection errorText = $$(errorLocator);

    @Step("Заполнение полей карты данными")
    public void inputCardData(String card, String month, String year, String name, String cvc) {
        inputCard.setValue(card);
        inputMonth.setValue(month);
        inputYear.setValue(year);
        inputName.setValue(name);
        inputCVC.setValue(cvc);
    }

    @Step("Выполняется проверка оплаты картой валидные данные")
    public void checkPaymentValid() {
        buttonContinue.click();
        approvedOperation.waitUntil(Condition.visible, timeOut);
    }

    @Step("Выполняется проверка оплаты кредитом не валидные данные")
    public void checkPaymentNotValid(String textError) {
        buttonContinue.click();
        SelenideElement errorField = errorText.findBy(Condition.text(textError));
        errorField.waitUntil(Condition.visible, timeOut);
    }

    @Step("Выполняется проверка появления всплывающего окна с ошибкой - Банк отклонил операцию")
    public void checkPaymentNotValidCard() {
        buttonContinue.click();
        errorOperation.waitUntil(Condition.visible, timeOut);
    }

}
