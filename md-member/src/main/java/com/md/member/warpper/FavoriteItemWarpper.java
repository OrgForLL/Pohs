package com.md.member.warpper;

import java.util.List;
import java.util.Map;


import com.md.goods.factory.UploadFactory;
import com.stylefeng.guns.core.base.warpper.BaseControllerWarpper;

/**
 * 收藏的包装类
 *
 * @authr 
 * @date 2017年2月19日15:07:29
 */
public class FavoriteItemWarpper extends BaseControllerWarpper {

    public FavoriteItemWarpper(List<Map<String, Object>> list) {
        super(list);
    }

    @Override
    public void warpTheMap(Map<String, Object> map) {
    	 map.put("images", UploadFactory.me().getAllUploadFileUrl(map.get("images").toString()));
    }

}
