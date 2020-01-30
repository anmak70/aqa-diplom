package page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$$;
import static org.openqa.selenium.By.cssSelector;

public class PaymentCreditPage {

    public PaymentCreditPage() {
        By paymentLocator = cssSelector(".heading");
        SelenideElement paymentCreditHead = $$(paymentLocator).findBy(Condition.text("Кредит по данным карты"));
        paymentCreditHead.waitUntil(Condition.visible, PageUtil.timeOut);
    }
}
