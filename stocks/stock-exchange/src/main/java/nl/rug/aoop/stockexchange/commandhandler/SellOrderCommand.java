package nl.rug.aoop.stockexchange.commandhandler;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.command.Command;
import nl.rug.aoop.stockexchange.orders.BuyOrderComparator;
import nl.rug.aoop.stockexchange.orders.RandomOrder;
import nl.rug.aoop.stockexchange.model.StockMarket;
import nl.rug.aoop.stockexchange.orders.SellOrderComparator;
import nl.rug.aoop.stockexchange.stocks.Stock;
import java.util.List;
import java.util.Map;

/**
 * Represents a command to handle a "sell" order in the stock market.
 */
@Getter
@Slf4j
public class SellOrderCommand implements Command {

    private final StockMarket stockMarket;
    private RandomOrder sellOrder;
    private final BuyOrderComparator buyOrderComparator;
    private final SellOrderComparator sellOrderComparator;

    /**
     * Constructs a SellOrderCommand with the given StockMarket.
     *
     * @param stockMarket The StockMarket in which the sell order is executed.
     */
    public SellOrderCommand(StockMarket stockMarket) {
        this.stockMarket = stockMarket;
        this.buyOrderComparator = new BuyOrderComparator();
        this.sellOrderComparator = new SellOrderComparator();
    }

    /**
     * Executes the "sell" order command.
     *
     * @param params A map of parameters containing the order details in the "body" field.
     */
    @Override
    public void executeCommand(Map<String, Object> params) {
        String body = (String) params.get("body");
        sellOrder = RandomOrder.convertToOrder(body);
        stockMarket.addOrder(sellOrder, stockMarket.getSellOrders());
        RandomOrder matchedOrder = null;

        if (stockMarket.getBuyOrders().containsKey(sellOrder.getStockName())) {
            List<RandomOrder> buyOrders = stockMarket.getBuyOrders().get(sellOrder.getStockName());

            if (!buyOrders.isEmpty() && !stockMarket.getBuyOrders().get(sellOrder.getStockName()).get(0).isResolved()) {
                matchedOrder = stockMarket.getBuyOrders().get(sellOrder.getStockName()).get(0);
            }

        }

        resolveMatchedOrder(matchedOrder);
        stockMarket.getSellOrders().get(sellOrder.getStockName()).sort(sellOrderComparator);
    }

    /**
     * Resolves a matched order, if one exists.
     *
     * @param matchedOrder The matched order to be resolved.
     */
    public void resolveMatchedOrder(RandomOrder matchedOrder) {
        if (matchedOrder != null) {
            resolveSellOrder(matchedOrder);
        }
    }

    /**
     * Resolves a sell order based on the matched order.
     *
     * @param matchedOrder The matched order for resolution.
     */
    public void resolveSellOrder(RandomOrder matchedOrder) {
        if (matchedOrder.getQuantity() > sellOrder.getQuantity()) {
            sellOrder.setResolved(true);
            matchedOrder.modifyOrder(sellOrder.getQuantity());
            stockMarket.handleTransaction(sellOrder);
        } else if (matchedOrder.getQuantity() < sellOrder.getQuantity()) {
            sellOrder.modifyOrder(matchedOrder.getQuantity());
            matchedOrder.setResolved(true);
            List<RandomOrder> orders = stockMarket.getBuyOrders().get(sellOrder.getStockName());
            orders.sort(buyOrderComparator);
            stockMarket.handleTransaction(matchedOrder);
        } else {
            sellOrder.setResolved(true);
            matchedOrder.setResolved(true);
            List<RandomOrder> orders = stockMarket.getBuyOrders().get(sellOrder.getStockName());
            orders.sort(buyOrderComparator);
            stockMarket.handleTransaction(matchedOrder);
            stockMarket.handleTransaction(sellOrder);
        }
        Stock findStock = stockMarket.findStock(matchedOrder.getStockName());
        stockMarket.updateStockPrice(findStock, matchedOrder.getPrice());
    }
}
