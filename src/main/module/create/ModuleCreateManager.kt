package main.module.create

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.PsiManager
import main.executeAfterTime
import main.executeCouldRollBackAction
import main.filetype.*
import main.utils.*


class ModuleCreateManager {

    private var newModuleName: String? = null
    private var moduleRemark: String? = null
    private var moduleCreateLayout: ModuleCreateLayout? = null
    private var action: AnAction? = null
    private var project: Project? = null
    private var moduleType = 0
    private var packageDifference = "xbf"

    fun create(project: Project?, action: AnAction?, moduleCreateLayout: ModuleCreateLayout) {
        this.project = project
        this.moduleCreateLayout = moduleCreateLayout
        this.action = action

        moduleRemark = moduleCreateLayout.etRemark?.text ?: ""
        newModuleName = moduleCreateLayout.etModuleName?.text
        packageDifference = moduleCreateLayout.etPackageName?.text ?: "xbf"
        if (newModuleName == null || newModuleName!!.isEmpty()) {
            showCommonDialog("请输入模块名")
            return
        }

        val preFixString = moduleCreateLayout.etPreFix?.text
        if (preFixString == null || preFixString.isEmpty()) {
            showCommonDialog("请输入preFix")
            return
        }

        val modulesFile = project?.projectFile?.parent?.parent?.findChild("modules.properties")
        if (modulesFile == null) {
            showCommonDialog("找不到 modules.properties文件")
            return
        }

        val gradleModuleName = preFixString.toBuildGradleModuleName()
        moduleCreateLayout.etPreFix?.text = gradleModuleName

        var rootModuleDir: PsiDirectory? = null
        if (moduleCreateLayout.rtModuleNormal?.isSelected == true) {
            moduleType = 0
        } else if (moduleCreateLayout.rtModuleBusiness?.isSelected == true) {
            val file = project.projectFile?.parent?.parent?.findChild("modules")
            if (file == null) {
                showCommonDialog("找不到 modules")
                return
            }
            moduleType = 1
            rootModuleDir = PsiManager.getInstance(project).findDirectory(file)
        } else if (moduleCreateLayout.rtModuleLibs?.isSelected == true) {
            val file = project.projectFile?.parent?.parent?.findChild("modulelibs")
            if (file == null) {
                showCommonDialog("找不到 modulelibs")
                return
            }
            moduleType = 2
            rootModuleDir = PsiManager.getInstance(project).findDirectory(file)
        }

        if (rootModuleDir?.findSubdirectory(newModuleName + "Module") != null) {
            showCommonDialog("有相同的模块名")
            return
        }

        ////////////////////////////////////////////////
        ////////////////////////////////////////////////
        ////////////////////////////////////////////////
        ////////////////////////////////////////////////
        ////////////////////////////////////////////////
        ////////////////////////////////////////////////
        ////////////////////////////////////////////////

        if (moduleType == 1 && !ModuleCreateFindUtils.createComponentModule(project, action, newModuleName!!, packageDifference)) {
            return
        }
        ////////以上为检验，前提条件
        executeAfterTime(project) {
            createString2modulesFile(modulesFile)
            addModuleToSettingsGradle()

            val newModuleDir = rootModuleDir?.createSubdirectory(newModuleName + "Module")

            newModuleDir?.apply {
                createSubdirectory("libs")
                val srcDir = createSubdirectory("src")
                val gitignoreString = FileIOUtils2.readTemplateFile("/newModule/gitignore.txt", action)
                val localgradleString = FileIOUtils2.readTemplateFile("/newModule/localgradle.txt", action)
                val proguardRulesString = FileIOUtils2.readTemplateFile("/newModule/proguard-rules.txt", action)
                val buildGradleString = FileIOUtils2.readTemplateFile("/newModule/buildGradle.txt", action)
                        .replace("\$moduleName", newModuleName!!.toBuildGradleModuleName())
                        .replace("\$applicationId", newModuleName!!.toBuildGradleApplicationId())
                        .replace(ModuleCreateManager.PACKAGE_DIFFERENCE, packageDifference)
                        .replace("\$resourcePrefix", preFixString)
                PsiFileFactory.getInstance(project)?.createFileFromText(".gitignore", NullFileType(), gitignoreString)?.let {
                    add(it)
                }
                PsiFileFactory.getInstance(project)?.createFileFromText("local.gradle", GradleFileType(), localgradleString)?.let {
                    add(it)
                }
                PsiFileFactory.getInstance(project)?.createFileFromText("proguard-rules.pro", ProFileType(), proguardRulesString)?.let {
                    add(it)
                }
                PsiFileFactory.getInstance(project)?.createFileFromText("build.gradle", GradleFileType(), buildGradleString)?.let {
                    add(it)
                }

                addSrc(srcDir)
            }
        }
    }

