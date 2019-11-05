package main.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import main.config.ProjectConfig
import main.container.HttpCallLayout
import main.create.HttpCoreUtils
import main.executeAfterTime
import main.executeCouldRollBackAction
import main.utils.FONT_SIZE
import main.utils.FileFormatUtils
import main.utils.showCommonDialog

import javax.swing.*


class NetworkAction : AnAction() {

    private var project: Project? = null

    private var mHttpCallLayout: HttpCallLayout? = null

    private var mEditor: Editor? = null

    private var currentFile: PsiFile? = null

    override fun actionPerformed(e: AnActionEvent) {
        mEditor = (e.getData(PlatformDataKeys.EDITOR) ?: return)
        project = e.getData(PlatformDataKeys.PROJECT)
        currentFile = e.getData(PlatformDataKeys.PSI_FILE)?.originalFile

        showDialog()
    }


    private fun showDialog() {
        val jDialog = JDialog()
        jDialog.title = "XBF"
        jDialog.setSize(HttpCallLayout.LAYOUT_WIDTH, FONT_SIZE * 37)
        jDialog.setLocation(FONT_SIZE * 30, FONT_SIZE * 14)
        jDialog.layout = null

        mHttpCallLayout = HttpCallLayout(true, true)
        mHttpCallLayout?.setBounds(0, 0, HttpCallLayout.LAYOUT_WIDTH, FONT_SIZE * 28)

        jDialog.add(mHttpCallLayout)
        initBtnViews(jDialog)
        jDialog.isVisible = true
    }

    private fun initBtnViews(jdDialog: JDialog) {
        val btnSpannedFile = JButton("生成方法")
        btnSpannedFile.setBounds((FONT_SIZE * 7.8).toInt(), FONT_SIZE * 31, (FONT_SIZE * 7.1).toInt(), (FONT_SIZE * 2.8).toInt())
        btnSpannedFile.addActionListener {
            if (ProjectConfig.isDebug) {
                show()
            } else {
                executeCouldRollBackAction(project) {
                    show()
                    jdDialog.isVisible = false
                }
            }
        }

        val btnCancel = JButton("取消")
        btnCancel.setBounds(0, FONT_SIZE * 31, (FONT_SIZE * 7.1).toInt(), (FONT_SIZE * 2.8).toInt())
        btnCancel.addActionListener {
            jdDialog.isVisible = false
        }

        jdDialog.add(btnCancel)
        jdDialog.add(btnSpannedFile)
    }

    private fun show() {
        val result = HttpCoreUtils.getHttpCallCommon(mHttpCallLayout!!).replace("\$dataContent", "")
        if (ProjectConfig.isDebug) {
            showCommonDialog(result)
        } else {
            val document = mEditor!!.document
            document.insertString(mEditor!!.caretModel.offset, result)

            executeAfterTime(project) {
                FileFormatUtils.format(project, currentFile!!)
            }
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val a = NetworkAction()
            ProjectConfig.isDebug = true
            a.showDialog()
        }
    }
}
