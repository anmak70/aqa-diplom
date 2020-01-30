package page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$$;
import static org.openqa.selenium.By.cssSelector;

public class ChoiceOfPaymentPage {

    private By buttonLocator = cssSelector(".button__text");
    private By paymentLocator = cssSelector(".heading");

    private SelenideElement buyCardButton = $$(buttonLocator).findBy(Condition.text("Купить"));
    private SelenideElement buyCreditButton = $$(buttonLocator).findBy(Condition.text("Купить в кредит"));
    private SelenideElement paymentCardHead = $$(paymentLocator).findBy(Condition.text("Оплата по карте"));
    private SelenideElement paymentCreditHead = $$(paymentLocator).findBy(Condition.text("Кредит по данным карты"));

    public PaymentCardPage checkBuyCardButton() {
        buyCardButton.click();
        return new PaymentCardPage();
    }

    public PaymentCreditPage checkBuyCreditButton() {
        buyCreditButton.click();
        return new PaymentCreditPage();
    }

    public void chooseCardPayment() {
        buyCardButton.click();
        paymentCardHead.waitUntil(Condition.visible, PageUtil.timeOut);
    }

    public void chooseCreditPayment() {
        buyCreditButton.click();
        paymentCreditHead.waitUntil(Condition.visible, PageUtil.timeOut);
    }

}
