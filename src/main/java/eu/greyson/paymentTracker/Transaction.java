package eu.greyson.paymentTracker;

import java.math.BigDecimal;

public class Transaction {

  private String currencyCode;
  private BigDecimal amount;

  Transaction(String currencyCode, BigDecimal amount) {
    this.currencyCode = currencyCode;
    this.amount = amount;
  }

  public String getCurrencyCode() {
    return currencyCode;
  }

  public void setCurrencyCode(String currencyCode) {
    this.currencyCode = currencyCode;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }
}