package main.utils

import java.awt.Font
import javax.swing.JCheckBox
import javax.swing.JLabel
import javax.swing.JRadioButton
import javax.swing.JTextField

fun JTextField.toCustomSize(fontSize: Int = FONT_SIZE): JTextField {
    this.font = Font("宋体", Font.PLAIN, fontSize)
    return this
}

fun JLabel.toCustomSize(fontSize: Int = FONT_SIZE): JLabel {
    this.font = Font("宋体", Font.PLAIN, fontSize)
    return this
}

fun JCheckBox.toCustomSize(fontSize: Int = FONT_SIZE): JCheckBox {
    this.font = Font("宋体", java.awt.Font.PLAIN, fontSize)
    return this
}

fun JRadioButton.toCustomSize(fontSize: Int = main.utils.FONT_SIZE): JRadioButton {
    this.font = Font("宋体", java.awt.Font.PLAIN, fontSize)
    return this
}

const val FONT_SIZE = 14