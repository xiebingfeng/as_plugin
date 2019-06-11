package main.container

import com.intellij.util.ui.JBDimension
import main.config.ProjectConfig
import main.utils.FONT_SIZE
import main.utils.boldStyle
import main.utils.toCustomSize
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
        title.preferredSize = Dimension(LAYOUT_WIDTH, (FONT_SIZE * 2.1).toInt())

        normalTitleLayout = JRadioButton("普通", true)
        noneTitleLayout = JRadioButton("无标题栏")
        noneTitleLayout?.preferredSize = Dimension(LAYOUT_WIDTH / 3 * 2, (FONT_SIZE * 2.1).toInt())

        val group = ButtonGroup()
        group.add(normalTitleLayout?.toCustomSize())
        group.add(noneTitleLayout?.toCustomSize())

        val tvTitleName = JLabel("标题名：")

        etTitleName = JTextField("")
        etTitleName?.preferredSize = JBDimension(FONT_SIZE * 12, (FONT_SIZE * 2.1).toInt())

        ckBack = JCheckBox("默认后退功能")
        ckToolBarBackGround = JCheckBox("默认标题栏背景色")
        ckMainBackGround = JCheckBox("默认主背景色")
        ckMainBackGround?.preferredSize = JBDimension(FONT_SIZE * 14, (FONT_SIZE * 2.1).toInt())

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

        container.add(title.toCustomSize())
        container.add(normalTitleLayout?.toCustomSize())
        container.add(noneTitleLayout?.toCustomSize())
        container.add(tvTitleName.toCustomSize())
        container.add(etTitleName?.toCustomSize())
        container.add(ckBack?.toCustomSize())
        container.add(ckToolBarBackGround?.toCustomSize())
        container.add(ckBottomLine?.toCustomSize())
        container.add(ckMainBackGround?.toCustomSize())
        container.add(ckEventBus?.toCustomSize())
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
        titleRight!!.boldStyle(FONT_SIZE - 1)
        titleRight?.preferredSize = Dimension(LAYOUT_WIDTH, (FONT_SIZE * 2.1).toInt())

        rbRightViewText = JRadioButton("文字", true)
        rbRightViewImage = JRadioButton("图片按钮")
        rbRightViewImage?.preferredSize = JBDimension(FONT_SIZE * 14, (FONT_SIZE * 2.1).toInt())
        val groupRight = ButtonGroup()
        groupRight.add(rbRightViewText?.toCustomSize())
        groupRight.add(rbRightViewImage?.toCustomSize())


        rightColorWhite = JRadioButton("白色", true)
        rightColorBlack = JRadioButton("黑色", true)
        rightColorCustom = JRadioButton("默认", true)

        val rightColorGroup = ButtonGroup()
        rightColorGroup.add(rightColorWhite?.toCustomSize())
        rightColorGroup.add(rightColorBlack?.toCustomSize())
        rightColorGroup.add(rightColorCustom?.toCustomSize())


        rightViewTitle = JTextField("新增")
        rightViewTitle?.preferredSize = JBDimension((FONT_SIZE * 7.1).toInt(), (FONT_SIZE * 2.1).toInt())

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

        container.add(titleRight?.toCustomSize())
        container.add(rbRightViewText?.toCustomSize())
        container.add(rbRightViewImage?.toCustomSize())
        container.add(rightColorWhite?.toCustomSize())
        container.add(rightColorBlack?.toCustomSize())
        container.add(rightColorCustom?.toCustomSize())
        container.add(rightViewTitle?.toCustomSize())
    }

    companion object {
        const val LAYOUT_WIDTH = FONT_SIZE * 22
        const val LAYOUT_HEIGHT = FONT_SIZE * 28
    }

}