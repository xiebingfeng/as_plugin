package main.module.allocation

import java.awt.*
import javax.swing.JPanel
import javax.swing.JScrollPane


class ModuleAllocationContainer : JPanel() {

    var container: JPanel

    init {
        layout = GridLayout(0, 1)

        container = JPanel()
        container.layout = FlowLayout(FlowLayout.LEFT)
        container.preferredSize = Dimension(LAYOUT_WIDTH - 20, 800)

        val scrollPane = JScrollPane(container)
        scrollPane.verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED;

        add(scrollPane, BorderLayout.CENTER)
    }

    companion object {
        const val LAYOUT_WIDTH = 350
    }

}