package cn.com.pism.batslog.ui;

import cn.com.pism.batslog.constants.BatsLogConstant;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nullable;

/**
 * @author PerccyKing
 * @version 0.0.1
 * @date 2021/10/12 上午 09:58
 * @since 0.0.1
 */
public class Notifier {
    private static final NotificationGroup NOTIFICATION_GROUP =
            new NotificationGroup(BatsLogConstant.BATS_LOG_NAME, NotificationDisplayType.BALLOON, true);

    public static Notification getInstance(NotificationType type) {
        return NOTIFICATION_GROUP.createNotification(type);
    }

    public static void notifyError(@Nullable Project project, String content) {
        NOTIFICATION_GROUP.createNotification(content, NotificationType.ERROR)
                .notify(project);
    }

}
