import com.codeborne.selenide.logevents.SelenideLogger;
import datautil.DataBaseUtil;
import page.PageUtil;
import datautil.UrlUtils;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.val;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import page.ChoiceOfPaymentPage;
import page.PaymentCardPage;
import page.PaymentCreditPage;

import java.sql.SQLException;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BuyTourDayTest {

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    PageUtil pageUtil = new PageUtil();

    @Test
    @DisplayName("Проверка выбора способа оплаты картой")
    void checkChoiceOfPaymentCard() {
        open(UrlUtils.browserHost);
        ChoiceOfPaymentPage choiceOfPaymentPage = new ChoiceOfPaymentPage();
        PaymentCardPage paymentCardPage = choiceOfPaymentPage.checkBuyCardButton();
    }

    @Test
    @DisplayName("Проверка выбора способа оплаты в кредит")
    void checkChoiceOfPaymentCredit() {
        open(UrlUtils.browserHost);
        ChoiceOfPaymentPage choiceOfPaymentPage = new ChoiceOfPaymentPage();
        PaymentCreditPage paymentCreditPage = choiceOfPaymentPage.checkBuyCreditButton();
    }

// Авто-тесты проверки покупки тура по дебетовой карте

    @ParameterizedTest
    @CsvFileSource(resources = "/ValidDataCard.csv", numLinesToSkip = 1)
    @DisplayName("Проверка оплаты по карте с валидными данными")
    void checkValidPaymentCard(String card, String month, String year, String name, String cvc) {
        open(UrlUtils.browserHost);
        ChoiceOfPaymentPage choiceOfPaymentPage = new ChoiceOfPaymentPage();
        PaymentCardPage paymentCardPage = choiceOfPaymentPage.checkBuyCardButton();
        pageUtil.inputCardData(card, month, year, name, cvc);
        pageUtil.checkPaymentValid();
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/NotValidDataCard.csv", numLinesToSkip = 1)
    @DisplayName("Проверка оплаты по карте с невалидными данными")
    void checkNotValidPaymentCard(String card, String month, String year, String name, String cvc, String textError) {
        open(UrlUtils.browserHost);
        ChoiceOfPaymentPage choiceOfPaymentPage = new ChoiceOfPaymentPage();
        PaymentCardPage paymentCardPage = choiceOfPaymentPage.checkBuyCardButton();
        pageUtil.inputCardData(card, month, year, name, cvc);
        pageUtil.checkPaymentNotValid(textError);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/ValidDataCardDeclined.csv", numLinesToSkip = 1)
    @DisplayName("Проверка оплаты по карте с валидными данными статус DECLINED, карта 4444 ... 4444")
    void checkValidPaymentCardDeclined(String card, String month, String year, String name, String cvc) {
        open(UrlUtils.browserHost);
        ChoiceOfPaymentPage choiceOfPaymentPage = new ChoiceOfPaymentPage();
        PaymentCardPage paymentCardPage = choiceOfPaymentPage.checkBuyCardButton();
        pageUtil.inputCardData(card, month, year, name, cvc);
        pageUtil.checkPaymentNotValidCard();
    }

// Авто-тесты проверки покупки тура в кредит

    @ParameterizedTest
    @CsvFileSource(resources = "/ValidDataCard.csv", numLinesToSkip = 1)
    @DisplayName("Проверка оплаты кредитом с валидными данными карты")
    void checkValidPaymentCredit(String card, String month, String year, String name, String cvc) {
        open(UrlUtils.browserHost);
        ChoiceOfPaymentPage choiceOfPaymentPage = new ChoiceOfPaymentPage();
        PaymentCreditPage paymentCreditPage = choiceOfPaymentPage.checkBuyCreditButton();
        pageUtil.inputCardData(card, month, year, name, cvc);
        pageUtil.checkPaymentValid();
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/NotValidDataCard.csv", numLinesToSkip = 1)
    @DisplayName("Проверка оплаты кредитом с невалидными данными карты")
    void checkNotValidPaymentCredit(String card, String month, String year, String name, String cvc, String textError) {
        open(UrlUtils.browserHost);
        ChoiceOfPaymentPage choiceOfPaymentPage = new ChoiceOfPaymentPage();
        PaymentCreditPage paymentCreditPage = choiceOfPaymentPage.checkBuyCreditButton();
        pageUtil.inputCardData(card, month, year, name, cvc);
        pageUtil.checkPaymentNotValid(textError);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/ValidDataCardDeclined.csv", numLinesToSkip = 1)
    @DisplayName("Проверка оплаты по карте с валидными данными статус DECLINED, карта 4444 ... 4444")
    void checkValidCreditCardDeclined(String card, String month, String year, String name, String cvc) {
        open(UrlUtils.browserHost);
        ChoiceOfPaymentPage choiceOfPaymentPage = new ChoiceOfPaymentPage();
        PaymentCreditPage paymentCreditPage = choiceOfPaymentPage.checkBuyCreditButton();
        pageUtil.inputCardData(card, month, year, name, cvc);
        pageUtil.checkPaymentNotValidCard();
    }

// Авто-тесты проверки работы с БД

    @ParameterizedTest
    @CsvFileSource(resources = "/ValidDataCardMysql.csv", numLinesToSkip = 1)
    @DisplayName("Проверка поля amount таблицы payment_entity")
    void checkPaymentAmount(String card, String month, String year, String name, String cvc, String status) throws SQLException {
        DataBaseUtil dataBaseUtil = new DataBaseUtil();
        int countBefore = dataBaseUtil.getNumberOfPaymentRecords();
        open(UrlUtils.browserHost);
        ChoiceOfPaymentPage choiceOfPaymentPage = new ChoiceOfPaymentPage();
        PaymentCardPage paymentCardPage = choiceOfPaymentPage.checkBuyCardButton();
        pageUtil.inputCardData(card, month, year, name, cvc);
        pageUtil.checkPaymentValid();
        int countAfter = dataBaseUtil.getNumberOfPaymentRecords();
        assertEquals(countBefore + 1, countAfter);
        val lastRecord = dataBaseUtil.getLastCreationPaymentRecords();
        val amountCard = lastRecord.getAmount();
        assertEquals(45000, amountCard);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/ValidDataCardMysql.csv", numLinesToSkip = 1)
    @DisplayName("Проверка поля status таблицы payment_entity")
    void checkPaymentStatus(String card, String month, String year, String name, String cvc, String status) throws SQLException {
        DataBaseUtil dataBaseUtil = new DataBaseUtil();
        int countBefore = dataBaseUtil.getNumberOfPaymentRecords();
        open(UrlUtils.browserHost);
        ChoiceOfPaymentPage choiceOfPaymentPage = new ChoiceOfPaymentPage();
        PaymentCardPage paymentCardPage = choiceOfPaymentPage.checkBuyCardButton();
        pageUtil.inputCardData(card, month, year, name, cvc);
        pageUtil.checkPaymentValid();
        int countAfter = dataBaseUtil.getNumberOfPaymentRecords();
        assertEquals(countBefore + 1, countAfter);
        val lastRecord = dataBaseUtil.getLastCreationPaymentRecords();
        val statusCard = lastRecord.getStatus();
        assertEquals(status, statusCard);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/ValidDataCardMysql.csv", numLinesToSkip = 1)
    @DisplayName("Сравнение поля transaction_id таблицы payment_entity c payment_id таблицы order_entity")
    void comparePaymentOrder(String card, String month, String year, String name, String cvc, String status) throws SQLException {
        DataBaseUtil dataBaseUtil = new DataBaseUtil();
        int countBefore = dataBaseUtil.getNumberOfPaymentRecords();
        open(UrlUtils.browserHost);
        ChoiceOfPaymentPage choiceOfPaymentPage = new ChoiceOfPaymentPage();
        PaymentCardPage paymentCardPage = choiceOfPaymentPage.checkBuyCardButton();
        pageUtil.inputCardData(card, month, year, name, cvc);
        pageUtil.checkPaymentValid();
        int countAfter = dataBaseUtil.getNumberOfPaymentRecords();
        assertEquals(countBefore + 1, countAfter);
        val lastRecord = dataBaseUtil.getLastCreationPaymentRecords();
        val transactionCard = lastRecord.getTransaction_id();
        val compareResult = dataBaseUtil.compareOrderPayment(transactionCard);
        assertEquals(1, compareResult);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/ValidDataCardMysql.csv", numLinesToSkip = 1)
    @DisplayName("Проверка поля status таблицы credit_request_entity")
    void checkCreditStatus(String card, String month, String year, String name, String cvc, String status) throws SQLException {
        DataBaseUtil dataBaseUtil = new DataBaseUtil();
        int countBefore = dataBaseUtil.getNumberOfCreditRecords();
        open(UrlUtils.browserHost);
        ChoiceOfPaymentPage choiceOfPaymentPage = new ChoiceOfPaymentPage();
        PaymentCreditPage paymentCreditPage = choiceOfPaymentPage.checkBuyCreditButton();
        pageUtil.inputCardData(card, month, year, name, cvc);
        pageUtil.checkPaymentValid();
        int countAfter = dataBaseUtil.getNumberOfCreditRecords();
        assertEquals(countBefore + 1, countAfter);
        val lastRecord = dataBaseUtil.getLastCreationRecordCredit();
        val statusCard = lastRecord.getStatus();
        assertEquals(status, statusCard);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/ValidDataCardMysql.csv", numLinesToSkip = 1)
    @DisplayName("Сравнение поля bank_id таблицы credit_request c credit_id таблицы order_entity")
    void compareCreditOrder(String card, String month, String year, String name, String cvc, String status) throws SQLException {
        DataBaseUtil dataBaseUtil = new DataBaseUtil();
        int countBefore = dataBaseUtil.getNumberOfCreditRecords();
        open(UrlUtils.browserHost);
        ChoiceOfPaymentPage choiceOfPaymentPage = new ChoiceOfPaymentPage();
        PaymentCreditPage paymentCreditPage = choiceOfPaymentPage.checkBuyCreditButton();
        pageUtil.inputCardData(card, month, year, name, cvc);
        pageUtil.checkPaymentValid();
        int countAfter = dataBaseUtil.getNumberOfCreditRecords();
        assertEquals(countBefore + 1, countAfter);
        val lastRecord = dataBaseUtil.getLastCreationRecordCredit();
        val bank = lastRecord.getBank_id();
        val compareResult = dataBaseUtil.compareOrderCredit(bank);
        assertEquals(1, compareResult);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/NotValidCard.csv", numLinesToSkip = 1)
    @DisplayName("Если номер карты не валидный - в таблицы payment_entity и order_entity не добавляются записи")
    void checkPaymentNotValidCard(String card, String month, String year, String name, String cvc, String status) throws SQLException {
        DataBaseUtil dataBaseUtil = new DataBaseUtil();
        int countBeforePayment = dataBaseUtil.getNumberOfPaymentRecords();
        int countBeforeOrder = dataBaseUtil.getNumberOfOrderRecords();
        open(UrlUtils.browserHost);
        ChoiceOfPaymentPage choiceOfPaymentPage = new ChoiceOfPaymentPage();
        PaymentCardPage paymentCardPage = choiceOfPaymentPage.checkBuyCardButton();
        pageUtil.inputCardData(card, month, year, name, cvc);
        pageUtil.checkPaymentNotValidCard();
        int countAfterCredit = dataBaseUtil.getNumberOfCreditRecords();
        int countAfterOrder = dataBaseUtil.getNumberOfOrderRecords();
        int countAfterPayment = dataBaseUtil.getNumberOfPaymentRecords();
        assertEquals(countBeforePayment, countAfterPayment);
        assertEquals(countBeforeOrder, countAfterOrder);
        assertEquals(countAfterCredit + countAfterPayment, countAfterOrder);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/NotValidCard.csv", numLinesToSkip = 1)
    @DisplayName("Если номер карты не валидный - в таблицы credit_request_entity и order_entity не добавляются записи")
    void checkCreditNotValidCard(String card, String month, String year, String name, String cvc, String status) throws SQLException {
        DataBaseUtil dataBaseUtil = new DataBaseUtil();
        int countBeforeCredit = dataBaseUtil.getNumberOfCreditRecords();
        int countBeforeOrder = dataBaseUtil.getNumberOfOrderRecords();
        open(UrlUtils.browserHost);
        ChoiceOfPaymentPage choiceOfPaymentPage = new ChoiceOfPaymentPage();
        PaymentCreditPage paymentCreditPage = choiceOfPaymentPage.checkBuyCreditButton();
        pageUtil.inputCardData(card, month, year, name, cvc);
        pageUtil.checkPaymentNotValidCard();
        int countAfterCredit = dataBaseUtil.getNumberOfCreditRecords();
        int countAfterOrder = dataBaseUtil.getNumberOfOrderRecords();
        int countAfterPayment = dataBaseUtil.getNumberOfPaymentRecords();
        assertEquals(countBeforeCredit, countAfterCredit);
        assertEquals(countBeforeOrder, countAfterOrder);
        assertEquals(countAfterCredit + countAfterPayment, countAfterOrder);
    }
}