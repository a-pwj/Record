package com.pwj.record.utils

import androidx.core.widget.NestedScrollView

/**
 * @Author:          pwj
 * @Date:            2020/5/18 13:00
 * @FileName:        ScrollViewUtils
 * @Description:     控制nestedScrollView滑动到一定的距离
 */
class ScrollViewUtils {

    private var nestedScrollViewTop = 0

    /**
     * 控制nestedScrollView滑动到一定的距离
     *
     * 使用方法：
     *   int[] intArray=new int[2];
     *   view.getLocationOnScreen(intArray);//测量某View相对于屏幕的距离
     *   MyActivity activity =(MyActivity) getActivity();
     *   activity.scrollByDistance(intArray[1]);
     */
    fun scrollByDistance(scrollView: NestedScrollView, dy: Int) {
        if (nestedScrollViewTop == 0) {
            val intArray = IntArray(2)
            scrollView.getLocationOnScreen(intArray)
            nestedScrollViewTop = intArray[1]
        }
        val distance = dy - nestedScrollViewTop //必须算上nestedScrollView本身与屏幕的距离
        scrollView.fling(distance) //添加上这句滑动才有效
        scrollView.smoothScrollBy(0, distance)
    }

}