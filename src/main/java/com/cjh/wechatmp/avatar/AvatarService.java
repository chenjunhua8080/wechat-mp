package com.cjh.wechatmp.avatar;

import com.cjh.wechatmp.api.CloudService;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class AvatarService {

    private CloudService cloudService;

    public String handleMessage(String content) {
        String result = null;
        List<AvatarPO> avatars = new ArrayList<>();
        if (content.contains("头像")) {
            avatars = cloudService.getAvatarByNew(1);
        } else if (content.contains("头像#1")) {
            String pageNum = content.substring(content.indexOf("#") + 1);
            avatars = cloudService.getAvatarByNew(Integer.parseInt(pageNum));
        }
        if (!avatars.isEmpty()) {
            result = "";
            for (AvatarPO avatar : avatars) {
                result += "<a href=\"" + avatar.getHref() + "\">" + avatar.getTitle() + "</a>\n";
            }
        }
        return result;
    }
}
