package page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$$;
import static org.openqa.selenium.By.cssSelector;

public class ChoiceOfPaymentPage {

    By buttonLocator = cssSelector(".button__text");

    private SelenideElement buyCardButton = $$(buttonLocator).findBy(Condition.text("Купить"));
    private SelenideElement buyCreditButton = $$(buttonLocator).findBy(Condition.text("Купить в кредит"));

    public PaymentCardPage checkBuyCardButton() {
        buyCardButton.click();
        return new PaymentCardPage();
    }

    public PaymentCreditPage checkBuyCreditButton() {
        buyCreditButton.click();
        return new PaymentCreditPage();
    }
}
