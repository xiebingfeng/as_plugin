package main.action

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import main.config.ProjectConfig
import main.container.ClassLayout
import main.container.ContentLayout
import main.container.HttpCallLayout
import main.container.ToolBarLayout
import main.create.ActivityCreateUtils
import main.create.FragmentCreateUtils
import main.create.ViewModelCreateUtils
import main.executeCouldRollBackAction
import main.utils.FONT_SIZE
import main.utils.showCommonDialog
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.io.File

import javax.swing.*
import javax.swing.JButton


class MVVMAction : AnAction() {
    private var project: Project? = null

    private var mClassName = ""
    private var mLayoutName = ""

    private var mClassLayout: ClassLayout? = null
    private var mToolBarLayout: ToolBarLayout? = null
    private var mContentLayout: ContentLayout? = null
    private var mHttpCallContent: HttpCallLayout? = null

    private var mDirectLoadUtils: DirectLoadUtils? = null

    init {
        mDirectLoadUtils = DirectLoadUtils()
    }

    override fun actionPerformed(e: AnActionEvent) {
        project = e.project
        mDirectLoadUtils = DirectLoadUtils()

        if (!ProjectConfig.isDebug) {
            mDirectLoadUtils?.load(e)
        }
        mDirectLoadUtils?.projectFilePath = project?.projectFile?.parent?.parent?.path.toString()

        if (mDirectLoadUtils?.srcCurrentDir == null) {
            showCommonDialog("请选择正常包路径")
        } else {
            showDialog()
        }
    }

    private fun showDialog() {
        val jDialog = JDialog()
        jDialog.title = "XBF"
        jDialog.setSize(ClassLayout.LAYOUT_WIDTH + ContentLayout.LAYOUT_WIDTH + HttpCallLayout.LAYOUT_WIDTH, ClassLayout.LAYOUT_HEIGHT + ToolBarLayout.LAYOUT_HEIGHT+50)
        jDialog.setLocation(FONT_SIZE * 14, FONT_SIZE * 14)
        jDialog.layout = null

        mClassLayout = ClassLayout()
        mClassLayout?.setBounds(0, 0, ClassLayout.LAYOUT_WIDTH, ClassLayout.LAYOUT_HEIGHT)

        mToolBarLayout = ToolBarLayout()
        mToolBarLayout?.setBounds(0, ClassLayout.LAYOUT_HEIGHT, ToolBarLayout.LAYOUT_WIDTH, ToolBarLayout.LAYOUT_HEIGHT)

        mContentLayout = ContentLayout()
        mContentLayout?.setBounds(ClassLayout.LAYOUT_WIDTH, 0, ContentLayout.LAYOUT_WIDTH, FONT_SIZE * 20)

        mHttpCallContent = HttpCallLayout(false, false)
        mHttpCallContent?.setBounds(ClassLayout.LAYOUT_WIDTH + ContentLayout.LAYOUT_WIDTH, 0, HttpCallLayout.LAYOUT_WIDTH, FONT_SIZE * 28)

        jDialog.add(mClassLayout)
        jDialog.add(mToolBarLayout)
        jDialog.add(mContentLayout)
        jDialog.add(mHttpCallContent)

        initBtnViews(jDialog)
        jDialog.isVisible = true
    }

    private fun initBtnViews(jdDialog: JDialog) {
        val btnSpannedFile = JButton("生成文件")
        btnSpannedFile.setBounds(FONT_SIZE * 60, FONT_SIZE * 37, FONT_SIZE * 9, (FONT_SIZE * 3).toInt())
        btnSpannedFile.addActionListener {
            mClassName = mClassLayout?.etClassName!!.text
            if (mClassName.isEmpty()) {
                showCommonDialog("请输入类名")
                return@addActionListener
            }

            mLayoutName = mClassLayout?.etLayoutName!!.text
            if (mLayoutName.isEmpty()) {
                showCommonDialog("请输入布局名")
                return@addActionListener
            }

            ProjectConfig.isNormalLayout = mContentLayout?.normalLayout!!.isSelected

            if (!checkFileExits()) {
                return@addActionListener
            }

            if (ProjectConfig.isDebug) {
                createFile(jdDialog)
            } else {
                executeCouldRollBackAction(project) {
                    PropertiesComponent.getInstance().setValue(ENABLE_AUTO_REFORMAT, true, true)
                    createFile(jdDialog)
                }
            }
        }

        val btnCancel = JButton("取消")
        btnCancel.setBounds(FONT_SIZE * 50, FONT_SIZE * 37, FONT_SIZE * 9, FONT_SIZE * 3)
        btnCancel.addActionListener {
            jdDialog.isVisible = false
        }
        jdDialog.add(btnCancel)
        jdDialog.add(btnSpannedFile)

        jdDialog.addWindowListener(object : WindowAdapter() {
            override fun windowClosing(e: WindowEvent?) {
                mDirectLoadUtils = null
                project = null
            }
        })
    }

    private fun checkFileExits(): Boolean {
        val layoutFile = mDirectLoadUtils?.projectFilePath + "/src/main/res/layout/" + "$mLayoutName.xml"

        if (File(layoutFile).exists()) {
            showCommonDialog("布局文件已存在")
            return false
        }

        if (mDirectLoadUtils?.directory?.findFile(mClassName + "Fragment.kt") != null) {
            showCommonDialog("Fragment文件已存在")
            return false
        }

        if (mDirectLoadUtils?.directory?.findFile(mClassName + "ViewModel.kt") != null) {
            showCommonDialog("ViewModel文件已存在")
            return false
        }

        val adapterDir = mDirectLoadUtils?.directory?.findSubdirectory("adapter")
        if (adapterDir != null) {
            if (adapterDir.findFile(mClassName + "Adapter.kt") != null) {
                showCommonDialog("Adapter文件已存在")
                return false
            }
        }

        return true
    }

    private fun createFile(jdDialog: JDialog) {
        FragmentCreateUtils(project, mDirectLoadUtils!!).create(mClassName, this, mLayoutName, mClassLayout!!,
                mToolBarLayout!!, mContentLayout!!, mHttpCallContent!!)
        ViewModelCreateUtils(mDirectLoadUtils!!, project).create(mClassName, this, mHttpCallContent!!, mContentLayout!!)
        ActivityCreateUtils(project, mDirectLoadUtils!!).create(mClassName, this, mClassLayout!!)
        jdDialog.isVisible = false
    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val a = MVVMAction()
            ProjectConfig.isDebug = true
            a.showDialog()
        }

        const val ENABLE_AUTO_REFORMAT = "json-to-kotlin-class-enable-auto-reformat"
    }
}
