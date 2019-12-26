package main.container

import main.config.ProjectConfig
import main.utils.FONT_SIZE
import main.utils.boldStyle
import main.utils.toCustomSize
import java.awt.Color
import java.awt.Dimension
import java.awt.FlowLayout
import javax.swing.*

class ContentLayout : JPanel() {

    var ckClickMethod: JCheckBox? = null
    var ckOverWriteInitViewLce: JCheckBox? = null
    var repeatRequestMethod: JCheckBox? = null
    var normalLayout: JRadioButton? = null
    var listLayout: JRadioButton? = null
    var ckLoadMore: JCheckBox? = null

    var autoRefresh: JCheckBox? = null
    var itemDecoration: JCheckBox? = null
    var dataEmpty: JCheckBox? = null

    init {
        if (ProjectConfig.isDebug) {
            background = Color.YELLOW
        }

        layout = FlowLayout(FlowLayout.LEFT)

        val title = JLabel("布局设置")
        title.boldStyle()
        title.preferredSize = Dimension(LAYOUT_WIDTH, (FONT_SIZE * 2.1).toInt())

        ckClickMethod = JCheckBox("重写点击函数(就是把点击功能放在一个地方集中处理)", true)
        ckOverWriteInitViewLce = JCheckBox("重写Lce初始前的initView，用的不多，详细见注解", false)
        repeatRequestMethod = JCheckBox("每次回到界面，多重新请求数据", false)
        repeatRequestMethod!!.preferredSize = Dimension(LAYOUT_WIDTH, (FONT_SIZE * 2.1).toInt())

        normalLayout = JRadioButton("普通布局", true)
        listLayout = JRadioButton("列表布局")
        listLayout?.preferredSize = Dimension(FONT_SIZE * 14, (FONT_SIZE * 2.2).toInt())

        val group = ButtonGroup()
        group.add(normalLayout?.toCustomSize())
        group.add(listLayout?.toCustomSize())

        ckLoadMore = JCheckBox("列表启动加载更多模式")
        autoRefresh = JCheckBox("列表模式下：初始化后是否自动刷新(默认是自动刷新的)", true)
        itemDecoration = JCheckBox("列表模式下：添加默认分割线(默认是不添加的)")
        dataEmpty = JCheckBox("列表模式下：数据为空时，是否要显示数据为空的提示(默认是)", true)

        fun checkEnabled() {
            repeatRequestMethod!!.isEnabled = !listLayout!!.isSelected

            ckLoadMore!!.isEnabled = listLayout!!.isSelected
            autoRefresh!!.isEnabled = listLayout!!.isSelected
            itemDecoration!!.isEnabled = listLayout!!.isSelected
            dataEmpty!!.isEnabled = listLayout!!.isSelected
        }

        listLayout?.addActionListener {
            checkEnabled()
        }

        normalLayout?.addActionListener {
            checkEnabled()
        }

        add(title.toCustomSize())
        add(ckClickMethod?.toCustomSize())
        add(ckOverWriteInitViewLce?.toCustomSize())
        add(normalLayout?.toCustomSize())
        add(listLayout?.toCustomSize())
        add(repeatRequestMethod?.toCustomSize())
        add(ckLoadMore?.toCustomSize())
        add(autoRefresh?.toCustomSize())
        add(itemDecoration?.toCustomSize())
        add(dataEmpty?.toCustomSize())

        checkEnabled()
    }

    companion object {
        const val LAYOUT_WIDTH = FONT_SIZE * 30
    }

}