    private fun addSrc(srcDir: PsiDirectory) {
        srcDir.apply {
            createSubdirectory("main").apply {
                addMain(this)
            }
            createSubdirectory("androidTest").apply {
                ModuleCreateTestUtils.createAndroidText(this, newModuleName, action, packageDifference)
            }

            createSubdirectory("test").apply {
                ModuleCreateTestUtils.createTest(this, newModuleName, action, packageDifference)
            }

        }
    }

    private fun addMain(mainDir: PsiDirectory) {
        mainDir.apply {
            createSubdirectory("java").apply {
                createSubdirectory("com").apply {
                    createSubdirectory(packageDifference).apply {
                        createSubdirectory(newModuleName!!.toPackageName()).apply {
                            addPackageNameDir(this)
                            createSubdirectory("debug").apply {
                                addDebugDir(this)
                            }
                        }
                    }
                }
            }

            createSubdirectory("manifest").let { manifestDir ->
                FileIOUtils2.readTemplateFile("/newModule/DebugAndroidManifest.txt", action)
                        .replace(PACKAGE_NAME, newModuleName!!.toPackageName())
                        .replace(PACKAGE_DIFFERENCE, packageDifference)
                        .replace(MODULE_NAME_UP, newModuleName!!.toCustomUpCase()).let {
                            PsiFileFactory.getInstance(project)?.createFileFromText("AndroidManifest.xml", XmlFileType(), it)?.let {
                                manifestDir.add(it)
                            }
                        }
            }
            createSubdirectory("res").apply {
                createSubdirectory("drawable")
                createSubdirectory("layout")
                createSubdirectory("values")
            }

            FileIOUtils2.readTemplateFile("/newModule/AndroidManifest.txt", action)
                    .replace(PACKAGE_NAME, newModuleName!!.toPackageName())
                    .replace(PACKAGE_DIFFERENCE, packageDifference)
                    .replace(MODULE_NAME_UP, newModuleName!!.toCustomUpCase()).let {
                        PsiFileFactory.getInstance(project)?.createFileFromText("AndroidManifest.xml", XmlFileType(), it)?.let {
                            add(it)
                        }
                    }
        }
    }

    /**
     * 如在  com.xbf.good  下添加文件
     */
    private fun addPackageNameDir(packageDir: PsiDirectory) {
        FileIOUtils2.readTemplateFile("/newModule/ModuleService.txt", action)
                .replace(PACKAGE_NAME, newModuleName!!.toPackageName())
                .replace(ModuleCreateManager.PACKAGE_DIFFERENCE, packageDifference)
                .replace(MODULE_NAME_UP, newModuleName!!.toCustomUpCase()).let {
                    PsiFileFactory.getInstance(project)?.createFileFromText(newModuleName!!.toCustomUpCase() + "ModuleService.kt", KotlinFileType(), it)?.let { file ->
                        executeCouldRollBackAction(project) {
                            FileFormatUtils.format(project, packageDir.add(file))
                        }
                    }
                }

        FileIOUtils2.readTemplateFile("/newModule/ModuleApp.txt", action)
                .replace(PACKAGE_NAME, newModuleName!!.toPackageName())
                .replace("\$NormalModuleName", newModuleName!!)
                .replace(ModuleCreateManager.PACKAGE_DIFFERENCE, packageDifference)
                .replace(MODULE_NAME_UP, newModuleName!!.toCustomUpCase()).let {
                    PsiFileFactory.getInstance(project)?.createFileFromText(newModuleName!!.toCustomUpCase() + "ModuleApp.kt", KotlinFileType(), it)?.let { file ->
                        executeCouldRollBackAction(project) {
                            FileFormatUtils.format(project, packageDir.add(file))
                        }
                    }
                }
    }

