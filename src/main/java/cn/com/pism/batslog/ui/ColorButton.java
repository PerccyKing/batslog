package cn.com.pism.batslog.ui;

import com.intellij.openapi.project.Project;
import com.intellij.ui.ColorChooser;
import com.intellij.util.ui.JBUI;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.swing.*;
import java.awt.*;

import static cn.com.pism.batslog.constants.BatsLogConstant.KEY_WORD_DEF_COL;

/**
 * @author wangyihuai
 * @date 2021/05/27 17:16
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ColorButton extends JButton {

    private Color selectColor;

    private int width;
    private int height;

    @Override
    protected void init(String text, Icon icon) {
        super.init(text, icon);
    }

    public ColorButton(Project project, Color color, int width, int height, Callback callback) {
        this.width = width;
        this.height = height;
        if (color != null) {
            this.selectColor = color;
        } else {
            this.selectColor = KEY_WORD_DEF_COL;
        }
        buttonInit(project, color, callback);
    }

    public ColorButton(Project project, Color color) {
        new ColorButton(project, color, 12, 12);
    }

    public ColorButton(Project project, Color color, int width, int height) {
        new ColorButton(project, color, width, height, color1 -> this.selectColor = color);
    }

    public ColorButton() {
        buttonInit(null, null, color -> this.selectColor = color);
    }

    private void buttonInit(Project project, Color color, Callback callback) {
        setMargin(JBUI.emptyInsets());
        setFocusable(false);
        setDefaultCapable(false);
        setFocusable(false);
        if (color != null) {
            selectColor = color;
        }
        addActionListener(e -> {
            Color color1 = ColorChooser.chooseColor(new JPanel(), "选择颜色", selectColor);
            if (color1 != null) {
                selectColor = color1;
                callback.callback(selectColor);
            }
        });
    }


    @Override
    public void paint(Graphics g) {
        final Color color = g.getColor();
        if (selectColor == null) {
            g.setColor(KEY_WORD_DEF_COL);
        } else {
            g.setColor(selectColor);
        }
        g.fillRect(0, 0, this.width, this.height);
        g.setColor(color);
    }

    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    @Override
    public Dimension getMaximumSize() {
        return getPreferredSize();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(this.width, this.height);
    }

    public interface Callback {
        /**
         * 回调
         *
         * @param choseColor 选中的颜色
         */
        void callback(Color choseColor);
    }
}