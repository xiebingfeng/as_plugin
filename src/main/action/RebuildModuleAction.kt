//package main.action
//
//import com.intellij.openapi.actionSystem.AnAction
//import com.intellij.openapi.actionSystem.AnActionEvent
//import com.intellij.openapi.actionSystem.PlatformDataKeys
//import com.intellij.openapi.editor.Editor
//import com.intellij.openapi.project.Project
//import main.config.ProjectConfig
//import main.container.HttpCallLayout
//import main.container.ModuleBuildLayout
//import main.create.HttpCoreUtils
//import main.executeCouldRollBackAction
//import main.utils.showCommonDialog
//
//import javax.swing.*
//import com.sun.javafx.scene.CameraHelper.project
//import com.intellij.psi.search.GlobalSearchScope
//import com.intellij.psi.PsiFile
//import com.intellij.psi.search.FilenameIndex
//
//
//class RebuildModuleAction : AnAction() {
//    override fun actionPerformed(p0: AnActionEvent?) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
////
////    private var project: Project? = null
////
////    private var mModuleBuildLayout: ModuleBuildLayout? = null
////
////    private var mDirectLoadUtils: DirectLoadUtils? = null
////
////    override fun actionPerformed(e: AnActionEvent) {
////        project = e.project
////        mDirectLoadUtils = DirectLoadUtils()
////
////        val psiFiles = FilenameIndex.getFilesByName(project, "gradle.properties", GlobalSearchScope.allScope(project!!))
////        psiFiles[0].originalFile.originalFile
////
////        if (!ProjectConfig.isDebug) {
////            mDirectLoadUtils?.load(e)
////        }
////        mDirectLoadUtils?.projectBaseFilePath = project?.projectFile?.parent?.parent?.path.toString()
////
////        showDialog()
////    }
////
////    private fun showDialog() {
////        val jDialog = JDialog()
////        jDialog.title = "KRT Rebuild Module"
////        jDialog.setSize(HttpCallLayout.LAYOUT_WIDTH, 500)
////        jDialog.setLocation(400, 200)
////        jDialog.layout = null
////
////        mModuleBuildLayout = ModuleBuildLayout()
////        mModuleBuildLayout?.setBounds(0, 0, ModuleBuildLayout.LAYOUT_WIDTH, 400)
////
////        jDialog.add(mModuleBuildLayout)
////        initBtnViews(jDialog)
////        jDialog.isVisible = true
////    }
////
////    private fun initBtnViews(jdDialog: JDialog) {
////        val btnSpannedFile = JButton("生成方法")
////        btnSpannedFile.setBounds(110, 430, 100, 40)
////        btnSpannedFile.addActionListener {
////            if (ProjectConfig.isDebug) {
////                show()
////            } else {
////                executeCouldRollBackAction(project) {
////                    show()
////                    jdDialog.isVisible = false
////                }
////            }
////        }
////
////        val btnCancel = JButton("取消")
////        btnCancel.setBounds(0, 430, 100, 40)
////        btnCancel.addActionListener {
////            jdDialog.isVisible = false
////        }
////        jdDialog.add(btnCancel)
////        jdDialog.add(btnSpannedFile)
////    }
////
////    private fun show() {
////        if (ProjectConfig.isDebug) {
//////            showCommonDialog()
////        } else {
////        }
////    }
////
////    companion object {
////        @JvmStatic
////        fun main(args: Array<String>) {
////            val a = RebuildModuleAction()
////            ProjectConfig.isDebug = true
////            a.showDialog()
////        }
////    }
//}
