package eu.greyson.paymentTracker;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PaymentTracker {

  private static Map<String, BigDecimal> balances = new ConcurrentHashMap();

  public static void main (String[] args) {

    // create a scanner so we can read the command-line input
    Scanner scanner = new Scanner(System.in);

    // create thread-safe scheduled task for periodic string output
    ScheduledExecutorService scheduledPool = Executors.newScheduledThreadPool(1);
    scheduledPool.scheduleWithFixedDelay(
      () -> System.out.println(PaymentTracker.balancesToString(balances)),
      1,
      1,
      TimeUnit.MINUTES
    );

    while (true) {

      // get command from command line
      String command = scanner.nextLine();

      // processing quit command
      if (command.equals("quit")) {
        scheduledPool.shutdown();
        break;
      }

      // command string validation
      Pattern pattern = Pattern.compile("([a-zA-Z]{3})(\\s)([+-]??\\d*([.,]+?\\d+?)??)");
      Matcher matcher = pattern.matcher(command);

      // valid command
      if (matcher.matches()) {

        // parse currency and amount
        String currencyCode = matcher.group(1).toUpperCase();
        BigDecimal amount = new BigDecimal(matcher.group(3).replace(",", "."));

        // create money object
        Transaction transaction = new Transaction(currencyCode, amount);

        // update balance
        balances = updateBalance(balances, transaction);
      }
      // invalid command
      else {
        System.out.println("Invalid input!");
      }

      System.out.println("\n" + balancesToString(balances));
    }
  }

  /**
   * Updates balances stored in memory and returns updated map of pairs (key = currency code, value = current balance)
   * @param balances input map with previous state of balances
   * @param transaction money object containing transaction amount and currency code
   * @return output map with current balances
   */
  protected static Map<String, BigDecimal> updateBalance(Map<String, BigDecimal> balances, Transaction transaction) {
    if (transaction != null) {
      BigDecimal newBalance = balances.getOrDefault(transaction.getCurrencyCode(), BigDecimal.ZERO).add(transaction.getAmount());
      balances.put(transaction.getCurrencyCode(), newBalance);
    }
    return balances;
  }

  /**
   * Converts map of balances to string. Currencies with zero balance will not be part of result string.
   * @param balances input map with previous state of balances
   * @return non-zero balances separated by newline
   */
  protected static String balancesToString(Map<String, BigDecimal> balances) {
    StringBuilder result = new StringBuilder();
    for (Map.Entry<String, BigDecimal> entry : balances.entrySet()) {
      if (entry.getValue().compareTo(BigDecimal.ZERO) != 0) {
        result.append(entry.getKey());
        result.append(" ");
        result.append(entry.getValue());
        result.append("\n");
      }
    }
    return result.toString();
  }

}
