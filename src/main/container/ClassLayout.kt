package main.container

import com.intellij.util.ui.JBDimension
import main.config.ProjectConfig
import main.utils.FONT_SIZE
import main.utils.boldStyle
import main.utils.toCustomSize
import java.awt.Color
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.Font
import javax.swing.JCheckBox
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField

class ClassLayout : JPanel() {

    var etClassName: JTextField? = null
    var etLayoutName: JTextField? = null
    var ckActivityContainer: JCheckBox? = null
    var newInstanceCB: JCheckBox? = null
    var checkAnim: JCheckBox? = null
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
        etLayoutName?.preferredSize = JBDimension(FONT_SIZE * 11, (FONT_SIZE * 2).toInt())

        if (ProjectConfig.isDebug) {
            etClassName?.text = "UserName"
            etLayoutName?.text = "fragment_user_name"
        }

        ckActivityContainer = JCheckBox("是否创建Activity装载Fragment")

        val newInstanceContainer = JPanel()
        newInstanceContainer.apply {
            preferredSize = JBDimension(LAYOUT_WIDTH, FONT_SIZE * 2)
            layout = FlowLayout(FlowLayout.LEFT)

            newInstanceCB = JCheckBox("是否增加newInstance()方法", true)
            add(newInstanceCB?.toCustomSize())

            pairAdd = JCheckBox("默认传参")
            add(pairAdd?.toCustomSize())
        }

        checkAnim = JCheckBox("是否动画结束后再加载数据")
        val animWarn = JLabel("动画未加载完就加载数据，会引起跳转卡顿")
        animWarn.foreground = Color.red

        add(tvClassName.toCustomSize())
        add(etClassName?.toCustomSize())
        add(tvLayoutName.toCustomSize())
        add(etLayoutName?.toCustomSize())
        add(ckActivityContainer?.toCustomSize())
        add(newInstanceContainer)
        add(checkAnim?.toCustomSize())
        add(animWarn.toCustomSize(FONT_SIZE - 2))
    }

    companion object {
        const val LAYOUT_WIDTH = FONT_SIZE * 22
        const val LAYOUT_HEIGHT = FONT_SIZE * 14
    }

}