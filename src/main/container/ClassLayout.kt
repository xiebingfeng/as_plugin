package main.container

import main.config.ProjectConfig
import main.utils.FONT_SIZE
import main.utils.boldStyle
import main.utils.toCustomSize
import java.awt.Color
import java.awt.Dimension
import java.awt.FlowLayout
import javax.swing.JCheckBox
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField

class ClassLayout : JPanel() {

    var etClassName: JTextField? = null
    var etLayoutName: JTextField? = null
    var ckActivityContainer: JCheckBox? = null
    var contentVisibleCB: JCheckBox? = null
    var newInstanceCB: JCheckBox? = null
    var pairAdd: JCheckBox? = null

    init {
        if (ProjectConfig.isDebug) {
            background = Color.CYAN
        }

        layout = FlowLayout(FlowLayout.LEFT)

        val tvClassName = JLabel("类名：")
        tvClassName.boldStyle()

        etClassName = JTextField("")

        etClassName?.preferredSize = Dimension(FONT_SIZE * 14, (FONT_SIZE * 2.2).toInt())

        val tvLayoutName = JLabel("布局名：")
        tvLayoutName.boldStyle()

        etLayoutName = JTextField("")
        etLayoutName?.preferredSize = Dimension(FONT_SIZE * 11, (FONT_SIZE * 2).toInt())


        if (ProjectConfig.isDebug) {
            etClassName?.text = "UserName"
            etLayoutName?.text = "fragment_user_name"
        }

        ckActivityContainer = JCheckBox("是否创建Activity装载Fragment")
        contentVisibleCB= JCheckBox("刚进入界面默认隐藏内容布局(防止闪屏)")

        val newInstanceContainer = JPanel()
        newInstanceContainer.apply {
            preferredSize = Dimension(LAYOUT_WIDTH, FONT_SIZE * 4)
            layout = FlowLayout(FlowLayout.LEFT)

            newInstanceCB = JCheckBox("是否增加newInstance()方法", true)
            add(newInstanceCB?.toCustomSize())

            pairAdd = JCheckBox("默认传参")
            add(pairAdd?.toCustomSize())
        }

        add(tvClassName.toCustomSize())
        add(etClassName?.toCustomSize())
        add(tvLayoutName.toCustomSize())
        add(etLayoutName?.toCustomSize())
        add(ckActivityContainer?.toCustomSize())
        add(contentVisibleCB?.toCustomSize())
        add(newInstanceContainer)
    }

    companion object {
        const val LAYOUT_WIDTH = FONT_SIZE * 22
        const val LAYOUT_HEIGHT = FONT_SIZE * 14
    }

}