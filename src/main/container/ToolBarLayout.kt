package main.container

import com.intellij.util.ui.JBDimension
import main.config.ProjectConfig
import main.utils.boldStyle
import java.awt.Color
import java.awt.Dimension
import java.awt.FlowLayout
import javax.swing.*

class ToolBarLayout : JPanel() {

    var normalTitleLayout: JRadioButton? = null
    var noneTitleLayout: JRadioButton? = null

    var etTitleName: JTextField? = null
    var ckBack: JCheckBox? = null
    var ckToolBarBackGround: JCheckBox? = null
    var ckMainBackGround: JCheckBox? = null
    var ckBottomLine: JCheckBox? = null
    var ckEventBus: JCheckBox? = null

    init {
        if (ProjectConfig.isDebug) {
            background = Color.YELLOW
        }

        layout = FlowLayout(FlowLayout.LEFT)

        initTop(this)
        initRightView(this)
    }

    private fun initTop(container: JPanel) {
        val title = JLabel("标题栏设置")
        title.boldStyle()
        title.preferredSize = Dimension(LAYOUT_WIDTH, 30)

        normalTitleLayout = JRadioButton("普通", true)
        noneTitleLayout = JRadioButton("无标题栏")
        noneTitleLayout?.preferredSize = Dimension(LAYOUT_WIDTH / 3 * 2, 30)

        val group = ButtonGroup()
        group.add(normalTitleLayout)
        group.add(noneTitleLayout)

        val tvTitleName = JLabel("标题名：")

        etTitleName = JTextField("")
        etTitleName?.preferredSize = JBDimension(200, 30)

        ckBack = JCheckBox("默认后退功能")
        ckToolBarBackGround = JCheckBox("默认标题栏背景色")
        ckMainBackGround = JCheckBox("默认主背景色")
        ckMainBackGround?.preferredSize = JBDimension(200, 30)

        ckBottomLine = JCheckBox("标题栏底下显示默认横线")
        ckEventBus = JCheckBox("自动检测 注册和销毁EventBus(默认是的)", true)

        fun disableAll() {
            tvTitleName.isEnabled = false
            etTitleName?.isEnabled = false
            ckBack?.isEnabled = false
            ckToolBarBackGround?.isEnabled = false
        }

        fun enableAll() {
            tvTitleName.isEnabled = true
            etTitleName?.isEnabled = true
            ckBack?.isEnabled = true
            ckToolBarBackGround?.isEnabled = true
        }

        normalTitleLayout?.addChangeListener {
            if (normalTitleLayout!!.isSelected) {
                enableAll()
            }
        }

        noneTitleLayout?.addChangeListener {
            if (noneTitleLayout!!.isSelected) {
                disableAll()
            }
        }

        container.add(title)
        container.add(normalTitleLayout)
        container.add(noneTitleLayout)
        container.add(tvTitleName)
        container.add(etTitleName)
        container.add(ckBack)
        container.add(ckToolBarBackGround)
        container.add(ckBottomLine)
        container.add(ckMainBackGround)
        container.add(ckEventBus)
    }

    var titleRight: JCheckBox? = null
    var rbRightViewText: JRadioButton? = null
    var rbRightViewImage: JRadioButton? = null

    var rightColorWhite: JRadioButton? = null
    var rightColorBlack: JRadioButton? = null
    var rightColorCustom: JRadioButton? = null
    var rightViewTitle: JTextField? = null

    private fun initRightView(container: JPanel) {
        titleRight = JCheckBox("右边按钮设置--只有在 普通  模式下才有用")
        titleRight!!.boldStyle(13)
        titleRight?.preferredSize = Dimension(LAYOUT_WIDTH, 30)

        rbRightViewText = JRadioButton("文字", true)
        rbRightViewImage = JRadioButton("图片按钮")
        rbRightViewImage?.preferredSize = JBDimension(200, 30)
        val groupRight = ButtonGroup()
        groupRight.add(rbRightViewText)
        groupRight.add(rbRightViewImage)


        rightColorWhite = JRadioButton("白色", true)
        rightColorBlack = JRadioButton("黑色", true)
        rightColorCustom = JRadioButton("默认", true)

        val rightColorGroup = ButtonGroup()
        rightColorGroup.add(rightColorWhite)
        rightColorGroup.add(rightColorBlack)
        rightColorGroup.add(rightColorCustom)


        rightViewTitle = JTextField("新增")
        rightViewTitle?.preferredSize = JBDimension(100, 30)

        fun enableAll() {
            rbRightViewText?.isEnabled = true
            rbRightViewImage?.isEnabled = true
            rightColorWhite?.isEnabled = true
            rightColorBlack?.isEnabled = true
            rightColorCustom?.isEnabled = true
            rightViewTitle?.isEnabled = true
        }

        fun disableAll() {
            rbRightViewText?.isEnabled = false
            rbRightViewImage?.isEnabled = false
            rightColorWhite?.isEnabled = false
            rightColorBlack?.isEnabled = false
            rightColorCustom?.isEnabled = false
            rightViewTitle?.isEnabled = false
        }

        titleRight?.addItemListener {
            if (titleRight!!.isSelected) {
                enableAll()
            } else {
                disableAll()
            }
        }
        disableAll()

        container.add(titleRight)
        container.add(rbRightViewText)
        container.add(rbRightViewImage)
        container.add(rightColorWhite)
        container.add(rightColorBlack)
        container.add(rightColorCustom)
        container.add(rightViewTitle)
    }

    companion object {
        const val LAYOUT_WIDTH = 300
    }

}