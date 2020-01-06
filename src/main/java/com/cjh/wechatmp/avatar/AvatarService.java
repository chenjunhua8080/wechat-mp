package com.cjh.wechatmp.avatar;

import com.cjh.wechatmp.api.CloudService;
import com.cjh.wechatmp.message.BaseMessage;
import com.cjh.wechatmp.message.MessageUtil;
import com.cjh.wechatmp.message.in.TextInMessage;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class AvatarService {

    private CloudService cloudService;

    public BaseMessage handleMessage(TextInMessage textInMessage) {
        String content = textInMessage.getContent();
        StringBuilder stringBuilder = null;
        if (content.contains("头像")) {
            String mediaId = cloudService.getAvatar();
            if (mediaId == null) {
                return MessageUtil.buildTextOutMessage(textInMessage, "请稍后再试");
            }
            return MessageUtil.buildImgOutMessage(textInMessage, mediaId);
        } else if (content.contains("头像#")) {
            String pageNum = content.substring(content.indexOf("#") + 1);
            int num = Integer.parseInt(pageNum);
            List<AvatarPO> avatars = cloudService.getAvatarByNew(num);
            if (!avatars.isEmpty()) {
                stringBuilder = new StringBuilder();
                for (AvatarPO avatar : avatars) {
                    stringBuilder.append("<a href=\"").append(avatar.getHref()).append("\">").append(avatar.getTitle())
                        .append("</a>\n");
                }
            }
            return MessageUtil.buildTextOutMessage(textInMessage,
                stringBuilder != null ? stringBuilder.toString() : "请稍后再试");
        }
        return null;
    }
}
