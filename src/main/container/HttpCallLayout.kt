package main.container

import com.intellij.util.ui.JBDimension
import main.config.ProjectConfig
import main.utils.boldStyle
import java.awt.Color
import java.awt.FlowLayout
import javax.swing.*
import java.awt.Dimension

class HttpCallLayout(val createMethod: Boolean, val isDataTypeVisible: Boolean) : JPanel() {

    var etMethodName: JTextField? = null
    var cbNetworkEnable: JCheckBox? = null
    var rtGetRequest: JRadioButton? = null
    var rtPostRequest: JRadioButton? = null
    var etUrl: JTextField? = null

    var ckJustReturnData: JRadioButton? = null
    var ckToObjectData: JRadioButton? = null
    var ckToListData: JRadioButton? = null
    var groupDataType: ButtonGroup? = null

    var ckLoadingCheck: JCheckBox? = null
    var ckErrorCheck: JCheckBox? = null
    var ckNotStartLce: JCheckBox? = null
    var ckDefaultTokenHead: JCheckBox? = null
    var ckNotShowContentWhenSuccess: JCheckBox? = null
    var ckModifyErrorWarn: JCheckBox? = null

    var ckMethodShow: JCheckBox? = null

    init {
        if (ProjectConfig.isDebug) {
            background = Color.BLUE
        }

        layout = FlowLayout(FlowLayout.LEFT)

        cbNetworkEnable = JCheckBox("网络请求", true)
        cbNetworkEnable?.apply {
            preferredSize = Dimension(LAYOUT_WIDTH, 30)
            boldStyle(15)
        }

        val methodContainer = JPanel()
        methodContainer.apply {
            preferredSize = JBDimension(LAYOUT_WIDTH, 40)
            layout = FlowLayout(FlowLayout.LEFT)

            ckMethodShow = JCheckBox("方法名:", true)
            add(ckMethodShow)

            etMethodName = JTextField()
            add(etMethodName)
            etMethodName!!.preferredSize = JBDimension(200, 30)
        }

        val urlContainer = JPanel()
        urlContainer.apply {
            preferredSize = JBDimension(LAYOUT_WIDTH, 40)
            layout = FlowLayout(FlowLayout.LEFT)
            add(JLabel("Url地址:"))

            etUrl = JTextField()
            if (ProjectConfig.isDebug) {
//                etUrl!!.text = "http://www.baidu.com"
            }

            add(etUrl)
            etUrl!!.preferredSize = JBDimension(200, 30)
        }

        if (createMethod) {
            cbNetworkEnable!!.isVisible = false
        } else {
            methodContainer.isVisible = false
        }

        rtGetRequest = JRadioButton("Get", true)
        rtPostRequest = JRadioButton("Post")
        rtPostRequest?.preferredSize = JBDimension(200, 30)
        val group = ButtonGroup()
        group.add(rtGetRequest)
        group.add(rtPostRequest)

        ckJustReturnData = JRadioButton("只返回请求结果")
        ckToObjectData = JRadioButton("转换成对象", true)
        ckToListData = JRadioButton("转换成列表")
        ckToListData?.preferredSize = JBDimension(LAYOUT_WIDTH, 30)

        ckJustReturnData?.foreground = Color.red
        ckToObjectData?.foreground = Color.red
        ckToListData?.foreground = Color.red

        groupDataType = ButtonGroup()
        groupDataType?.add(ckJustReturnData)
        groupDataType?.add(ckToObjectData)
        groupDataType?.add(ckToListData)

        ckJustReturnData?.isVisible = isDataTypeVisible
        ckToObjectData?.isVisible = isDataTypeVisible
        ckToListData?.isVisible = isDataTypeVisible

        ckDefaultTokenHead = JCheckBox("是否加载默认头")

        val lceWarn = JLabel("下面是LCE模式设置::::::::")
        lceWarn.preferredSize = JBDimension(200, 30)

        ckLoadingCheck = JCheckBox("加载时显示  加载Dialog  (非列表刷新请求时使用)")
        ckErrorCheck = JCheckBox("当加载失败或网络错误时是否显示   错误界面")

        ckNotStartLce = JCheckBox("不启动lce，纯粹的请求网络，用户看不见")
        ckNotShowContentWhenSuccess = JCheckBox("请求成功后，是否立即取消等待，显示界面内容")
        ckModifyErrorWarn = JCheckBox("修改错误时返回的信息(到代码中修改内容)")

        ckDefaultTokenHead?.preferredSize = JBDimension(140, 30)

        fun checkEnabled() {
            val viewCount = componentCount

            for (i in 1 until viewCount) {
                getComponent(i).isEnabled = cbNetworkEnable!!.isSelected
            }
        }

        cbNetworkEnable?.addActionListener {
            checkEnabled()
        }

        add(cbNetworkEnable)
        add(methodContainer)
        add(urlContainer)
        add(rtGetRequest)
        add(rtPostRequest)

        add(ckJustReturnData)
        add(ckToObjectData)
        add(ckToListData)

        add(ckDefaultTokenHead)
        add(lceWarn)
        add(ckNotStartLce)
        add(ckLoadingCheck)
        add(ckErrorCheck)
        add(ckNotShowContentWhenSuccess)
        add(ckModifyErrorWarn)

        checkEnabled()
    }

    companion object {
        const val LAYOUT_WIDTH = 320
    }

}