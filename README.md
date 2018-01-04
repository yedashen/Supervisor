# Supervisor
之前做过两个关于日历的需求。第一个不需要用ViewPager来左右滑动，只支持点击左右箭头来切换，所以我用一个RecyclerView搞定了，点击左右键我切换adpater里面的数据即可。第二个需求是要支持左右滑动来切换月份，我就用（从github里面学的）的是ViewPager实现的，然后每个item显示的是月份。
# 我上面项目描述的第一个效果，就是直接用recyclerView实现的日历效果如下：
 ![](https://github.com/yedashen/Supervisor/blob/master/app/gif/1.gif)

