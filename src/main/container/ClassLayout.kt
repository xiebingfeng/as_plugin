package main.container

import com.intellij.util.ui.JBDimension
import main.config.ProjectConfig
import main.utils.boldStyle
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

        etClassName?.preferredSize = Dimension(200, 30)

        val tvLayoutName = JLabel("布局名：")
        tvLayoutName.boldStyle()

        tvLayoutName.setBounds(0, 40, 100, 40)
        etLayoutName = JTextField("")
        etLayoutName?.preferredSize = JBDimension(200, 30)

        if (ProjectConfig.isDebug) {
            etClassName?.text = "UserName"
            etLayoutName?.text = "fragment_user_name"
        }

        ckActivityContainer = JCheckBox("是否创建Activity装载Fragment")

        val newInstanceContainer = JPanel()
        newInstanceContainer.apply {
            preferredSize = JBDimension(LAYOUT_WIDTH, 28)
            layout = FlowLayout(FlowLayout.LEFT)

            newInstanceCB = JCheckBox("是否增加newInstance()方法", true)
            add(newInstanceCB)

            pairAdd = JCheckBox("默认传参")
            add(pairAdd)
        }

        checkAnim = JCheckBox("是否动画结束后再加载数据")
        val animWarn = JLabel("动画未加载完就加载数据，会引起跳转卡顿")
        animWarn.foreground = Color.red
        animWarn.font = Font("宋体", Font.PLAIN, 12)

        add(tvClassName)
        add(etClassName)
        add(tvLayoutName)
        add(etLayoutName)
        add(ckActivityContainer)
        add(newInstanceContainer)
        add(checkAnim)
        add(animWarn)
    }

    companion object {
        const val LAYOUT_WIDTH = 300
    }

}