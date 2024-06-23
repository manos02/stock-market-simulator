package nl.rug.aoop.stockexchange.orders;

import java.util.Comparator;

/**
 * Comparator for sorting buy orders based on their price and resolved status.
 */
public class BuyOrderComparator implements Comparator<RandomOrder> {

    /**
     * Compares two buy orders for sorting.
     *
     * @param o1 The first buy order.
     * @param o2 The second buy order.
     * @return -1 if the first order should be placed before the second, 1 if the second order
     * should be placed before the first, or 0 if they have the same priority.
     *
     */
    @Override
    public int compare(RandomOrder o1, RandomOrder o2) {
        if (!o1.isResolved() && o2.isResolved()) {
            return -1;
        } else if (o1.isResolved() && !o2.isResolved()) {
            return 1;
        } else {
            return Double.compare(o1.getPrice(), o2.getPrice());
        }
    }
}
