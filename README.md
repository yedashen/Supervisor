# Supervisor
之前做过两个关于日历的需求。第一个不需要用ViewPager来左右滑动，只支持点击左右箭头来切换，所以我用一个RecyclerView搞定了，点击左右键我切换adpater里面的数据即可。第二个需求是要支持左右滑动来切换月份，我就用（从github里面学的）的是ViewPager实现的，然后每个item显示的是月份。
# 我上面项目描述的第一个效果，就是直接用recyclerView实现的日历效果如下：
 ![](https://github.com/yedashen/Supervisor/blob/master/app/gif/1.gif)
# 第二个效果是可以左右滑动切换月份的。我是点击一个按钮(相当于日历icon)会弹出一个dialog，里面装了一个自定义的viewPager：
 ![](https://github.com/yedashen/Supervisor/blob/master/app/gif/2.gif)
# 然后我项目描述的第二个效果的需求是这样的：界面上有一个可以左右滑动切换上一天下一天的viewpager，没切换一个日期就刷新数据列表（我下面第三个图里面的左右切换的时候有一个textView也跟着在变化，就是相当于切换列表数据了），但是这样只能一天一天的切换。所以又在这个基础上增加了一个日历icon点击弹出日期来可以左右滑动切换月份的去选择，这样更加灵活。点击dialog里面的日期日期之后，关闭dialog之后让之前那个viewpager显示的item的日期显示为当前选中的日期，这个比较麻烦，你可以看我的代码是怎么实现的。
 ![](https://github.com/yedashen/Supervisor/blob/master/app/gif/3.gif)

