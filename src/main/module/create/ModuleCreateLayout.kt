package main.module.create

import main.config.ProjectConfig
import main.utils.boldStyle
import java.awt.Color
import java.awt.Dimension
import java.awt.FlowLayout
import javax.swing.*

class ModuleCreateLayout : JPanel() {

    var etModuleName: JTextField? = null
    var etPreFix: JTextField? = null
    var etRemark: JTextField? = null
    var etPackageName: JTextField? = null

    var rtModuleNormal: JRadioButton? = null
    var rtModuleBusiness: JRadioButton? = null
    var rtModuleLibs: JRadioButton? = null

    init {
        if (ProjectConfig.isDebug) {
            background = Color.CYAN
        }
        layout = FlowLayout(FlowLayout.LEFT)

        val tvModuleName = JLabel("工程名(首字母小写)：")
        tvModuleName.boldStyle()
        etModuleName = JTextField("")
        etModuleName?.preferredSize = Dimension(100, 30)

        if (ProjectConfig.isDebug) {
            etModuleName?.text = "good"
        }

        val tvPreFixName = JLabel("resourcePrefix(别加下划线)：")
        tvPreFixName.boldStyle()

        etPreFix = JTextField()
        if (ProjectConfig.isDebug) {
            etPreFix!!.text = "good"
        }
        etPreFix?.preferredSize = Dimension(100, 30)

        val tvModuleRemarkName = JLabel("模块注解：")
        tvModuleRemarkName.boldStyle()

        val tvPackageDes = JLabel("公司项目差别包名： ")
        val tvCom = JLabel("com.")
        etPackageName = JTextField()
        etPackageName!!.text = "xbf"
        etPackageName?.preferredSize = Dimension(50, 30)
        val tvComEnd = JLabel(".xxx")


        etRemark = JTextField()
        if (ProjectConfig.isDebug) {
            etRemark!!.text = "好好啊"
        }
        etRemark?.preferredSize = Dimension(120, 30)

        rtModuleNormal = JRadioButton("普通")
        rtModuleBusiness = JRadioButton("业务模块", true)
        rtModuleLibs = JRadioButton("库模块")

        val moduleRBContainer = JPanel()
        moduleRBContainer.add(rtModuleNormal)
        moduleRBContainer.add(rtModuleBusiness)
        moduleRBContainer.add(rtModuleLibs)

        val group = ButtonGroup()
        group.add(rtModuleNormal)
        group.add(rtModuleBusiness)
        group.add(rtModuleLibs)

        add(tvModuleName)
        add(etModuleName)

        add(tvPreFixName)
        add(etPreFix)

        add(tvModuleRemarkName)
        add(etRemark)

        add(tvPackageDes)
        add(tvCom)
        add(etPackageName)
        add(tvComEnd)

        add(moduleRBContainer)
    }

    companion object {
        const val LAYOUT_WIDTH = 320
    }

}