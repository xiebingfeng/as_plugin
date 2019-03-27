package main.module.allocation

import com.intellij.psi.PsiFile
import javax.swing.JCheckBox
import javax.swing.JLabel
import javax.swing.JPanel

class ModuleAllocationManager {

    val listModule = ArrayList<ModuleEntity>()

    fun create(container: ModuleAllocationContainer, psiFile: PsiFile?) {
        var index = 0
        psiFile?.text?.split("\n")?.forEach {
            val string = it.split("=")
            if (string.size < 2) {
                return@forEach
            }
            val entity = ModuleEntity(string[0], string[1].toBoolean(), index)
            index++
            listModule.add(entity)
        }

        index = 0
        listModule.forEach {
            val checkContainer = JPanel()
            checkContainer.apply {
                val showText = JLabel(it.name)
                val checkBox = JCheckBox(index.toString(), it.checked)
                checkBox.addActionListener {
                    listModule.get(checkBox.text.toInt()).checked = checkBox.isSelected
                }
                add(showText)
                add(checkBox)
                index++
            }

            container.container.add(checkContainer)
        }

    }

}