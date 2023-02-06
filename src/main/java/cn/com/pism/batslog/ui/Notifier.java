package cn.com.pism.batslog.ui;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nullable;

/**
 * @author PerccyKing
 * @version 0.0.1
 * @since 2021/10/12 上午 09:58
 */
public class Notifier {

    private Notifier() {
    }

    public static Notification getInstance(NotificationType type) {
        return getNotificationGroup().createNotification("", type);
    }

    private static NotificationGroup getNotificationGroup() {
        return NotificationGroup.findRegisteredGroup("cn.com.pism.batslog.notification");
    }

    @SuppressWarnings("unused")
    public static void notifyError(@Nullable Project project, String content) {
        getNotificationGroup().createNotification(content, NotificationType.ERROR)
                .notify(project);
    }

}
