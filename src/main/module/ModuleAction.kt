package main.module

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import main.config.ProjectConfig
import main.executeCouldRollBackAction
import main.module.allocation.ModuleAllocationContainer
import main.module.allocation.ModuleAllocationManager
import main.module.create.ModuleCreateAction
import main.utils.showCommonDialog
import java.awt.Color
import java.lang.StringBuilder

import javax.swing.*


class ModuleAction : AnAction() {

    private var project: Project? = null

    private var psiFile: PsiFile? = null

    private var mManager: ModuleAllocationManager? = null

    override fun actionPerformed(e: AnActionEvent) {
        project = e.project
        if (project == null) {
            return
        }
        val file = project?.projectFile?.parent?.parent?.findChild("modules.properties")
        if (file == null) {
            showCommonDialog("找不到 modules.properties文件")
            return
        }

        psiFile = PsiManager.getInstance(project!!).findFile(file)

        showDialog()
//        ModuleCreateAction().showDialog(project, this)
    }

    private fun showDialog() {
        val container = ModuleAllocationContainer()

        mManager = ModuleAllocationManager()
        mManager?.create(container, psiFile)
        container.setBounds(0, 0, ModuleAllocationContainer.LAYOUT_WIDTH, 400)
        container.background = Color.RED

        val jDialog = JDialog()
        jDialog.title = "KRT"
        jDialog.setSize(ModuleAllocationContainer.LAYOUT_WIDTH, 500)
        jDialog.setLocation(400, 200)
        jDialog.layout = null

        jDialog.add(container)
        initBtnViews(jDialog)
        jDialog.isVisible = true
    }


    private fun initBtnViews(jdDialog: JDialog) {
        val btnSpannedFile = JButton("修改配置")
        btnSpannedFile.setBounds(110, 430, 100, 40)
        btnSpannedFile.addActionListener {
            if (ProjectConfig.isDebug) {
                modify()
            } else {
                executeCouldRollBackAction(project) {
                    modify()
                    jdDialog.isVisible = false
                }
            }
        }

        val btnCancel = JButton("取消")
        btnCancel.setBounds(0, 430, 100, 40)
        btnCancel.addActionListener {
            jdDialog.isVisible = false
        }

        val showText = JLabel("修改完成后请  sync project with Gradle Files")
        showText.setBounds(0, 390, 300, 40)

        val btnCreateModule = JButton("新建Module")
        btnCreateModule.setBounds(220, 430, 100, 40)
        btnCreateModule.addActionListener {
            jdDialog.isVisible = false
            ModuleCreateAction().showDialog(project, this)
        }

        jdDialog.add(btnCancel)
        jdDialog.add(btnSpannedFile)
        jdDialog.add(showText)
        jdDialog.add(btnCreateModule)
    }

    private fun modify() {
        if (mManager?.listModule?.size ?: 0 <= 0) {
            return
        }
        val result = StringBuilder()
        mManager?.listModule?.forEach {
            result.append(it.name).append("=").append(it.checked).append("\n")
        }

        if (ProjectConfig.isDebug) {
            showCommonDialog(result.toString())
        } else {
            psiFile?.originalFile?.viewProvider?.document?.setText(result)
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val a = ModuleAction()
            ProjectConfig.isDebug = true
            a.showDialog()

//            ProjectConfig.isDebug = true
//            val a = ModuleCreateAction()
//            a.showDialog(null, null)
        }
    }
}
