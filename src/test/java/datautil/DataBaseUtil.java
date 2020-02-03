package datautil;

import io.qameta.allure.Step;
import lombok.val;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseUtil {

    private static String urlSql = System.getProperty("urlDb");
    private static String user = "app";
    private static String password = "pass";

    private String countPayment = "SELECT COUNT(*) FROM payment_entity;";
    private String countCredit = "SELECT COUNT(*) FROM credit_request_entity;";
    private String countOrder = "SELECT COUNT(*) FROM order_entity;";
    private String lastCreationPaymentRecord = "SELECT amount, status, transaction_id FROM payment_entity ORDER BY created DESC LIMIT 1;";
    private String lastCreationCreditRecord = "SELECT bank_id, status FROM credit_request_entity ORDER BY created DESC LIMIT 1;";
    private String comparePayment = "SELECT COUNT(*) FROM order_entity WHERE payment_id = ?;";
    private String compareCredit = "SELECT COUNT(*) FROM order_entity WHERE credit_id = ?;";

    public DataBaseUtil() {
    }

    public int getNumberOfPaymentRecords() {
        int count = receivedCountRecords(countPayment);
        return count;
    }

    public int getNumberOfCreditRecords() {
        int count = receivedCountRecords(countCredit);
        return count;
    }

    public int getNumberOfOrderRecords() {
        int count = receivedCountRecords(countOrder);
        return count;
    }

    public Payment getLastCreationPaymentRecords() {
        Payment lastRecord = lastCreationPaymentRecords();
        return lastRecord;
    }

    public Credit getLastCreationRecordCredit() {
        Credit lastRecord = lastCreationRecordCredit();
        return lastRecord;
    }

    public int compareOrderPayment(String transaction) {
        String transactionId = transaction;
        int compare = compareOrderPaymentId(transactionId);
        return compare;
    }

    public int compareOrderCredit(String transaction) {
        String transactionId = transaction;
        int compare = compareOrderCreditId(transactionId);
        return compare;
    }

    private int receivedCountRecords(String request) {
        val runner = new QueryRunner();
        int count = 0;
        try (
                val conn = DriverManager.getConnection(urlSql, user, password)
        ) {
            val countRecord = runner.query(conn, request, new ScalarHandler<>());
            count = Integer.parseInt(countRecord.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    private Payment lastCreationPaymentRecords() {
        val runner = new QueryRunner();
        Payment lastRecord = null;
        try (
                val conn = DriverManager.getConnection(urlSql, user, password);
        ) {
            lastRecord = runner.query(conn, lastCreationPaymentRecord, new BeanHandler<>(Payment.class));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lastRecord;
    }

    private Credit lastCreationRecordCredit() {
        val runner = new QueryRunner();
        Credit lastRecord = null;
        try (
                val conn = DriverManager.getConnection(urlSql, user, password);
        ) {
            lastRecord = runner.query(conn, lastCreationCreditRecord, new BeanHandler<>(Credit.class));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lastRecord;
    }

    @Step("Сравнение transaction_id таблицы payment_entity с payment_id таблицы order_entity")
    private int compareOrderPaymentId(String transaction) {
        val runner = new QueryRunner();
        int compare = 0;
        try (
                val conn = DriverManager.getConnection(urlSql, user, password);
        ) {
            val compareResult = runner.query(conn, comparePayment, new ScalarHandler<>(), transaction);
            compare = Integer.parseInt(compareResult.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return compare;
    }

    @Step("Сравнение bank_id таблицы credit_request_entity с credit_id таблицы order_entity")
    private int compareOrderCreditId(String transaction) {
        val runner = new QueryRunner();
        int compare = 0;
        try (
                val conn = DriverManager.getConnection(urlSql, user, password);
        ) {
            val compareResult = runner.query(conn, compareCredit, new ScalarHandler<>(), transaction);
            compare = Integer.parseInt(compareResult.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return compare;
    }

}
