package eu.greyson.paymentTracker;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Assert;
import org.junit.Test;

public class PaymentTrackerTest {

  @Test
  public void test_01_balancesToString() {

    Map<String, BigDecimal> balances = new ConcurrentHashMap();

    Assert.assertEquals("Ověření, že prázdné pole nevygeneruje žádné znaky", "", PaymentTracker.balancesToString(balances));
  }

  @Test
  public void test_02_balancesToString() {

    Map<String, BigDecimal> balances = new ConcurrentHashMap();
    balances.put("EUR", new BigDecimal(-1000));

    Assert.assertEquals("Ověření, že se vygeneruje string ve správném formátu", "EUR -1000\n", PaymentTracker.balancesToString(balances));
  }

  @Test
  public void test_03_updateBalances() {

    Map<String, BigDecimal> balances = new ConcurrentHashMap();

    // empty transaction
    Transaction transaction = null;
    balances = PaymentTracker.updateBalance(balances, transaction);

    Assert.assertEquals("Počet záznamů v poli", 0, balances.size());
  }

  @Test
  public void test_04_updateBalances() {

    Map<String, BigDecimal> balances = new ConcurrentHashMap();

    // -1000 CZK
    Transaction transaction = new Transaction("CZK", new BigDecimal(-1000));
    balances = PaymentTracker.updateBalance(balances, transaction);

    Assert.assertEquals("1 - Počet záznamů v poli", 1, balances.size());
    Assert.assertEquals("1 - Měna", transaction.getCurrencyCode(), balances.entrySet().iterator().next().getKey());
    Assert.assertEquals("1 - Částka", transaction.getAmount(), balances.entrySet().iterator().next().getValue());

    // +250 CZK => -1000 + 250 = -750
    Transaction transaction2 = new Transaction("CZK", new BigDecimal(250));
    balances = PaymentTracker.updateBalance(balances, transaction2);

    Assert.assertEquals("2 - Počet záznamů v poli", 1, balances.size());
    Assert.assertEquals("2 - Měna", transaction2.getCurrencyCode(), balances.entrySet().iterator().next().getKey());
    Assert.assertEquals("2 - Částka", transaction2.getAmount().add(transaction.getAmount()), balances.entrySet().iterator().next().getValue());

    // +750 CZK => -750 + 750 = 0
    Transaction transaction3 = new Transaction("CZK", new BigDecimal(750));
    balances = PaymentTracker.updateBalance(balances, transaction3);

    Assert.assertEquals("3 - Počet záznamů v poli", 1, balances.size());
    Assert.assertEquals("3 - Měna", transaction3.getCurrencyCode(), balances.entrySet().iterator().next().getKey());
    Assert.assertEquals("3 - Částka", BigDecimal.ZERO, balances.entrySet().iterator().next().getValue());

  }

}
