package page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$$;
import static org.openqa.selenium.By.cssSelector;

public class PaymentCardPage {

    private By paymentLocator = cssSelector(".heading");
    private SelenideElement paymentCardHead = $$(paymentLocator).findBy(Condition.text("Оплата по карте"));

    public PaymentCardPage() {
        paymentCardHead.waitUntil(Condition.visible, PageUtil.timeOut);
    }
}
