package datautil;

import io.qameta.allure.Step;
import lombok.val;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseUtil {

    private static String urlMysql = "jdbc:mysql://192.168.99.100:3306/app";
    private static String user = "app";
    private static String password = "pass";

    public static String countPayment = "SELECT COUNT(*) FROM payment_entity;";
    public static String countCredit = "SELECT COUNT(*) FROM credit_request_entity;";
    public static String countOrder = "SELECT COUNT(*) FROM order_entity;";
    public static String lastCreationPaymentRecord = "SELECT amount, status, transaction_id FROM payment_entity ORDER BY created DESC LIMIT 1;";
    public static String lastCreationCreditRecord = "SELECT bank_id, status FROM credit_request_entity ORDER BY created DESC LIMIT 1;";
    public static String comparePayment = "SELECT COUNT(*) FROM order_entity WHERE payment_id = ?;";
    public static String compareCredit = "SELECT COUNT(*) FROM order_entity WHERE credit_id = ?;";

    public DataBaseUtil() throws SQLException {
    }

    public int receivedCountRecords(String request) throws SQLException {
        val runner = new QueryRunner();
        try (
                val conn = DriverManager.getConnection(urlMysql, user, password);
        ) {
            val cn = runner.query(conn, request, new ScalarHandler<>());
            int count = Integer.parseInt(cn.toString());
            return count;
        }
    }

    public Payment receiveLastCreationRecordPayment(String request) throws SQLException {
        val runner = new QueryRunner();
        try (
                val conn = DriverManager.getConnection(urlMysql, user, password);
        ) {
            val rs = runner.query(conn, request, new BeanHandler<>(Payment.class));
            return rs;
        }
    }

    public Credit receiveLastCreationRecordCredit(String request) throws SQLException {
        val runner = new QueryRunner();
        try (
                val conn = DriverManager.getConnection(urlMysql, user, password);
        ) {
            val rs = runner.query(conn, request, new BeanHandler<>(Credit.class));
            return rs;
        }
    }

    @Step("Сравнение transaction_id/bank_id таблицы payment_entity/credit_request_entity с payment_id/credit_id таблицы order_entity")
    public int compareOrderPaymentCredit(String transaction, String request) throws SQLException {
        val runner = new QueryRunner();
        try (
                val conn = DriverManager.getConnection(urlMysql, user, password);
        ) {
            val rs = runner.query(conn, request, transaction, new ScalarHandler<>());
            int cp = Integer.parseInt(rs.toString());
            return cp;
        }
    }



}
