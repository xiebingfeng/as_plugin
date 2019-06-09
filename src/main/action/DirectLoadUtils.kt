package main.action

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.PsiManager
import com.intellij.psi.impl.file.PsiDirectoryFactory
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import org.w3c.dom.Element
import main.utils.showCommonDialog
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

/**
 * 初始化各种路径
 */
class DirectLoadUtils {
    //当前选中的包目录::       //PsiDirectory:/Users/xiebingfeng/IdeaProjects/MyApplication/app/src/main/java/com/example
    var directory: PsiDirectory? = null

    //包目录Factory
    var psiFileFactory: PsiFileFactory? = null

    //工程根目录文件路径  ::   /Users/xiebingfeng/IdeaProjects/MyApplication/app
    var projectBaseFilePath = ""

    //当前选中的包目录文件路径  ::  /Users/xiebingfeng/IdeaProjects/MyApplication/app/src/main/java/com/example/xiebingfeng/test
    var packageFilePath = ""

    //文件的包名   ::   com.example.xiebingfeng.myapplication.aaa.bbb
    var packageDeclare = ""

    //Module下的包名  R 文件 :   com.example.xiebingfeng.myapplication
    var packageName = ""

    //PsiDirectory:/Users/xiebingfeng/IdeaProjects/MyApplication/app/src/main
    var srcMainDir: PsiDirectory? = null

    fun load(event: AnActionEvent) {
        try {
            val project = event.getData(PlatformDataKeys.PROJECT)

            project?.let {
                val dataContext = event.dataContext
                val module = LangDataKeys.MODULE.getData(dataContext)
                module?.let {
                    val navigatable = LangDataKeys.NAVIGATABLE.getData(dataContext)
                    val directory: PsiDirectory? =
                            if (navigatable is PsiDirectory) {
                                srcMainDir = navigatable
                                while (!srcMainDir.toString().endsWith("src" + File.separator + "main")) {
                                    srcMainDir = srcMainDir?.parentDirectory
                                    if (srcMainDir == null) {
                                        return@let
                                    }
                                }
                                navigatable
                            } else {
                                val root = ModuleRootManager.getInstance(module)
                                var tempDirectory: PsiDirectory? = null
                                for (file in root.sourceRoots) {
                                    tempDirectory = PsiManager.getInstance(project).findDirectory(file)
                                    if (tempDirectory != null) {
                                        break
                                    }
                                }
                                tempDirectory
                            }
                    //PsiDirectory:/Users/xiebingfeng/IdeaProjects/MyApplication/app/src/main/java/com/example
                    directory?.let {
                        getPackageName(directory)
                        this.directory = it
                        val directoryFactory = PsiDirectoryFactory.getInstance(directory.project)
                        val packageName = directoryFactory.getQualifiedName(directory, false)
                        psiFileFactory = PsiFileFactory.getInstance(project)
                        packageDeclare = if (packageName.isNotEmpty()) "$packageName" else ""
                    }
                }
            }
        } catch (e: Throwable) {
            showCommonDialog("解析文件包了错")
            throw e
        }
    }

    /**
     * 从AndroidManifest.xml文件中获取当前app的包名
     * @return
     */
    private fun getPackageName(directory: PsiDirectory) {
        val dbf = DocumentBuilderFactory.newInstance()
        try {
            val db = dbf.newDocumentBuilder()
            val directoryStr = directory.toString()

            packageFilePath = directoryStr.subSequence(directoryStr.indexOf(":") + 1, directoryStr.length).toString()

            projectBaseFilePath = directoryStr.subSequence(directoryStr.indexOf(":") + 1,
                    directoryStr.indexOf(File.separator + "src" + File.separator + "main")).toString()

            val a = "$projectBaseFilePath" + File.separator + "src" + File.separator + "main" + File.separator + "AndroidManifest.xml"
            val doc = db.parse(a)
            val nodeList = doc.getElementsByTagName("manifest")
            for (i in 0 until nodeList.length) {
                val node = nodeList.item(i)
                val element = node as Element
                packageName = element.getAttribute("package")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}