    /**
     * 如在  com.xbf.good.debug  下添加文件
     */
    private fun addDebugDir(debugDir: PsiDirectory) {
        FileIOUtils2.readTemplateFile("/newModule/TestActivity.txt", action)
                .replace(MODULE_REMARK, moduleRemark!!)
                .replace(PACKAGE_NAME, newModuleName!!.toPackageName())
                .replace(ModuleCreateManager.PACKAGE_DIFFERENCE, packageDifference)
                .replace(MODULE_NAME_UP, newModuleName!!.toCustomUpCase()).let {
                    PsiFileFactory.getInstance(project)?.createFileFromText(newModuleName!!.toCustomUpCase() + "TestActivity.kt", KotlinFileType(), it)?.let { file ->
                        executeCouldRollBackAction(project) {
                            debugDir.add(file)
                        }
                    }
                }

        FileIOUtils2.readTemplateFile("/newModule/ModuleApplication.txt", action)
                .replace(MODULE_REMARK, moduleRemark!!)
                .replace(PACKAGE_NAME, newModuleName!!.toPackageName())
                .replace(ModuleCreateManager.PACKAGE_DIFFERENCE, packageDifference)
                .replace(MODULE_NAME_UP, newModuleName!!.toCustomUpCase()).let {
                    PsiFileFactory.getInstance(project)?.createFileFromText(newModuleName!!.toCustomUpCase() + "ModuleApplication.kt", KotlinFileType(), it)?.let { file ->
                        executeCouldRollBackAction(project) {
                            debugDir.add(file)
                        }
                    }
                }
    }

    /**
     * 在 modules.properties 下添加配置
     */
    private fun createString2modulesFile(file: VirtualFile) {
        val modulesFile = PsiManager.getInstance(project!!).findFile(file)
        val text = modulesFile?.text ?: ""

        var insertText = "is_" + newModuleName!!.toBuildGradleModuleName() + "_module_run_alone"

        if (text.contains(insertText)) {
            return
        } else {
            if (!text.endsWith("\n")) {
                insertText = "\n" + insertText
            }
            modulesFile!!.viewProvider.document?.setText("$text$insertText=false\n")
        }
    }

    /**
     * 在 settings.gradle 添加配置信息
     */
    private fun addModuleToSettingsGradle() {
        val file = project?.projectFile?.parent?.parent?.findChild("settings.gradle")!!
        val settingsFile = PsiManager.getInstance(project!!).findFile(file)
        var settingsText = settingsFile?.text ?: ""

        if (!settingsText.endsWith("\n")) {
            settingsText += "\n"
        }

        when (moduleType) {
            0 -> {

            }
            1 -> settingsFile!!.viewProvider.document?.setText(settingsText + "include 'modules:" + newModuleName + "Module'")
            2 -> settingsFile!!.viewProvider.document?.setText(settingsText + "include 'modulelibs:" + newModuleName + "Module'")
        }
    }

    companion object {
        const val MODULE_REMARK = "\$ModuleRemark"
        const val MODULE_NAME_UP = "\$ModuleNameUp"
        const val PACKAGE_DIFFERENCE = "\$packageDifference"
        const val PACKAGE_NAME = "\$packageName"
    }
}

