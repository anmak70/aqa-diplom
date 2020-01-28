package datautil;

import io.qameta.allure.Step;
import lombok.val;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseUtil {

    private static String urlSql = "jdbc:mysql://192.168.99.100:3306/app";
    private static String user = "app";
    private static String password = "pass";

    private String countPayment = "SELECT COUNT(*) FROM payment_entity;";
    private String countCredit = "SELECT COUNT(*) FROM credit_request_entity;";
    private String countOrder = "SELECT COUNT(*) FROM order_entity;";
    private String lastCreationPaymentRecord = "SELECT amount, status, transaction_id FROM payment_entity ORDER BY created DESC LIMIT 1;";
    private String lastCreationCreditRecord = "SELECT bank_id, status FROM credit_request_entity ORDER BY created DESC LIMIT 1;";
    private String comparePayment = "SELECT COUNT(*) FROM order_entity WHERE payment_id = ?;";
    private String compareCredit = "SELECT COUNT(*) FROM order_entity WHERE credit_id = ?;";

    public DataBaseUtil() throws SQLException {
    }

    public int getNumberOfPaymentRecords() throws SQLException {
        int count = receivedCountRecords(countPayment);
        return count;
    }

    public int getNumberOfCreditRecords() throws SQLException {
        int count = receivedCountRecords(countCredit);
        return count;
    }

    public int getNumberOfOrderRecords() throws SQLException {
        int count = receivedCountRecords(countOrder);
        return count;
    }

    private int receivedCountRecords(String request) throws SQLException {
        val runner = new QueryRunner();
        try (
                val conn = DriverManager.getConnection(urlSql, user, password);
        ) {
            val countRecord = runner.query(conn, request, new ScalarHandler<>());
            int count = Integer.parseInt(countRecord.toString());
            return count;
        }
    }

    public Payment getLastCreationPaymentRecords() throws SQLException {
        val runner = new QueryRunner();
        try (
                val conn = DriverManager.getConnection(urlSql, user, password);
        ) {
            val lastRecord = runner.query(conn, lastCreationPaymentRecord, new BeanHandler<>(Payment.class));
            return lastRecord;
        }
    }

    public Credit getLastCreationRecordCredit() throws SQLException {
        val runner = new QueryRunner();
        try (
                val conn = DriverManager.getConnection(urlSql, user, password);
        ) {
            val lastRecord = runner.query(conn, lastCreationCreditRecord, new BeanHandler<>(Credit.class));
            return lastRecord;
        }
    }

    @Step("Сравнение transaction_id таблицы payment_entity с payment_id таблицы order_entity")
    public int compareOrderPayment(String transaction) throws SQLException {
        val runner = new QueryRunner();
        try (
                val conn = DriverManager.getConnection(urlSql, user, password);
        ) {
           val compareResult = runner.query(conn, comparePayment, new ScalarHandler<>(), transaction);
            int compare = Integer.parseInt(compareResult.toString());
            return compare;
        }
    }

    @Step("Сравнение bank_id таблицы credit_request_entity с credit_id таблицы order_entity")
    public int compareOrderCredit(String transaction) throws SQLException {
        val runner = new QueryRunner();
        try (
                val conn = DriverManager.getConnection(urlSql, user, password);
        ) {
            val compareResult = runner.query(conn, compareCredit, new ScalarHandler<>(), transaction);
            int compare = Integer.parseInt(compareResult.toString());
            return compare;
        }
    }

}
