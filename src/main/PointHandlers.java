package main;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mr. Robot on 08-Oct-16.
 */
class PointHandlers {

    Map<Point2d, Runnable> handles = new HashMap<>();

    public void addEventHandler (Integer[] pos_a, Runnable handler) {
        Point2d pos = new Point2d(pos_a[1], pos_a[0]);
        handles.put(pos, handler);
    }

    public void handle (Integer[] pos_a) {
        Point2d pos = new Point2d(pos_a[1], pos_a[0]);
        if (!handles.containsKey(pos))
            throw new RuntimeException("This position dosen't exist");
        handles.get(pos).run();
    }
}
