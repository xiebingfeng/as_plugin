package main.container

import com.intellij.util.ui.JBDimension
import main.config.ProjectConfig
import main.utils.boldStyle
import java.awt.Color
import java.awt.FlowLayout
import javax.swing.*

class ContentLayout : JPanel() {

    var ckClickMethod: JCheckBox? = null
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
        title.preferredSize = JBDimension(LAYOUT_WIDTH, 30)

        ckClickMethod = JCheckBox("重写点击函数(就是把点击功能放在一个地方集中处理)", true)


        normalLayout = JRadioButton("普通布局", true)
        listLayout = JRadioButton("列表布局")
        listLayout?.preferredSize = JBDimension(200, 30)

        val group = ButtonGroup()
        group.add(normalLayout)
        group.add(listLayout)

        ckLoadMore = JCheckBox("列表启动加载更多模式")
        autoRefresh = JCheckBox("列表模式下：初始化后是否自动刷新(默认是自动刷新的)", true)
        itemDecoration = JCheckBox("列表模式下：添加默认分割线(默认是不添加的)")
        dataEmpty = JCheckBox("列表模式下：数据为空时，是否要显示数据为空的提示(默认是)", true)

        fun checkEnabled() {
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

        add(title)
        add(ckClickMethod)
        add(normalLayout)
        add(listLayout)
        add(ckLoadMore)
        add(autoRefresh)
        add(itemDecoration)
        add(dataEmpty)

        checkEnabled()
    }

    companion object {
        const val LAYOUT_WIDTH = 400
    }

}