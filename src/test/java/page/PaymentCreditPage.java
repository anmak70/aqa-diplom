package page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$$;
import static org.openqa.selenium.By.cssSelector;

public class PaymentCreditPage {

    private By paymentLocator = cssSelector(".heading");
    private SelenideElement paymentCreditHead = $$(paymentLocator).findBy(Condition.text("Кредит по данным карты"));

    public PaymentCreditPage() {
        paymentCreditHead.waitUntil(Condition.visible, PageUtil.timeOut);
    }
}
