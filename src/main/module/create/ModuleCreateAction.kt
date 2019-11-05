package main.module.create

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.project.Project
import main.config.ProjectConfig
import main.executeCouldRollBackAction
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.JButton
import javax.swing.JDialog

class ModuleCreateAction {

    private var project: Project? = null
    private var mAction: AnAction? = null

    private var mModuleCreateLayout: ModuleCreateLayout? = null

    fun showDialog(project: Project?, action: AnAction?) {
        this.project = project
        this.mAction = action

        mModuleCreateLayout = ModuleCreateLayout()
        mModuleCreateLayout?.setBounds(0, 0, ModuleCreateLayout.LAYOUT_WIDTH, 400)

        val jDialog = JDialog()
        jDialog.title = "XBF-CREATE-MODULE"
        jDialog.setSize(ModuleCreateLayout.LAYOUT_WIDTH, 500)
        jDialog.setLocation(400, 200)
        jDialog.layout = null

        jDialog.add(mModuleCreateLayout)
        initBtnViews(jDialog)
        jDialog.isVisible = true
    }

    private fun initBtnViews(jdDialog: JDialog) {
        val btnSpannedFile = JButton("生成文件")
        btnSpannedFile.setBounds(0, 420, 130, 40)
        btnSpannedFile.addActionListener {
            if (ProjectConfig.isDebug) {
                ModuleCreateManager().create(project, mAction, mModuleCreateLayout!!)
            } else {
                executeCouldRollBackAction(project) {
                    ModuleCreateManager().create(project, mAction, mModuleCreateLayout!!)
                }
            }
            jdDialog.isVisible = false
        }

        val btnCancel = JButton("取消")
        btnCancel.setBounds(140, 420, 130, 40)
        btnCancel.addActionListener {
            jdDialog.isVisible = false
        }
        jdDialog.add(btnCancel)
        jdDialog.add(btnSpannedFile)

        jdDialog.addWindowListener(object : WindowAdapter() {
            override fun windowClosing(e: WindowEvent?) {
            }
        })
    }

}