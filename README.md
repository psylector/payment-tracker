# payment-tracker

Payment tracker is simple console Java application for simple payment processing. 
It stores current balances of each currency in memory and prints periodically current balances. 

Main features:
- insert transaction in random currency and update current balance by transaction currency code
- periodical print current balances once a minute

### Installation

Requirements:
- Java 8
- maven

Run application:

```sh
mvn package
java -jar target/payment-tracker-1.0-SNAPSHOT.jar
```

### Commands
```sh
# process transactions by following section "Usage"
XXX +100
XXX -99.99
XXX -0,01

# end of application
quit
```

### Usage

Transaction processing cause updates of current balances.

Sample input:
```sh
USD 1000
HKD 100
USD -100
RMB 2000
HKD 200
```

Sample output:
```sh
USD 900
RMB 2000
HKD 300
```

### Bonus features
- support of decimal numbers
- support of decimal separators "," and "."

Sample input:
```sh
USD 1000.15
HKD -99,99
```

Sample input:
```sh
USD 1000,15
HKD -99,99
```