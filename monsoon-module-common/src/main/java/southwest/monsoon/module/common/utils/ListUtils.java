package southwest.monsoon.module.common.utils;

import lombok.experimental.UtilityClass;
import southwest.monsoon.module.common.web.result.exception.WebException;
import southwest.monsoon.module.common.web.result.msg.SimpleMsg;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @version 7/2/2021
 */
@UtilityClass
public class ListUtils {
    public static <T> List<List<T>> splitList(List<T> list, int splitSize) {
        if (splitSize < 1) {
            throw WebException.err(SimpleMsg.text("'splitSize' can't lower than 1"));
        }
        List<List<T>> returnList = new ArrayList<>();
        if (list != null && list.size() > 0) {
            int size = (list.size() + splitSize - 1) / splitSize;
            returnList = new ArrayList(size);
            for (int i = 0; i < size; i++) {
                int endIndex = (i + 1) * splitSize;
                if (endIndex > list.size() - 1) {
                    endIndex = list.size();
                }
                List subList = list.subList(i * splitSize, endIndex);
                returnList.add(subList);
            }
        }
        return returnList;
    }

    public static <T> int segmentOperation100(List<T> list, Function<List<T>, Integer> function) {
        return segmentOperation(list, function, 100);
    }

    public static <T> int segmentOperation(List<T> list, Function<List<T>, Integer> function, int batchSize) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        if (batchSize <= 0) {
            throw WebException.err(SimpleMsg.text("batchSize must be greater than 0"));
        }
        if (batchSize > list.size()) {
            return function.apply(list);
        }
        List<T> container = new ArrayList<>(batchSize);
        int num = 0;
        for (T t : list) {
            container.add(t);
            if (container.size() >= batchSize) {
                num += function.apply(container);
                container.clear();
            }
        }
        if (!container.isEmpty()) {
            num += function.apply(container);
        }
        return num;
    }
}
