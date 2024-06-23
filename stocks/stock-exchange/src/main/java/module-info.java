
module stock.exchange {
    requires networking;
    requires messagequeue;
    requires command;
    requires org.slf4j;
    requires org.mockito;
    requires static lombok;
    requires com.google.gson;
    requires util;
    requires stock.market.ui;
    requires com.fasterxml.jackson.databind;
    opens nl.rug.aoop.stockexchange.orders to com.google.gson;
    exports nl.rug.aoop.stockexchange.stocks;
    exports nl.rug.aoop.stockexchange.orders;
    exports nl.rug.aoop.stockexchange.model;
    exports nl.rug.aoop.stockexchange.traders;
}