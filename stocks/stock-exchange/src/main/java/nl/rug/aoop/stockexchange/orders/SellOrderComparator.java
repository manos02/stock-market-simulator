
package nl.rug.aoop.stockexchange.orders;

import java.util.Comparator;

/**
 * A custom comparator for comparing and sorting Sell (RandomOrder) orders.
 */
public class SellOrderComparator implements Comparator<RandomOrder> {

    /**
     * Compares two RandomOrder objects for sorting.
     *
     * @param o1 The first RandomOrder object to compare.
     * @param o2 The second RandomOrder object to compare.
     * @return   A negative value if o1 should be ordered before o2, a positive value if o2 should be ordered before o1,
     *           and 0 if they are considered equal in sorting.
     */
    @Override
    public int compare(RandomOrder o1, RandomOrder o2) {
        if (!o1.isResolved() && o2.isResolved()) {
            return -1;
        } else if (o1.isResolved() && !o2.isResolved()) {
            return 1;
        } else {
            return Double.compare(o2.getPrice(), o1.getPrice());
        }
    }
}
