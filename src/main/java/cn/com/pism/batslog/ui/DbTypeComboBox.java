package cn.com.pism.batslog.ui;

import com.intellij.openapi.ui.ComboBox;

import javax.swing.*;

/**
 * @author wangyihuai
 * @date 2020/10/28 下午 02:16
 */
public class DbTypeComboBox extends ComboBox<DbTypeComboBox.Item> {




    public abstract static class Item {
        private final String displayName;
        private final Icon icon;

        public Item(String displayName, Icon icon) {
            this.displayName = displayName;
            this.icon = icon;
        }

        public String getDisplayName() {
            return this.displayName;
        }

        public Icon getIcon() {
            return this.icon;
        }
    }
}
