package com.cjh.wechatmp.avatar;

import com.cjh.wechatmp.api.CloudService;
import com.cjh.wechatmp.enums.InstructsEnum;
import com.cjh.wechatmp.message.BaseMessage;
import com.cjh.wechatmp.message.MessageUtil;
import com.cjh.wechatmp.message.in.TextInMessage;
import com.cjh.wechatmp.redis.RedisService;
import com.cjh.wechatmp.util.ByteUtil;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class AvatarService {

    private CloudService cloudService;
    private RedisService redisService;

    public BaseMessage handleMessage(TextInMessage textInMessage) {
        String content = textInMessage.getContent();
        StringBuilder stringBuilder = null;
        String lastInstruct = redisService.getLastInstruct(textInMessage.getFromUserName(), false);
        if (content.equals("头像") ||
            (InstructsEnum.Instruct9.getCode().toString().equals(lastInstruct)
                && content.equals(InstructsEnum.Instruct91.getCode().toString()))) {
            String mediaId = cloudService.getAvatar();
            if (mediaId == null) {
                return MessageUtil.buildTextOutMessage(textInMessage, "请稍后再试");
            }
            return MessageUtil.buildImgOutMessage(textInMessage, mediaId);
        } else if (content.contains("头像#") ||
            (InstructsEnum.Instruct9.getCode().toString().equals(lastInstruct)
                && content.equals(InstructsEnum.Instruct92.getCode().toString()))) {
            String pageNum = content.substring(content.indexOf("#") + 1);
            int num = Integer.parseInt(pageNum);
            List<AvatarPO> avatars = cloudService.getAvatarByNew(num);
            if (!avatars.isEmpty()) {
                stringBuilder = new StringBuilder();
                for (AvatarPO avatar : avatars) {
                    stringBuilder.append("<a href=\"").append(avatar.getHref()).append("\">")
                        .append(avatar.getTitle())
                        .append("</a>\n");
                }
            }
            return MessageUtil.buildTextOutMessage(textInMessage,
                stringBuilder != null ? ByteUtil.limit2048byte(stringBuilder.toString()) : "请稍后再试");
        }
        return null;
    }
}
