package cn.com.pism.batslog.ui;

import cn.com.pism.batslog.settings.BatsLogSettingState;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.ui.ColorChooser;
import com.intellij.util.ui.JBUI;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static cn.com.pism.batslog.constants.BatsLogConstant.KEY_WORD_DEF_COL;

/**
 * @author wangyihuai
 * @date 2021/05/27 17:16
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ColorButton extends JButton {

    private Color selectColor;

    private BatsLogSettingState service;

    @Override
    protected void init(String text, Icon icon) {
        super.init(text, icon);
    }

    ColorButton(Project project, Color color) {
        this.service = ServiceManager.getService(project, BatsLogSettingState.class);
        if (color != null) {
            this.selectColor = color;
        } else {
            this.selectColor = KEY_WORD_DEF_COL;
            service.setKeyWordDefCol(KEY_WORD_DEF_COL);
        }
        buttonInit(project, color);
    }

    ColorButton() {
        buttonInit(null, null);
    }

    private void buttonInit(Project project, Color color) {
        setMargin(JBUI.emptyInsets());
        setFocusable(false);
        setDefaultCapable(false);
        setFocusable(false);
        if (color != null) {
            selectColor = color;
        }
        MouseAdapter adapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Color color = ColorChooser.chooseColor(new JPanel(), "选择颜色", selectColor);
                if (color != null) {
                    selectColor = color;
                    service.setKeyWordDefCol(color);
                }
                super.mouseClicked(e);
            }
        };
        addMouseListener(adapter);
    }


    @Override
    public void paint(Graphics g) {
        final Color color = g.getColor();
        g.setColor(selectColor);
        g.fillRect(0, 0, 12, 12);
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
        return new Dimension(12, 12);
    }


}