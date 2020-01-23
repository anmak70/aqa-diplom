package page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$$;
import static org.openqa.selenium.By.cssSelector;

public class PaymentCreditPage {

    By paymentLocator = cssSelector(".heading");
    By inputLocator = cssSelector(".input__inner");
    By buttonLocator = cssSelector(".button__text");
    By approvedLocator = cssSelector(".notification__content");
    By errorLocator = cssSelector(".input__sub");

    private SelenideElement paymentCreditHead = $$(paymentLocator).findBy(Condition.text("Кредит по данным карты"));
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

    public PaymentCreditPage() {
        paymentCreditHead.waitUntil(Condition.visible, 10000);
    }

    @Step("Выполняется проверка оплаты кредитом валидные данные - {textStep}")
    public void checkPaymentCreditValid(String textStep) {
        buttonContinue.click();
        approvedOperation.waitUntil(Condition.visible, 40000);
    }

    @Step("Выполняется проверка появления всплывающего окна с ошибкой - Банк отклонил операцию - {textStep}")
    public void checkPaymentCreditNotValidCard(String textStep) {
        buttonContinue.click();
        errorOperation.waitUntil(Condition.visible, 40000);
    }

    @Step("Выполняется проверка оплаты кредитом не валидные данные - {textStep}")
    public void checkPaymentCreditNotValid(String textError, String textStep) {
        buttonContinue.click();
        SelenideElement errorField = errorText.findBy(Condition.text(textError));
        errorField.waitUntil(Condition.visible, 40000);
    }

    @Step("Заполнение полей карты данными - кредит")
    public void inputCardDataCredit(String card, String month, String year, String name, String cvc) {
        inputCard.setValue(card);
        inputMonth.setValue(month);
        inputYear.setValue(year);
        inputName.setValue(name);
        inputCVC.setValue(cvc);
    }
}
