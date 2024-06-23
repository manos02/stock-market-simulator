package nl.rug.aoop.stockexchange.commandhandler;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.command.Command;
import nl.rug.aoop.stockexchange.orders.BuyOrderComparator;
import nl.rug.aoop.stockexchange.orders.RandomOrder;
import nl.rug.aoop.stockexchange.model.StockMarket;
import nl.rug.aoop.stockexchange.orders.SellOrderComparator;
import nl.rug.aoop.stockexchange.stocks.Stock;
import java.util.*;


/**
 * Represents a command to handle a "buy" order in the stock market.
 */

@Getter
@Slf4j
public class BuyOrderCommand implements Command {
    private final StockMarket stockMarket;
    private final BuyOrderComparator buyOrderComparator;
    private final SellOrderComparator sellOrderComparator;
    private RandomOrder buyOrder;

    /**
     * Constructs a BuyOrderCommand with the given StockMarket.
     *
     * @param stockMarket The StockMarket in which the buy order is executed.
     */
    public BuyOrderCommand(StockMarket stockMarket) {
        this.stockMarket = stockMarket;
        this.buyOrderComparator = new BuyOrderComparator();
        this.sellOrderComparator = new SellOrderComparator();
    }

    /**
     * Executes the "buy" order command.
     *
     * @param params A map of parameters containing the order details in the "body" field.
     */
    @Override
    public void executeCommand(Map<String, Object> params) {
        String body = (String) params.get("body");
        buyOrder =  RandomOrder.convertToOrder(body);
        stockMarket.addOrder(buyOrder, stockMarket.getBuyOrders());
        RandomOrder matchedOrder = null;

        if (stockMarket.getSellOrders().containsKey(buyOrder.getStockName())) {
            List<RandomOrder> sellOrders = stockMarket.getSellOrders().get(buyOrder.getStockName());

            if (!sellOrders.isEmpty() && !sellOrders.get(0).isResolved()) {
                matchedOrder = sellOrders.get(0);
            }

        }

        resolveMatchedOrder(matchedOrder);
        stockMarket.getBuyOrders().get(buyOrder.getStockName()).sort(buyOrderComparator);
    }

    /**
     * Resolves a matched order, if one exists.
     *
     * @param matchedOrder The matched order to be resolved.
     */
    public void resolveMatchedOrder(RandomOrder matchedOrder) {
        if (matchedOrder != null) {
            resolveBuyOrder(matchedOrder);
        }
    }

    /**
     * Resolves a buy order based on the matched order.
     *
     * @param matchedOrder The matched order for resolution.
     */
    public void resolveBuyOrder(RandomOrder matchedOrder) {
        if (matchedOrder.getQuantity() > buyOrder.getQuantity()) {
            buyOrder.setResolved(true);
            matchedOrder.modifyOrder(buyOrder.getQuantity());
            stockMarket.handleTransaction(buyOrder);
        } else if (matchedOrder.getQuantity() < buyOrder.getQuantity()) {
            buyOrder.modifyOrder(matchedOrder.getQuantity());
            matchedOrder.setResolved(true);
            List<RandomOrder> orders = stockMarket.getSellOrders().get(buyOrder.getStockName());
            orders.sort(sellOrderComparator);
            stockMarket.handleTransaction(matchedOrder);
        } else {
            buyOrder.setResolved(true);
            matchedOrder.setResolved(true);
            List<RandomOrder> orders = stockMarket.getSellOrders().get(buyOrder.getStockName());
            orders.sort(sellOrderComparator);
            stockMarket.handleTransaction(matchedOrder);
            stockMarket.handleTransaction(buyOrder);
        }

        Stock findStock = stockMarket.findStock(matchedOrder.getStockName());
        stockMarket.updateStockPrice(findStock, matchedOrder.getPrice());
    }
}
