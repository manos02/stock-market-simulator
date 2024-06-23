module trading {
    requires stock.exchange;
    requires lombok;
    requires messagequeue;
    requires networking;
    requires com.fasterxml.jackson.databind;
    requires command;
    requires awaitility;
    requires org.slf4j;
    requires stock.market.ui;
}