import com.codeborne.selenide.logevents.SelenideLogger;
import datautil.DataBaseUtil;
import datautil.UrlLocalHostUtils;
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

    @Test
    @DisplayName("Проверка выбора способа оплаты картой")
    void checkChoiceOfPaymentCard() {
        open(UrlLocalHostUtils.browserHost);
        ChoiceOfPaymentPage choiceOfPaymentPage = new ChoiceOfPaymentPage();
        PaymentCardPage paymentCardPage = choiceOfPaymentPage.checkBuyCardButton();
    }

    @Test
    @DisplayName("Проверка выбора способа оплаты в кредит")
    void checkChoiceOfPaymentCredit() {
        open(UrlLocalHostUtils.browserHost);
        ChoiceOfPaymentPage choiceOfPaymentPage = new ChoiceOfPaymentPage();
        PaymentCreditPage paymentCreditPage = choiceOfPaymentPage.checkBuyCreditButton();
    }

// Авто-тесты проверки покупки тура по дебетовой карте

    @ParameterizedTest
    @CsvFileSource(resources = "/ValidDataCard.csv", numLinesToSkip = 1)
    @DisplayName("Проверка оплаты по карте с валидными данными")
    void checkValidPaymentCard(String card, String month, String year, String name, String cvc, String textStep) {
        open(UrlLocalHostUtils.browserHost);
        ChoiceOfPaymentPage choiceOfPaymentPage = new ChoiceOfPaymentPage();
        PaymentCardPage paymentCardPage = choiceOfPaymentPage.checkBuyCardButton();
        paymentCardPage.inputCardData(card, month, year, name, cvc);
        paymentCardPage.checkPaymentCardValid(textStep);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/NotValidDataCard.csv", numLinesToSkip = 1)
    @DisplayName("Проверка оплаты по карте с невалидными данными")
    void checkNotValidPaymentCard(String card, String month, String year, String name, String cvc, String textError, String textStep) {
        open(UrlLocalHostUtils.browserHost);
        ChoiceOfPaymentPage choiceOfPaymentPage = new ChoiceOfPaymentPage();
        PaymentCardPage paymentCardPage = choiceOfPaymentPage.checkBuyCardButton();
        paymentCardPage.inputCardData(card, month, year, name, cvc);
        paymentCardPage.checkPaymentCardNotValid(textError, textStep);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/ValidDataCardDeclined.csv", numLinesToSkip = 1)
    @DisplayName("Проверка оплаты по карте с валидными данными статус DECLINED, карта 4444 ... 4444")
    void checkValidPaymentCardDeclined(String card, String month, String year, String name, String cvc, String textStep) {
        open(UrlLocalHostUtils.browserHost);
        ChoiceOfPaymentPage choiceOfPaymentPage = new ChoiceOfPaymentPage();
        PaymentCardPage paymentCardPage = choiceOfPaymentPage.checkBuyCardButton();
        paymentCardPage.inputCardData(card, month, year, name, cvc);
        paymentCardPage.checkPaymentNotValidCard(textStep);
    }

// Авто-тесты проверки покупки тура в кредит

    @ParameterizedTest
    @CsvFileSource(resources = "/ValidDataCard.csv", numLinesToSkip = 1)
    @DisplayName("Проверка оплаты кредитом с валидными данными карты")
    void checkValidPaymentCredit(String card, String month, String year, String name, String cvc, String textStep) {
        open(UrlLocalHostUtils.browserHost);
        ChoiceOfPaymentPage choiceOfPaymentPage = new ChoiceOfPaymentPage();
        PaymentCreditPage paymentCreditPage = choiceOfPaymentPage.checkBuyCreditButton();
        paymentCreditPage.inputCardDataCredit(card, month, year, name, cvc);
        paymentCreditPage.checkPaymentCreditValid(textStep);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/NotValidDataCard.csv", numLinesToSkip = 1)
    @DisplayName("Проверка оплаты кредитом с невалидными данными карты")
    void checkNotValidPaymentCredit(String card, String month, String year, String name, String cvc, String textError, String textStep) {
        open(UrlLocalHostUtils.browserHost);
        ChoiceOfPaymentPage choiceOfPaymentPage = new ChoiceOfPaymentPage();
        PaymentCreditPage paymentCreditPage = choiceOfPaymentPage.checkBuyCreditButton();
        paymentCreditPage.inputCardDataCredit(card, month, year, name, cvc);
        paymentCreditPage.checkPaymentCreditNotValid(textError, textStep);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/ValidDataCardDeclined.csv", numLinesToSkip = 1)
    @DisplayName("Проверка оплаты по карте с валидными данными статус DECLINED, карта 4444 ... 4444")
    void checkValidCreditCardDeclined(String card, String month, String year, String name, String cvc, String textStep) {
        open(UrlLocalHostUtils.browserHost);
        ChoiceOfPaymentPage choiceOfPaymentPage = new ChoiceOfPaymentPage();
        PaymentCreditPage paymentCreditPage = choiceOfPaymentPage.checkBuyCreditButton();
        paymentCreditPage.inputCardDataCredit(card, month, year, name, cvc);
        paymentCreditPage.checkPaymentCreditNotValidCard(textStep);
    }

// Авто-тесты проверки работы с БД

    @ParameterizedTest
    @CsvFileSource(resources = "/ValidDataCardMysql.csv", numLinesToSkip = 1)
    @DisplayName("Проверка поля amount таблицы payment_entity")
    void checkPaymentAmount(String card, String month, String year, String name, String cvc, String status, String textStep) throws SQLException {
        DataBaseUtil dataBaseUtil = new DataBaseUtil();
        int countBefore = dataBaseUtil.receivedCountRecords(dataBaseUtil.countPayment);
        open(UrlLocalHostUtils.browserHost);
        ChoiceOfPaymentPage choiceOfPaymentPage = new ChoiceOfPaymentPage();
        PaymentCardPage paymentCardPage = choiceOfPaymentPage.checkBuyCardButton();
        paymentCardPage.inputCardData(card, month, year, name, cvc);
        paymentCardPage.checkPaymentCardValid(textStep);
        int countAfter = dataBaseUtil.receivedCountRecords(dataBaseUtil.countPayment);
        assertEquals(countBefore + 1, countAfter);
        val rs = dataBaseUtil.receiveLastCreationRecordPayment(dataBaseUtil.lastCreationPaymentRecord);
        val amountCard = rs.getAmount();
        assertEquals(45000, amountCard);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/ValidDataCardMysql.csv", numLinesToSkip = 1)
    @DisplayName("Проверка поля status таблицы payment_entity")
    void checkPaymentStatus(String card, String month, String year, String name, String cvc, String status, String textStep) throws SQLException {
        DataBaseUtil dataBaseUtil = new DataBaseUtil();
        int countBefore = dataBaseUtil.receivedCountRecords(dataBaseUtil.countPayment);
        open(UrlLocalHostUtils.browserHost);
        ChoiceOfPaymentPage choiceOfPaymentPage = new ChoiceOfPaymentPage();
        PaymentCardPage paymentCardPage = choiceOfPaymentPage.checkBuyCardButton();
        paymentCardPage.inputCardData(card, month, year, name, cvc);
        paymentCardPage.checkPaymentCardValid(textStep);
        int countAfter = dataBaseUtil.receivedCountRecords(dataBaseUtil.countPayment);
        assertEquals(countBefore + 1, countAfter);
        val rs = dataBaseUtil.receiveLastCreationRecordPayment(dataBaseUtil.lastCreationPaymentRecord);
        val statusCard = rs.getStatus();
        assertEquals(status, statusCard);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/ValidDataCardMysql.csv", numLinesToSkip = 1)
    @DisplayName("Сравнение поля transaction_id таблицы payment_entity c payment_id таблицы order_entity")
    void comparePaymentOrder(String card, String month, String year, String name, String cvc, String status, String textStep) throws SQLException {
        DataBaseUtil dataBaseUtil = new DataBaseUtil();
        int countBefore = dataBaseUtil.receivedCountRecords(dataBaseUtil.countPayment);
        open(UrlLocalHostUtils.browserHost);
        ChoiceOfPaymentPage choiceOfPaymentPage = new ChoiceOfPaymentPage();
        PaymentCardPage paymentCardPage = choiceOfPaymentPage.checkBuyCardButton();
        paymentCardPage.inputCardData(card, month, year, name, cvc);
        paymentCardPage.checkPaymentCardValid(textStep);
        int countAfter = dataBaseUtil.receivedCountRecords(dataBaseUtil.countPayment);
        assertEquals(countBefore + 1, countAfter);
        val rs = dataBaseUtil.receiveLastCreationRecordPayment(dataBaseUtil.lastCreationPaymentRecord);
        val transactionCard = rs.getTransaction_id();
        val tr = dataBaseUtil.compareOrderPaymentCredit(transactionCard, dataBaseUtil.comparePayment);
        assertEquals(1, tr);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/ValidDataCardMysql.csv", numLinesToSkip = 1)
    @DisplayName("Проверка поля status таблицы credit_request_entity")
    void checkCreditStatus(String card, String month, String year, String name, String cvc, String status, String textStep) throws SQLException {
        DataBaseUtil dataBaseUtil = new DataBaseUtil();
        int countBefore = dataBaseUtil.receivedCountRecords(dataBaseUtil.countCredit);
        open(UrlLocalHostUtils.browserHost);
        ChoiceOfPaymentPage choiceOfPaymentPage = new ChoiceOfPaymentPage();
        PaymentCreditPage paymentCreditPage = choiceOfPaymentPage.checkBuyCreditButton();
        paymentCreditPage.inputCardDataCredit(card, month, year, name, cvc);
        paymentCreditPage.checkPaymentCreditValid(textStep);
        int countAfter = dataBaseUtil.receivedCountRecords(dataBaseUtil.countCredit);
        assertEquals(countBefore + 1, countAfter);
        val rs = dataBaseUtil.receiveLastCreationRecordCredit(dataBaseUtil.lastCreationCreditRecord);
        val statusCard = rs.getStatus();
        assertEquals(status, statusCard);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/ValidDataCardMysql.csv", numLinesToSkip = 1)
    @DisplayName("Сравнение поля bank_id таблицы credit_request c credit_id таблицы order_entity")
    void compareCreditOrder(String card, String month, String year, String name, String cvc, String status, String textStep) throws SQLException {
        DataBaseUtil dataBaseUtil = new DataBaseUtil();
        int countBefore = dataBaseUtil.receivedCountRecords(dataBaseUtil.countCredit);
        open(UrlLocalHostUtils.browserHost);
        ChoiceOfPaymentPage choiceOfPaymentPage = new ChoiceOfPaymentPage();
        PaymentCreditPage paymentCreditPage = choiceOfPaymentPage.checkBuyCreditButton();
        paymentCreditPage.inputCardDataCredit(card, month, year, name, cvc);
        paymentCreditPage.checkPaymentCreditValid(textStep);
        int countAfter = dataBaseUtil.receivedCountRecords(dataBaseUtil.countCredit);
        assertEquals(countBefore + 1, countAfter);
        val rs = dataBaseUtil.receiveLastCreationRecordCredit(dataBaseUtil.lastCreationCreditRecord);
        val bank = rs.getBank_id();
        val tr = dataBaseUtil.compareOrderPaymentCredit(bank, dataBaseUtil.compareCredit);
        assertEquals(1, tr);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/NotValidCard.csv", numLinesToSkip = 1)
    @DisplayName("Если номер карты не валидный - в таблицы payment_entity и order_entity не добавляются записи")
    void checkPaymentNotValidCard(String card, String month, String year, String name, String cvc, String status, String textStep) throws SQLException {
        DataBaseUtil dataBaseUtil = new DataBaseUtil();
        int countBeforePayment = dataBaseUtil.receivedCountRecords(dataBaseUtil.countPayment);
        int countBeforeOrder = dataBaseUtil.receivedCountRecords(dataBaseUtil.countOrder);
        open(UrlLocalHostUtils.browserHost);
        ChoiceOfPaymentPage choiceOfPaymentPage = new ChoiceOfPaymentPage();
        PaymentCardPage paymentCardPage = choiceOfPaymentPage.checkBuyCardButton();
        paymentCardPage.inputCardData(card, month, year, name, cvc);
        paymentCardPage.checkPaymentNotValidCard(textStep);
        int countAfterCredit = dataBaseUtil.receivedCountRecords(dataBaseUtil.countCredit);
        int countAfterOrder = dataBaseUtil.receivedCountRecords(dataBaseUtil.countOrder);
        int countAfterPayment = dataBaseUtil.receivedCountRecords(dataBaseUtil.countPayment);
        assertEquals(countBeforePayment, countAfterPayment);
        assertEquals(countBeforeOrder, countAfterOrder);
        assertEquals(countAfterCredit + countAfterPayment, countAfterOrder);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/NotValidCard.csv", numLinesToSkip = 1)
    @DisplayName("Если номер карты не валидный - в таблицы credit_request_entity и order_entity не добавляются записи")
    void checkCreditNotValidCard(String card, String month, String year, String name, String cvc, String status, String textStep) throws SQLException {
        DataBaseUtil dataBaseUtil = new DataBaseUtil();
        int countBeforeCredit = dataBaseUtil.receivedCountRecords(dataBaseUtil.countCredit);
        int countBeforeOrder = dataBaseUtil.receivedCountRecords(dataBaseUtil.countOrder);
        open(UrlLocalHostUtils.browserHost);
        ChoiceOfPaymentPage choiceOfPaymentPage = new ChoiceOfPaymentPage();
        PaymentCreditPage paymentCreditPage = choiceOfPaymentPage.checkBuyCreditButton();
        paymentCreditPage.inputCardDataCredit(card, month, year, name, cvc);
        paymentCreditPage.checkPaymentCreditNotValidCard(textStep);
        int countAfterCredit = dataBaseUtil.receivedCountRecords(dataBaseUtil.countCredit);
        int countAfterOrder = dataBaseUtil.receivedCountRecords(dataBaseUtil.countOrder);
        int countAfterPayment = dataBaseUtil.receivedCountRecords(dataBaseUtil.countPayment);
        assertEquals(countBeforeCredit, countAfterCredit);
        assertEquals(countBeforeOrder, countAfterOrder);
        assertEquals(countAfterCredit + countAfterPayment, countAfterOrder);
    }
}