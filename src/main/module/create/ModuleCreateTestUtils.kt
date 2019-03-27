package main.module.create

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFileFactory
import main.module.create.ModuleCreateManager.Companion.MODULE_NAME_UP
import main.module.create.ModuleCreateManager.Companion.PACKAGE_NAME
import main.utils.FileIOUtils2
import main.utils.toCustomUpCase
import main.utils.toPackageName
import main.filetype.JavaFileType

object ModuleCreateTestUtils {

    fun createAndroidText(srcDirectory: PsiDirectory, newModuleName: String?, action: AnAction?) {
        srcDirectory.createSubdirectory("java").apply {
            createSubdirectory("com").apply {
                createSubdirectory("krt").apply {
                    createSubdirectory(newModuleName!!.toPackageName()).apply {
                        FileIOUtils2.readTemplateFile("/newModule/androidtest/ExampleInstrumentedTest.txt", action)
                                .replace(PACKAGE_NAME, newModuleName.toPackageName())
                                .replace(MODULE_NAME_UP, newModuleName.toCustomUpCase()).let {
                                    PsiFileFactory.getInstance(project)?.createFileFromText("ExampleInstrumentedTest.java", JavaFileType(), it)?.let {
                                        this.add(it)
                                    }
                                }
                    }
                }
            }
        }
    }

    fun createTest(srcDirectory: PsiDirectory, newModuleName: String?, action: AnAction?) {
        srcDirectory.createSubdirectory("java").apply {
            createSubdirectory("com").apply {
                createSubdirectory("krt").apply {
                    createSubdirectory(newModuleName!!.toPackageName()).apply {
                        FileIOUtils2.readTemplateFile("/newModule/androidtest/ExampleUnitTest.txt", action)
                                .replace(PACKAGE_NAME, newModuleName.toPackageName())
                                .replace(MODULE_NAME_UP, newModuleName.toCustomUpCase()).let {
                                    PsiFileFactory.getInstance(project)?.createFileFromText("ExampleUnitTest.java", JavaFileType(), it)?.let {
                                        this.add(it)
                                    }
                                }
                    }
                }
            }
        }
    }


}